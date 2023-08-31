package com.yigitcanyontem.forum.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class SearchNotFoundException extends RuntimeException{
    public SearchNotFoundException(String msg) {
        super(msg);
    }
}
