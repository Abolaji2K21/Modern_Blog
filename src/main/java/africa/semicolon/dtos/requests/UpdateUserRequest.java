package africa.semicolon.dtos.requests;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private String userId;
    private String firstname;
    private String lastname;
    private String username;

}