package africa.semicolon.dtos.requests;

import lombok.Data;

@Data
public class CreateCommentRequest {
    private String userId;
    private String comment;
    private String postId;

}
