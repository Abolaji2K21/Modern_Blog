package africa.semicolon.data.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("Comments")
public class Comment {
    @Id
    private String id;
    private String userId;
    @DBRef
    private Post post;
    private String comment;
    private String postId;
}