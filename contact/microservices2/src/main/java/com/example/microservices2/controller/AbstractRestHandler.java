package com.example.microservices2.controller;

import com.example.microservices2.exception.*;
import com.example.microservices2.exception.model.RestErrorInfo;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
@RestControllerAdvice
public class AbstractRestHandler implements ApplicationEventPublisherAware {

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    protected ApplicationEventPublisher eventPublisher;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DataFormatException.class)
    public
    @ResponseBody
    RestErrorInfo handleDataStoreException(DataFormatException ex, WebRequest request, HttpServletResponse response) {
        log.error("Converting Data Store exception to RestResponse: " + ex.getMessage());
        return new RestErrorInfo(ex, "You messed up.");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidObjectDetailsException.class)
    public
    @ResponseBody
    RestErrorInfo handleInvalidPayloadException(InvalidObjectDetailsException ex, WebRequest request, HttpServletResponse response) {
        log.error("InvalidObjectDetailsException handler:" + ex.getMessage());
        return new RestErrorInfo(ex, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public
    @ResponseBody
    RestErrorInfo handleMethodArgumentNotValidException(BindException ex, WebRequest request, HttpServletResponse response) {
        log.error("MethodArgumentNotValidException handler:" + ex.getMessage());
        final StringBuilder errMessage = new StringBuilder();
        if(ex.getBindingResult() != null && !CollectionUtils.isEmpty(ex.getBindingResult().getAllErrors()))
            ex.getBindingResult().getAllErrors().forEach(error -> errMessage.append(error.getDefaultMessage()).append(","));

        return new RestErrorInfo(ex, StringUtils.isNotEmpty(errMessage.toString()) ? errMessage.toString() : ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidPaginationRequestException.class)
    public
    @ResponseBody
    RestErrorInfo handleInvalidPaginationException(InvalidPaginationRequestException ex, WebRequest request, HttpServletResponse response) {
        log.error("InvalidPaginationRequest handler:" + ex.getMessage());
        return new RestErrorInfo(ex, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(DBOperationException.class)
    public
    @ResponseBody
    RestErrorInfo handleDBOperationException(DBOperationException ex, WebRequest request, HttpServletResponse response) {
        log.error("DBOperationException handler:" + ex.getMessage());
        return new RestErrorInfo(ex, ex.getMessage());
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.eventPublisher = applicationEventPublisher;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public
    @ResponseBody
    RestErrorInfo checkResourceFound(ResourceNotFoundException ex, WebRequest request, HttpServletResponse response) {
        log.error("ResourceNotFoundException: " + ex.getMessage());
        return new RestErrorInfo(ex, ex.getMessage());

    }
}
