package africa.semicolon.data.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Document("Posts")
public class Post {
    @Id
    private String postId;
    private String title;
    private String content;
    private LocalDateTime dateTimeCreated;
    private String userId;
    private LocalDateTime dateTimeUpdated;
    @DBRef
    private List<Comment> comments = new ArrayList<>();
    @DBRef
    private List<View> views = new ArrayList<>();
}