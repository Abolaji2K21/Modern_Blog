package africa.semicolon.data.repositories;

import africa.semicolon.data.model.View;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViewRepository extends MongoRepository<View, String> {
    View findByPostId(String postId);
}
