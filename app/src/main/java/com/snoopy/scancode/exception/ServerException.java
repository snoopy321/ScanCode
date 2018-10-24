package com.snoopy.scancode.exception;

import java.io.IOException;

/**
 * @Author: snoopy
 * @Date: 2018/10/22 15:16
 */

public class ServerException extends IOException {
    public ServerException(String message) {
        super(message);
    }
}
