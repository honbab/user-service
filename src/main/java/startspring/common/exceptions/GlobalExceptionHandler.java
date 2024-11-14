package startspring.common.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> badRequestException(BadRequestException e) {
        log.error("BadRequestException", e);
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(BadResponseException.class)
    public ResponseEntity<String> badResponseException(BadResponseException e) {
        log.error("BadResponseException", e);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e.getMessage());
    }

    @ExceptionHandler(TokenValidationException.class)
    public ResponseEntity<String> tokenValidationException(TokenValidationException e) {
        log.debug("TokenValidationException", e);
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(NotFoundResourceException.class)
    public ResponseEntity<String> notFoundResourceException(NotFoundResourceException e) {
        log.debug("NotFoundResourceException", e);
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
