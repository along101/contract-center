package com.along101.contract.aop;

import com.along101.contract.common.exception.ContractException;
import com.along101.contract.common.exception.ContractException;
import com.along101.contract.controller.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Global Exception handler
 */
@Aspect
@Component
@Slf4j
@Order(5)
public class ExceptionAspect {

    @Around("com.along101.contract.aop.ResourcePointCuts.webController()")
    public Object handleException(ProceedingJoinPoint apiMethod) {

        //Transaction transaction = Cat.newTransaction("ExceptionAspect", "ExceptionAspect");

        try {
            return apiMethod.proceed();
        } catch (ContractException e) {
            log.error(e.getMessage(), e);

            //transaction.setStatus(e);
            return Response.error(e);
        } catch (Throwable throwable) {
            log.error(throwable.getMessage(), throwable);

            //transaction.setStatus(throwable);

            return Response.error(throwable);
        } finally {
            //transaction.complete();
        }
    }
}
