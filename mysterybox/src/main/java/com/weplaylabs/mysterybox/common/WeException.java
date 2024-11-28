package com.weplaylabs.mysterybox.common;

import jakarta.servlet.ServletException;

public class WeException extends ServletException
{
    public static final long serialVersionUID = 1L;
    public int errorCode;

    public WeException(int errorCode) {
        this.errorCode = errorCode;
    }
}
