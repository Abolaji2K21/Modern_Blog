package africa.semicolon.dtos.responds;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LogoutUserResponse {
    private String userId;
    private String username;
    private boolean loggedIn;


}
