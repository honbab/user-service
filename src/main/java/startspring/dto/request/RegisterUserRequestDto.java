package startspring.dto.request;

import jakarta.validation.constraints.NotNull;

public record RegisterUserRequestDto(
        @NotNull String userId,
        @NotNull String password,
        @NotNull String name,
        @NotNull String email,
        @NotNull String phoneNumber
        ) {
}
