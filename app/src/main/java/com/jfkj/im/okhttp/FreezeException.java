package com.jfkj.im.okhttp;

import java.io.IOException;

public class FreezeException extends IOException {
    public FreezeException() {
    }

    public FreezeException(String message) {
        super(message);
    }

    public FreezeException(String message, Throwable cause) {
        super(message, cause);
    }

    public FreezeException(Throwable cause) {
        super(cause);
    }
}
