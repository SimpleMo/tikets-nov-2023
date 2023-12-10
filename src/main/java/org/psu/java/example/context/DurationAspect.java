package org.psu.java.example.context;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeanUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.StringJoiner;

/**
 * Аспект для опеределения продолжительности выполнения операций
 */
@Slf4j
@Aspect
@Component
public class DurationAspect {

    @SneakyThrows
    @Around("@annotation(org.springframework.web.bind.annotation.PostMapping) || @annotation(org.springframework.web.bind.annotation.GetMapping)")
    public Object around(ProceedingJoinPoint pjp) {
        var start = LocalTime.now();

        var result = pjp.proceed();

        var end = LocalTime.now();
        var duration = Duration.between(start, end).toMillis();

        var signature = (MethodSignature) pjp.getSignature();
        var controllerClass = signature.getMethod().getDeclaringClass();

        var annotation = AnnotationUtils.findAnnotation(signature.getMethod(), RequestMapping.class);

        var controllerPath = new StringJoiner(", ");
        Arrays.stream(AnnotationUtils.findAnnotation(controllerClass, RequestMapping.class).path()).forEach(controllerPath::add);

        var method = new StringJoiner(", ");
        Arrays.stream(annotation.method()).map(Enum::name).forEach(method::add);

        var path = new StringJoiner(", ");
        Arrays
                .stream(
                        Optional
                                .ofNullable(AnnotationUtils.findAnnotation(signature.getMethod(), GetMapping.class))
                                .map(GetMapping::path)
                                .orElseGet(() -> AnnotationUtils.findAnnotation(signature.getMethod(), PostMapping.class).path()))
                .forEach(path::add);

        log.info(String.format("Запрос %s к %s%s обрабатывался %d мс", method, controllerPath, path, duration));
        return result;
    }
}
