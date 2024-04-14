package africa.semicolon.data.repositories;

import africa.semicolon.data.model.Comment;
import africa.semicolon.data.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {
    List<Comment> findByPostId(String postId);

    Optional<Comment> findByPostIdAndUserId(String postId, String userId);
}
