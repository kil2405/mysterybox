package com.weplaylabs.mysterybox.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import com.weplaylabs.mysterybox.common.BaseSessionClass;
import com.weplaylabs.mysterybox.common.GameResource;
import com.weplaylabs.mysterybox.common.TimeCalculation;
import com.weplaylabs.mysterybox.model.resource.ErrorCodeResource;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/error")
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController
{
    @Value("${spring.profiles.active}")
    private String serverMode;

    @Autowired
	private BaseSessionClass bSC;

	@Autowired
	private GameResource gameResource;

    private final static Logger LOGGER = LoggerFactory.getLogger(ErrorController.class);
    private final ErrorAttributes errorAttributes;

    public ErrorController(ErrorAttributes errorAttributes)
	{
		Assert.notNull(errorAttributes, "ErrorAttributes must not be null");
		this.errorAttributes = errorAttributes;
	}

    @RequestMapping
	public ResponseEntity<Map<String, Object>> error(HttpServletRequest aRequest, HttpServletResponse response)
	{
        int customUserId = -1;
		int userId = bSC.getUserId();
		
		ServletWebRequest servletWebRequest = new ServletWebRequest(aRequest);
        ErrorAttributeOptions options = ErrorAttributeOptions.of(ErrorAttributeOptions.Include.EXCEPTION, ErrorAttributeOptions.Include.MESSAGE, ErrorAttributeOptions.Include.STACK_TRACE, ErrorAttributeOptions.Include.BINDING_ERRORS, ErrorAttributeOptions.Include.STATUS);
		Map<String, Object> result = this.errorAttributes.getErrorAttributes(servletWebRequest, options);

        LOGGER.error("userId:" + userId + " : " + result.toString());

        int customStatusCode = ((Integer) result.get("status"));

        String requestUrl = (String) result.get("path");
        String errorText[] = ((String) result.get("message")).split(":");

        try {
            customUserId = Integer.parseInt(errorText[0]);
        } catch (NumberFormatException e) {}

        if(customUserId != -1){
            userId = customUserId;
        }

        result.put("code", customStatusCode);

		ErrorCodeResource errorCodeRS = gameResource.getErrorCode().get(customStatusCode);
		if(errorCodeRS != null) {
			result.put("message", errorCodeRS.getTextEn());;
		}

        result.put("userId", userId);
		
		try
		{
			if (customStatusCode != 404 && customStatusCode != 20013)
			{
				HashMap<String, Object> param = new HashMap<>();
				param.put("in_month", TimeCalculation.getMonth());
				param.put("in_log_time", TimeCalculation.getCurrentUnixTime());
				param.put("in_user_id", userId);
				param.put("in_request_url", requestUrl);
				param.put("in_error_code", customStatusCode);
	
				if (errorText == null || errorText.length < 2)
					param.put("in_request_body", "");
				else
					param.put("in_request_body", errorText[1].substring(0,Math.min(errorText[1].length(), 4000)).trim());
	
				if (errorText == null || errorText.length < 3)
					param.put("in_error_text", "");
				else
					param.put("in_error_text", "error");
	
				//repositoryService.setErrorLog(userId, param);
			}
		}
		catch(Exception e)
		{
		}
		
		result.remove("timestamp");
		result.remove("error");
		result.remove("exception");
		result.remove("path");
		result.remove("userId");
		result.remove("status");
		if(serverMode.equals("live"))
			result.remove("trace");
		
		return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
