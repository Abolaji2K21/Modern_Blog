package africa.semicolon.dtos.responds;

import africa.semicolon.data.model.Post;
import lombok.Data;
@Data
public class CreatePostResponse{
    private String postId;
    private String title;
    private String content;
    private String dateCreated;
}