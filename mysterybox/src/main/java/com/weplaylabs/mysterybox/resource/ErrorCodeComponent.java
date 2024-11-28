package com.weplaylabs.mysterybox.resource;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.weplaylabs.mysterybox.model.resource.ErrorCodeResource;
import com.weplaylabs.mysterybox.repository.resource.ErrorCodeRepository;

@Component
public class ErrorCodeComponent
{
    @Autowired
    private ErrorCodeRepository errorCodeRepository;

    private final HashMap<Integer, ErrorCodeResource> resource = new HashMap<>();
    
    public void LoadResource() throws Exception
    {
        resource.clear();

        List<ErrorCodeResource> errorCodeResources = errorCodeRepository.findAll();
        if(errorCodeResources == null || errorCodeResources.isEmpty())
        {
            System.out.println("ErrorCodeResource is empty");
            System.exit(0);
        }

        for(ErrorCodeResource errorCodeResource : errorCodeResources)
        {
            if(resource.containsKey(errorCodeResource.getErrorCode()))
            {
                System.out.println("ErrorCodeResource is duplicated | " + errorCodeResource.getErrorCode());
                System.exit(0);
            }

            resource.put(errorCodeResource.getErrorCode(), errorCodeResource);
        }

        System.out.println("ErrorCodeResource Loading Complete");
    }

    public ErrorCodeResource get(int errorCode)
    {
        return resource.get(errorCode);
    }
}
