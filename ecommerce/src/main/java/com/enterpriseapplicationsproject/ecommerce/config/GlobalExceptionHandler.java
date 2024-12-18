package com.enterpriseapplicationsproject.ecommerce.config;


import com.enterpriseapplicationsproject.ecommerce.data.entities.ShoppingCart;
import com.enterpriseapplicationsproject.ecommerce.data.entities.Wishlist;
import com.enterpriseapplicationsproject.ecommerce.dto.ServiceError;
import com.enterpriseapplicationsproject.ecommerce.exception.*;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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

    @ExceptionHandler(WishlistNotJoinedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ServiceError onResourceNotFoundException(WebRequest req, WishlistNotJoinedException ex){
        return errorResponse(HttpStatus.valueOf(HttpStatus.CONFLICT.value()), req, ex.getMessage());
    }

    @ExceptionHandler(WishlistNotCreatedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ServiceError onResourceNotFoundException(WebRequest req, WishlistNotCreatedException ex){
        return errorResponse(HttpStatus.valueOf(HttpStatus.UNAUTHORIZED.value()), req, ex.getMessage());
    }

    @ExceptionHandler(WishlistCantBeDeletedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ServiceError onResourceNotFoundException(WebRequest req, WishlistCantBeDeletedException ex){
        return errorResponse(HttpStatus.valueOf(HttpStatus.CONFLICT.value()), req, ex.getMessage());
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

    @ExceptionHandler(InvalidCredentialException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ServiceError onInvalidCredentialException(WebRequest req, InvalidCredentialException ex){
        return errorResponse(HttpStatus.valueOf(HttpStatus.UNAUTHORIZED.value()), req, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ServiceError onMethodArgumentNotValid(WebRequest req, MethodArgumentNotValidException ex){
        String message = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        return errorResponse(HttpStatus.valueOf(HttpStatus.BAD_REQUEST.value()), req, message);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ServiceError onConstraintViolationException(WebRequest req, ConstraintViolationException ex){
        return errorResponse(HttpStatus.valueOf(HttpStatus.BAD_REQUEST.value()), req, ex.getMessage());
    }


    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ServiceError onAuthenticationException(WebRequest req, AuthenticationException ex){
        return errorResponse(HttpStatus.valueOf(HttpStatus.UNAUTHORIZED.value()), req, ex.getMessage());
    }

    @ExceptionHandler(JwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ServiceError onJwtException(WebRequest req, JwtException ex){
        return errorResponse(HttpStatus.valueOf(HttpStatus.UNAUTHORIZED.value()), req, ex.getMessage());
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ServiceError onUnauthorizedAccessException(WebRequest req, UnauthorizedAccessException ex){
        return errorResponse(HttpStatus.valueOf(HttpStatus.FORBIDDEN.value()), req, ex.getMessage());
    }

    @ExceptionHandler(OrderCreationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ServiceError onOrderCreationException(WebRequest req, OrderCreationException ex){
        return errorResponse(HttpStatus.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), req, ex.getMessage());
    }

    @ExceptionHandler(UserRegistrationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ServiceError onUserRegistrationException(WebRequest req, UserRegistrationException ex){
        return errorResponse(HttpStatus.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), req, ex.getMessage());
    }

    @ExceptionHandler(TokenNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ServiceError onTokenNotFoundException(WebRequest req, TokenNotFoundException ex){
        return errorResponse(HttpStatus.valueOf(HttpStatus.NOT_FOUND.value()), req, ex.getMessage());
    }

    @ExceptionHandler(RevokingTokenErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ServiceError onRevokingTokenErrorException(WebRequest req, RevokingTokenErrorException ex){
        return errorResponse(HttpStatus.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), req, ex.getMessage());
    }

    @ExceptionHandler(IsTokenRevokedException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ServiceError onResourceNotFoundException(WebRequest req, IsTokenRevokedException ex){
        return errorResponse(HttpStatus.valueOf(HttpStatus.NOT_FOUND.value()), req, ex.getMessage());
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public String onResourceNotFoundException(WebRequest req, NullPointerException ex){
        log.error("Exception handler :::: {}", ex);
        return "NULLLLLLLLLLLLLLLLLLLLLLLLLLLLL POINTER!!!";
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ServiceError onRuntimeException(WebRequest req, RuntimeException ex) {
        log.error("Internal server error: {}", ex.getMessage(), ex);
        return errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, req, "Unexpected error occurred");
    }

    @ExceptionHandler(LoginException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ServiceError onLoginException(WebRequest req, LoginException ex) {
        return errorResponse(HttpStatus.UNAUTHORIZED, req, ex.getMessage());
    }

    @ExceptionHandler(InvalidCardNumberException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ServiceError onInvalidCardNumberException(WebRequest req, InvalidCardNumberException ex) {
        return errorResponse(HttpStatus.BAD_REQUEST, req, ex.getMessage());
    }


    private ServiceError errorResponse (HttpStatus httpStatus, WebRequest req, String message) {
        HttpServletRequest httpreq = (HttpServletRequest) req.resolveReference("request");
        if (httpreq == null) {
            log.error("HttpServletRequest is null");
            return new ServiceError(httpStatus.value(), new Date(), "unknown", message);
        }

        final ServiceError output = new ServiceError(httpStatus.value() ,  new Date(), httpreq.getRequestURI(), message);
        log.error("Exception handler :::: {}", output.toString());
        return output;

    }

}