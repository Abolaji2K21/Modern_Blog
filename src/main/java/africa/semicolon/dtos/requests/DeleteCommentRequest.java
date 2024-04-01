package africa.semicolon.dtos.requests;

import lombok.Data;

@Data
public class DeleteCommentRequest {
    private String commentId;
    private String username;
}
