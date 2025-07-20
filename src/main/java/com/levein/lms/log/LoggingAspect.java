package com.levein.lms.log;

import com.levein.lms.dto.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    // Match all methods in classes annotated with @RestController
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controllerMethods() {
    }

    // Match all methods in classes annotated with @Service
    @Pointcut("within(@org.springframework.stereotype.Service *)")
    public void serviceMethods() {
    }

    // Log before controller or service methods
    @Before("controllerMethods() || serviceMethods()")
    public void logBefore(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = signature.getDeclaringTypeName();
        String methodName = signature.getName();

        log.info("-> Invoked: {}.{}()", className, methodName);
    }


    @AfterReturning(pointcut = "controllerMethods() || serviceMethods()", returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = signature.getDeclaringTypeName();
        String methodName = signature.getName();

        String simpleResult = extractResponseSummary(result);

        log.info("-> {}.{}() executed successfully. {}", className, methodName, simpleResult);
    }

    private String extractResponseSummary(Object result) {
        if (result instanceof ResponseEntity<?> response) {
            HttpStatus status = HttpStatus.valueOf(response.getStatusCode().value());
            return "HTTP Status: " + status.value() + " " + status.getReasonPhrase();
        }

        // If it's a custom response wrapper like ApiResponse
        if (result instanceof ApiResponse<?> apiResponse) {
            return "Status: " + apiResponse.getStatus() + ", Message: " + apiResponse.getMessage();
        }

        // Generic fallback
        return "Response type: " + (result != null ? result.getClass().getSimpleName() : "null");
    }


}
