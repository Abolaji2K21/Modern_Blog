package africa.semicolon.dtos.responds;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse {

    boolean isSuccessful;
    Object registerUserResponse;

}
