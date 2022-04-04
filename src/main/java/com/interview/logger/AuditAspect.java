package com.interview.logger;

import lombok.SneakyThrows;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.time.OffsetDateTime;
import java.util.Arrays;

@Aspect
@Component
@PropertySource("classpath:application.properties")
public class AuditAspect {

    private final File auditFile;

    @SneakyThrows
    public AuditAspect() {
        auditFile = new File("audit.txt");
        if (!auditFile.exists()) {
            auditFile.createNewFile();
        }
    }

    @Before("@annotation(com.interview.logger.Audit)")
    public void startMethodServiceWithAnnotation(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        StringBuilder msg = new StringBuilder(OffsetDateTime.now().toString());
        msg.append(" Start method: ");
        msg.append(signature.getDeclaringTypeName());
        msg.append(":");
        msg.append(signature.getMethod().getName());
        if(joinPoint.getArgs().length > 0) {
            msg.append(", with param: ").append(Arrays.toString(joinPoint.getArgs()));
        }
        writeToAudit(msg.toString());
    }

    @AfterReturning(value = "@annotation(com.interview.logger.Audit)", returning = "returnValue")
    public void endMethodWithAnnotation(JoinPoint joinPoint, Object returnValue) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        StringBuilder msg = new StringBuilder(OffsetDateTime.now().toString());
        msg.append(" End method: ");
        msg.append(signature.getDeclaringTypeName());
        msg.append(":");
        msg.append(signature.getMethod().getName());
        if (returnValue != null) {
            msg.append(", return value: ");
            msg.append(returnValue);
        }
        writeToAudit(msg.toString());
    }

    @AfterThrowing(value = "@annotation(com.interview.logger.Audit)", throwing = "ex")
    public void throwMethodWithAnnotation(JoinPoint joinPoint, Exception ex) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        StringBuilder msg = new StringBuilder(OffsetDateTime.now().toString());
        msg.append(" Throw method: ");
        msg.append(signature.getDeclaringTypeName());
        msg.append(":");
        msg.append(signature.getMethod().getName());
        msg.append(ex.toString());
        writeToAudit(msg.toString());
    }

    @SneakyThrows
    private void writeToAudit(String msg) {
        BufferedWriter writer = new BufferedWriter(new FileWriter(auditFile, true));
        writer.write(msg + "\n");
        writer.close();
    }
}
