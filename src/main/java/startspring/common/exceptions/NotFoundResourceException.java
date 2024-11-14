package startspring.common.exceptions;

import lombok.Getter;

@Getter
public class NotFoundResourceException extends RuntimeException{

    public NotFoundResourceException(String message) {
        super(message);
    }
}
