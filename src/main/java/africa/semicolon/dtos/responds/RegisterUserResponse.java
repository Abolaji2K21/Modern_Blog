package africa.semicolon.dtos.responds;

import lombok.Data;

@Data
public class RegisterUserResponse {
    private String userId;
    private String username;
    private String dateRegistered;

}
