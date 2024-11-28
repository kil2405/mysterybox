package com.weplaylabs.mysterybox.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class BaseSessionClass
{
    @Autowired(required = true)
    private HttpServletRequest request;

    public int getUserId() {
        try {
            return Integer.parseInt(request.getHeader("userId"));
        } catch(Exception e) {
            return -1;
        }
    }
}
