package com.yigitcanyontem.forum.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class LoginException extends RuntimeException{
    public LoginException(String msg) {
        super(msg);
    }

}
