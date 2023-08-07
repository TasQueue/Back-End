package com.example.taskqueue.exception.notfound;

import com.example.taskqueue.exception.base.BaseErrorCode;
import com.example.taskqueue.exception.notfound.config.ResourceNotFoundErrorCode;
import com.example.taskqueue.exception.notfound.config.ResourceNotFoundException;

public class CategoryNotFoundException extends ResourceNotFoundException {
    public CategoryNotFoundException() {
        super(ResourceNotFoundErrorCode.CATEGORY_NOT_FOUND);
    }
}
