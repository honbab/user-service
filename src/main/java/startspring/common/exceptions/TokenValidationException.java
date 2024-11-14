package startspring.common.exceptions;

import lombok.Getter;

@Getter
public class TokenValidationException extends RuntimeException{

    public TokenValidationException(String message) {
        super(message);
    }
}
