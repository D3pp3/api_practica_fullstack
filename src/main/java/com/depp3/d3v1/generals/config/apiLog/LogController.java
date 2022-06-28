package com.depp3.d3v1.generals.config.apiLog;

import com.depp3.d3v1.generals.exceptions.GenericException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component @Aspect @Slf4j
public class LogController {

    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void allControllers() {}

    @Before("allControllers()")
    public void apiRequestLog(JoinPoint jp) {
        log.debug("----------------------------- O -----------------------------");
        log.debug(jp.getSignature().getName() + "()" );
        log.debug("----------------------------- O -----------------------------");
    }

    @AfterThrowing(pointcut = "allControllers()", throwing = "result")
    public void apiResponseThrow(JoinPoint jp, GenericException result) {
        log.error(result.getMessage() + " | " +
                result.getShowableMessage() + " | " +
                result.getCode() + " | " +
                result.getStatus());
    }

    @AfterReturning(pointcut = "allControllers()",  returning = "result")
    public void apiResponseLog(JoinPoint jp, ResponseEntity result) {
        log.debug("----------------------------- O -----------------------------");
        log.debug(result.getBody().toString());
        log.debug("----------------------------- O -----------------------------");
    }
}
