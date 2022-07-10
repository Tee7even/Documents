package com.tee7even.documents.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ParentNodeNotFoundException extends RuntimeException {

    public ParentNodeNotFoundException(String message) {
        super(message);
    }

    public ParentNodeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParentNodeNotFoundException(Throwable cause) {
        super(cause);
    }

    public ParentNodeNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
