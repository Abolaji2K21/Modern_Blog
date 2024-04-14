package africa.semicolon.dtos.requests;

import lombok.Data;

@Data
public class EditPostRequest {
    private String postId;
    private String userId;
    private String title;
    private String content;
}
