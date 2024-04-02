package africa.semicolon.dtos.requests;

import lombok.Data;

@Data
public class EditCommentRequest {
    private String commentId;
    private String content;
    private String username;
    private String postId;
}
