package africa.semicolon.dtos.requests;

import lombok.Data;

@Data
public class RegisterUserRequest {
    private String firstname;
    private String lastname;
    private String username;
    private String password;


}
