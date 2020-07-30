package com.jfkj.im.okhttp;

import java.io.IOException;

public class OtherException extends IOException {
    public OtherException() {
    }

    public OtherException(String message) {
        super(message);
    }

    public OtherException(String message, Throwable cause) {
        super(message, cause);
    }

    public OtherException(Throwable cause) {
        super(cause);
    }
}
