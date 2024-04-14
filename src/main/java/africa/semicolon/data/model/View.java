package africa.semicolon.data.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Document("Views")
public class View {
    @Id
    private String viewId;
    private String postId;
    private Set<String> userId = new HashSet<>();
    private LocalDateTime timeOfView = LocalDateTime.now();
}