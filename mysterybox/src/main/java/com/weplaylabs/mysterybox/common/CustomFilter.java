package com.weplaylabs.mysterybox.common;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.weplaylabs.mysterybox.model.game.User;
import com.weplaylabs.mysterybox.redisService.RedisService;
import com.weplaylabs.mysterybox.repository.game.UserJpaRepository;
import com.weplaylabs.mysterybox.util.VersionCheckResult;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

@Component
public class CustomFilter implements Filter
{
	@Value("${server.encryption}")
    private boolean useEncryption;

	private final static Logger LOGGER = LoggerFactory.getLogger(CustomFilter.class);
	@Autowired
	private UserJpaRepository userJpaRepository;

	@Autowired
	private RedisService redisService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException
    {
    }

    @Override
    public void destroy()
    {
    }

	private VersionCheckResult CheckRedisSession(int userId) throws Exception
	{
		Gson gson = new Gson();
		
		VersionCheckResult result = new VersionCheckResult();
		result.result = ConstantVal.VERSION_CHECK_OK;

		String redisKey = "user::" + userId;
		User user = redisService.get(redisKey, User.class, gson);
		if(user == null)
		{
			// Redis에 유저 정보가 없으면 세션이 만료 (기본 4시간)
			result.result = ConstantVal.VERSION_CHECK_NO_SESSION;
			return result;
		}

		result.result = ConstantVal.VERSION_CHECK_OK;
		result.encryption = user.getEncryption();

		// 유저 정보가 있다면 세션 유지를 위해 새롭게 갱신
		userJpaRepository.save(user);
		return result;
	}

    private boolean excludeUrl(String uri, String contextPath) {
		if (uri.startsWith(contextPath + "/api/client-secure/")) {
			return false;
		} else {
			return true;
		}
	}

	//DB 시간과, 서버시간 비교 (1시간에 1번씩)
	private void CheckServerTime()
	{
		try {
			if(System.currentTimeMillis() - TimeCalculation.SERVER_TIME_REFRESH_TIME > ConstantVal.HOUR_OF_SECOND * 1000)
			{
				TimeCalculation.SERVER_TIME_REFRESH_TIME = System.currentTimeMillis();
				Long serverTime = userJpaRepository.getNowUnixTime();
				TimeCalculation.SERVER_TIME_VALUE = System.currentTimeMillis() - serverTime * 1000;
			}
		} catch (Exception e) {
			System.err.println("CheckServerTime Catch Error");
		}
	}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        byte[] encryptBytes;

        HttpServletRequest hsr = (HttpServletRequest) request;
		String requestUrl = hsr.getRequestURI().toString().trim();
		String contextPath = hsr.getContextPath();
		String encryption = null;

        if (!requestUrl.startsWith(contextPath + "/api/client-secure") && !requestUrl.startsWith(contextPath + "/api/client")) {
			System.out.println("invalid URL : " + requestUrl);
			return;
		}

		CheckServerTime();
		
        XSSRequestWrapper cloneRequest = null;
        ResponseWrapper responseWrapper = new ResponseWrapper((HttpServletResponse) response);
		if (excludeUrl(requestUrl, contextPath) || ((HttpServletRequest) request).getMethod().equals("OPTIONS"))// 여기서는 암호화 안하는 패킷
		{
			cloneRequest = new XSSRequestWrapper(hsr, null);
			
			try {
				chain.doFilter(cloneRequest, responseWrapper);
				encryptBytes = responseWrapper.getDataStream();
				response.setContentLength(encryptBytes.length);
				response.getOutputStream().write(encryptBytes);
			} catch (WeException e) {
				((HttpServletResponse) response).sendError(e.errorCode, cloneRequest.getRequestBody());
			} catch(Exception ex) {
				((HttpServletResponse) response).sendError(20010, cloneRequest.getRequestBody());
			}
			return;
		}

        String clientUserId = hsr.getHeader("userId");
		VersionCheckResult versionCheckResult = null;

		if(clientUserId != null)
		{
			try {
				versionCheckResult = CheckRedisSession(Integer.parseInt(clientUserId));
			} catch (Exception e) {
				((HttpServletResponse)response).sendError(20011, "checkRedisSession error:[" + requestUrl + "]");
				return;
			}
		}
		else
		{
			LOGGER.error("No Header Value(UserId) [" + requestUrl + "]");
			((HttpServletResponse)response).sendError(20012, "No Header Value(UserId) [" + requestUrl + "]");
			return;
		}

		if (versionCheckResult != null && versionCheckResult.result == ConstantVal.VERSION_CHECK_NO_SESSION) {

			((HttpServletResponse) response).sendError(20016, "Reconnect due to session expiation.");
			return;
		}

		if(useEncryption == false)
		{
			cloneRequest = new XSSRequestWrapper(hsr, null);

			try
			{
				chain.doFilter(cloneRequest, responseWrapper);
				encryptBytes = responseWrapper.getDataStream();

				response.setContentLength(encryptBytes.length);
				response.getOutputStream().write(encryptBytes);
			}
			catch (WeException e) {
				LOGGER.error("error:[" + requestUrl + "]" + cloneRequest.getRequestBody());
				((HttpServletResponse) response).sendError(e.errorCode, cloneRequest.getRequestBody());
			} catch (Exception e) {
				LOGGER.error("error:[" + requestUrl + "]" + cloneRequest.getRequestBody());
				e.printStackTrace();
				((HttpServletResponse) response).sendError(20013, cloneRequest.getRequestBody());
			}

			return;
		}

		if (versionCheckResult != null) {
			encryption = versionCheckResult.encryption;
		}
		else
		{
			LOGGER.error("versionCheckResult is null for userId: " + clientUserId);
			((HttpServletResponse) response).sendError(20017, "versionCheckResult is null");
			return;
		}

		cloneRequest = new XSSRequestWrapper(hsr, encryption);

		if(cloneRequest.isDecodeError)
		{
			LOGGER.error("Encryption data error !! [" + requestUrl + "]");
			((HttpServletResponse) response).sendError(20015, cloneRequest.getRequestBody());
			return;
		}

		try
		{
			chain.doFilter(cloneRequest, responseWrapper);
			if(responseWrapper.getStatus() == HttpStatus.OK.value())
			{
				try
				{
					encryptBytes = AESUtil.Aes128Encode(responseWrapper.getDataStream(), encryption);
				}
				catch(Exception e) {
					encryptBytes = new byte[1];
				}

				response.setContentLength(encryptBytes.length);
				response.getOutputStream().write(encryptBytes);
			}
			else
			{
				encryptBytes = responseWrapper.getDataStream();
				response.setContentLength(encryptBytes.length);
				response.getOutputStream().write(encryptBytes);
			}

		} catch (WeException we) {
			LOGGER.error("error:[" + requestUrl + "]" + cloneRequest.getRequestBody());
			((HttpServletResponse) response).sendError(we.errorCode, cloneRequest.getRequestBody());
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("error:[" + requestUrl + "]" + cloneRequest.getRequestBody());
			((HttpServletResponse) response).sendError(20022, cloneRequest.getRequestBody());
		}
    }
}

class ResponseWrapper extends HttpServletResponseWrapper {
	ByteArrayOutputStream output;
	FilterServletOutputStream filterOutput;

	public ResponseWrapper(HttpServletResponse response) {
		super(response);
		output = new ByteArrayOutputStream();
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		if (filterOutput == null) {
			filterOutput = new FilterServletOutputStream(output);
		}
		return filterOutput;
	}

	public byte[] getDataStream() {
		return output.toByteArray();
	}
}

class FilterServletOutputStream extends ServletOutputStream {

	DataOutputStream output;

	public FilterServletOutputStream(OutputStream output) {
		this.output = new DataOutputStream(output);
	}

	@Override
	public void write(int arg0) throws IOException {
		output.write(arg0);
	}

	@Override
	public void write(byte[] arg0, int arg1, int arg2) throws IOException {
		output.write(arg0, arg1, arg2);
	}

	@Override
	public void write(byte[] arg0) throws IOException {
		output.write(arg0);
	}

	public boolean isReady() {
		return true;
	}

	public void setWriteListener(WriteListener writeListener) {
	}
}

class XSSRequestWrapper extends HttpServletRequestWrapper {

	public boolean isDecodeError = false;

	private byte[] rawData;

	private HttpServletRequest request;

	private ResettableServletInputStream servletStream;

	String secretKey;

	public XSSRequestWrapper(HttpServletRequest request, String secretKey) {
		super(request);
		this.secretKey = secretKey;
		this.request = request;

		this.servletStream = new ResettableServletInputStream();

		rawData = getBody(this.request);

		if (rawData == null) {
			isDecodeError = true;
		} else {
			servletStream.stream = new ByteArrayInputStream(rawData);
		}
	}

	public String getRequestBody() {
		return "request body:" + new String(rawData);
	}

	public void resetInputStream(byte[] newRawData) {
		servletStream.stream = new ByteArrayInputStream(newRawData);
	}

	public byte[] getBody(HttpServletRequest request) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte buffer[] = new byte[128];
		try {
			InputStream inputStream = request.getInputStream();

			while (true) {
				int readCount = inputStream.read(buffer, 0, 128);

				if (readCount < 0) {
					break;
				}
				baos.write(buffer, 0, readCount);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		} finally {
		}
		if (secretKey == null) {
			return baos.toByteArray();
		}
		return AESUtil.Aes128Decode(baos.toByteArray(), secretKey);
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		return servletStream;
	}

	@Override
	public BufferedReader getReader() throws IOException {
		return new BufferedReader(new InputStreamReader(servletStream));
	}

	private class ResettableServletInputStream extends ServletInputStream {
		private InputStream stream;

		@Override
		public int read() throws IOException {

			return stream.read();
		}

		public boolean isFinished() {
			return false;
		}

		public boolean isReady() {
			return false;
		}

		public void setReadListener(ReadListener readListener) {
		}
	}
}

class AESUtil
{
	public static byte[] Aes128Encode(byte[] ori, String key) throws Exception
    {
		SecretKeySpec newKey = new SecretKeySpec(key.getBytes(), "AES");
		byte[] ivBytes = new byte[16];
		System.arraycopy(key.getBytes(), 0, ivBytes, 0, 16);
		AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);
		return cipher.doFinal(ori);
	}

	public static byte[] Aes128Decode(byte[] ori, String key)
    {
		byte[] encrypted = null;
		try {
			SecretKeySpec newKey = new SecretKeySpec(key.getBytes(), "AES");
			byte[] ivBytes = new byte[16];
			System.arraycopy(key.getBytes(), 0, ivBytes, 0, 16);
			AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
			encrypted = cipher.doFinal(ori);
		} 
        catch (Exception e) {
            System.err.println("=================================================================================");
			System.err.println("encryption error:" + key);
			int dataSize = ori.length;
			System.err.println("encryption data size:" + dataSize);
			System.err.println("encryption data");

			String oriDataString = "";
			for (int i = 0; i < dataSize; i++) {
				oriDataString += ori[i] + ",";
			}

			System.err.println(oriDataString);
			System.err.println("=================================================================================");

			e.printStackTrace();
			return null;
		}
		return encrypted;
	}
}