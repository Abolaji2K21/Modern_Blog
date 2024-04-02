package africa.semicolon.dtos.responds;

import lombok.Data;

@Data
public class EditCommentResponse {
    private String commentId;
    private String content;
    private String username;
    private String postId;

}
