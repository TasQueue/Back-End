package com.example.taskqueue.exceptionhandler;

import com.example.taskqueue.exception.badinput.config.BadInputException;
import com.example.taskqueue.exception.notfound.config.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class  CommonExceptionHandler {

    /**
     * ResourceNotFoundException 타입 예외처리 ExceptionHandler
     * @param e ResourceNotFoundException
     * @return ErrorResponse
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> ResourceNotFoundExceptionHandler(ResourceNotFoundException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(e.getErrorCode().getCode())
                .message(e.getMessage())
                .errors(Map.of())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * BadInputException 타입 예외처리 ExceptionHandler
     * @param e BadInputException
     * @return ErrorResponse
     */
    @ExceptionHandler(BadInputException.class)
    public ResponseEntity<ErrorResponse> BadInputExceptionHandler(BadInputException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(e.getErrorCode().getCode())
                .message(e.getMessage())
                .errors(e.getBadInputMaps())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}
