package africa.semicolon.dtos.responds;

import lombok.Data;

@Data
public class RegisterUserResponse {
    private String id;
    private String username;
    private String dateRegistered;

}
