package startspring.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record LoginUserRequestDto(@NotEmpty String id, @NotEmpty String password) {}