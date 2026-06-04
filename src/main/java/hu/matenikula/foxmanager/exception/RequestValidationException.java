package hu.matenikula.foxmanager.exception;

import javax.validation.ConstraintViolation;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RequestValidationException extends RuntimeException {

    private final List<String> errors;

    public RequestValidationException(Set<? extends ConstraintViolation<?>> violations) {
        this.errors = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
    }

    public List<String> getErrors() {
        return errors;
    }
}