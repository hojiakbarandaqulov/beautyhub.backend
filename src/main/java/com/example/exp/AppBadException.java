package com.example.exp;

import java.util.Locale;

public class AppBadException extends RuntimeException {

    public AppBadException(String message) {
        super(message);
    }

    public AppBadException(String message, Object o, Locale locale) {

    }
}
