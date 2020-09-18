package pl.carparts.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Slf4j
@Component
public class LoggerAspect {

    @AfterReturning(value = "execution(* pl.carparts.service..*(..))", returning = "returnValue")
    public void logMethodInvocation(JoinPoint point, Object returnValue) {
        StringBuilder logBuilder = new StringBuilder();
        logBuilder
            .append("Method: ")
            .append(point.getSignature().getName())
            .append(" with params: | ");
        Arrays.stream(point.getArgs()).forEach(el -> {
            logBuilder.append(el);
            logBuilder.append(" | ");
        });

        logBuilder
            .append("  : returnValue: ")
            .append(returnValue.toString());
        log.info(logBuilder.toString());
    }

    @AfterThrowing(value = "execution(* pl.carparts.service..*(..))", throwing = "exception")
    public void logException(JoinPoint point, Throwable exception) {
        StringBuilder logBuilder = new StringBuilder();
        logBuilder
                .append("Method: ")
                .append(point.getSignature().getName())
                .append(" with params: | ");
        Arrays.stream(point.getArgs()).forEach(el -> {
            logBuilder.append(el);
            logBuilder.append(" | ");
        });

        logBuilder
                .append("  : thrown exception: ")
                .append(exception.getMessage());
        log.info(logBuilder.toString());
    }
}
