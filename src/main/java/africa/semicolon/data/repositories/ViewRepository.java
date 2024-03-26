package africa.semicolon.data.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViewRepository extends MongoRepository<ViewRepository, String> {
}
