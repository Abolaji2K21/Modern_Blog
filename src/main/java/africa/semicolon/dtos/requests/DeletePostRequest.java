package africa.semicolon.dtos.requests;

import lombok.Data;

@Data
public class DeletePostRequest {
    private String postId;
    private String userId;
}