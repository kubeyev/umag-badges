package com.badgers.umag.core.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Map;

public class Responseable {
   public <T> ResponseEntity<T> response(T t, HttpStatus status) {
        return new ResponseEntity<T>(t, status);
    }

    public <T> ResponseEntity<T> ok(T t) {
        return response(t, HttpStatus.OK);
    }

    public ResponseEntity<Map> okm(String... messages) {
        return ok(Collections.singletonMap("messages", messages));
    }
}
