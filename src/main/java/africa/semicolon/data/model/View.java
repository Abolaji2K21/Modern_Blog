package africa.semicolon.data.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document("Views")
public class View {
    @Id
    private String id;

    private List<String> userId;
    private LocalDateTime timeOfView = LocalDateTime.now();
}