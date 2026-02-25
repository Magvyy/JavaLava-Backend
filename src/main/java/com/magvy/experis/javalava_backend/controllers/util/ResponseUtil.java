package com.magvy.experis.javalava_backend.controllers.util;

import com.magvy.experis.javalava_backend.domain.entitites.Attachment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ResponseUtil {

    public static <T> ResponseEntity<T> wrapEntity(T entity) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<>(entity, headers, HttpStatus.OK);
    }

    public static <T> ResponseEntity<T> wrapEntity(T entity, HttpStatus status) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<>(entity, headers, status);
    }
}
