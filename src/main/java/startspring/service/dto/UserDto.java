package startspring.service.dto;

public record UserDto (   String userId,
        String password,
        String name,
        String email,
        String phoneNumber
) {

}
