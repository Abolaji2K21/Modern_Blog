package africa.semicolon.dtos.requests;

import lombok.Data;

@Data
public class LoginUserRequest {
    public String username;
    public String password;
}
