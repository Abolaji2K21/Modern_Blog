package africa.semicolon.data.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document("Comments")
public class Comment {
    @Id
    private String commentId;
    private String userId;
    private String comment;
    private String postId;
    private LocalDateTime DateTimeCreated = LocalDateTime.now();

}