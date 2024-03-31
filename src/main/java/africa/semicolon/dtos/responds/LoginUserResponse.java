package africa.semicolon.dtos.responds;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
public class LoginUserResponse {
    private final String id;
    private final String username;


}
