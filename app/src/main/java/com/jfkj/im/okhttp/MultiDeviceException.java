package com.jfkj.im.okhttp;

import java.io.IOException;

public class MultiDeviceException extends IOException {
    public MultiDeviceException(String message) {
        super(message);
    }
}
