package africa.semicolon.dtos.responds;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.validation.ObjectError;

@Data
@AllArgsConstructor
public class ApiResponse {

    boolean isSuccessful;
    Object data;
//    Object createUserResponse;
//    Object createCommentResponse;


}
