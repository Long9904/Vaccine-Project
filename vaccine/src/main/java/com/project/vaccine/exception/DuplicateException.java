package com.project.vaccine.exception;


import lombok.Getter;

import java.util.List;

@Getter
public class DuplicateException extends RuntimeException {
    public DuplicateException(String message) {
        super(message);
    }
}
