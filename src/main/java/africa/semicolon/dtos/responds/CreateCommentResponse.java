package africa.semicolon.dtos.responds;

import lombok.Data;

@Data
public class CreateCommentResponse {
    private String postId;
    private String content;
    private String username;
}

