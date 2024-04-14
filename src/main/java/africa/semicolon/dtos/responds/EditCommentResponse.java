package africa.semicolon.dtos.responds;

import lombok.Data;

@Data
public class EditCommentResponse {
    private String commentId;
    private String comment;
    private String userId;
    private String postId;

}
