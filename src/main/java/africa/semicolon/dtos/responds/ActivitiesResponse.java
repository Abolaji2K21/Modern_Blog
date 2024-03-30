package africa.semicolon.dtos.responds;

import africa.semicolon.data.model.Comment;
import africa.semicolon.data.model.Post;
import africa.semicolon.data.model.View;
import lombok.Data;

import java.util.List;

@Data
public class ActivitiesResponse {
    private Post post;
    private View view;
    private List<Comment> comments;
}
