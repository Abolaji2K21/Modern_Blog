package africa.semicolon.dtos.requests;

import lombok.Data;

@Data
public class EditPostRequest {
    private String postId;
    private String username;
    private String title;
    private String content;
}
