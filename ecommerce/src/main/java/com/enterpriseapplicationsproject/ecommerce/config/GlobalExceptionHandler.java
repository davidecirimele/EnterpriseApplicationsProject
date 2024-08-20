package com.enterpriseapplicationsproject.ecommerce.config;


import com.enterpriseapplicationsproject.ecommerce.data.entities.ShoppingCart;
import com.enterpriseapplicationsproject.ecommerce.dto.ServiceError;
import com.enterpriseapplicationsproject.ecommerce.exception.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ServiceError onResourceNotFoundException(WebRequest req, ProductNotFoundException ex){
        return errorResponse(HttpStatus.valueOf(HttpStatus.NOT_FOUND.value()), req, ex.getMessage());
    }

    @ExceptionHandler(OrderNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ServiceError onResourceNotFoundException(WebRequest req, OrderNotFoundException ex){
        return errorResponse(HttpStatus.valueOf(HttpStatus.BAD_REQUEST.value()), req, ex.getMessage());
    }

    @ExceptionHandler(OutOfStockException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ServiceError onResourceNotFoundException(WebRequest req, OutOfStockException ex){
        return errorResponse(HttpStatus.valueOf(HttpStatus.CONFLICT.value()), req, ex.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ServiceError onResourceNotFoundException(WebRequest req, UserNotFoundException ex){
        return errorResponse(HttpStatus.valueOf(HttpStatus.NOT_FOUND.value()), req, ex.getMessage());
    }

    @ExceptionHandler(ShoppingCartNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ServiceError onResourceNotFoundException(WebRequest req, ShoppingCartNotFoundException ex){
        return errorResponse(HttpStatus.valueOf(HttpStatus.NOT_FOUND.value()), req, ex.getMessage());
    }

    @ExceptionHandler(AddressNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ServiceError onResourceNotFoundException(WebRequest req, AddressNotFoundException ex){
        return errorResponse(HttpStatus.valueOf(HttpStatus.NOT_FOUND.value()), req, ex.getMessage());
    }

    @ExceptionHandler(EncryptionErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ServiceError onResourceNotFoundException(WebRequest req, EncryptionErrorException ex){
        return errorResponse(HttpStatus.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), req, ex.getMessage());
    }

    @ExceptionHandler(WishlistNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ServiceError onResourceNotFoundException(WebRequest req, WishlistNotFoundException ex){
        return errorResponse(HttpStatus.valueOf(HttpStatus.NOT_FOUND.value()), req, ex.getMessage());
    }

    @ExceptionHandler(WishlistItemNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ServiceError onResourceNotFoundException(WebRequest req, WishlistItemNotFoundException ex){
        return errorResponse(HttpStatus.valueOf(HttpStatus.NOT_FOUND.value()), req, ex.getMessage());
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ServiceError onResourceNotFoundException(WebRequest req, UserAlreadyExistsException ex){
        return errorResponse(HttpStatus.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), req, ex.getMessage());
    }

    @ExceptionHandler(InvalidJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ServiceError onInvalidJwtException(WebRequest req, InvalidJwtException ex){
        return errorResponse(HttpStatus.valueOf(HttpStatus.UNAUTHORIZED.value()), req, ex.getMessage());
    }




    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public String onResourceNotFoundException(WebRequest req, NullPointerException ex){
        log.error("Exception handler :::: {}", ex);
        return "NULLLLLLLLLLLLLLLLLLLLLLLLLLLLL POINTER!!!";
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ServiceError onMethodArgumentNotValid(HttpStatus httpStatus, WebRequest req, MethodArgumentNotValidException ex){

        String message = ex.getBindingResult().getFieldErrors().stream()
                                            .map(viol -> viol.getField().concat(" : ")
                                                .concat(viol.getDefaultMessage()))
                                            .collect(Collectors.joining(" , "));
        return errorResponse(httpStatus, req, message);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ServiceError defaultErrorHandler(HttpStatus httpStatus, WebRequest req ,Exception ex){
        return errorResponse(httpStatus, req, ex.getMessage());
    }


    private ServiceError errorResponse (HttpStatus httpStatus, WebRequest req, String message) {
        HttpServletRequest httpreq = (HttpServletRequest) req.resolveReference("request");
        final ServiceError output = new ServiceError(httpStatus.value() ,  new Date(), httpreq.getRequestURI(), message);
        log.error("Exception handler :::: {}", output.toString());
        return output;

    }
}