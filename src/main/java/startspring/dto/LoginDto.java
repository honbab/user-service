package startspring.dto;

import jakarta.validation.constraints.NotEmpty;

public record LoginDto(
        @NotEmpty String id,
        @NotEmpty String password
) {
}
