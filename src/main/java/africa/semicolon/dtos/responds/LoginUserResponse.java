package africa.semicolon.dtos.responds;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class LoginUserResponse {
    private final String userId;
    private final String username;
    private boolean loggedIn;



}
