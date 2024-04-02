package africa.semicolon.dtos.requests;

import lombok.Data;

@Data
public class CreateCommentRequest {
    private String username;
    private String content;
    private String postId;

}
