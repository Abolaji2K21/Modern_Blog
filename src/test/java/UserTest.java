import africa.semicolon.data.model.User;
import africa.semicolon.data.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class UserTest {

    @Autowired
    private UserRepository repository;

    @Test
    public void testThatRepositoryCanSave() {
        User user = new User();
        user.setId("commenter");

        repository.save(user);

        assertEquals(1, repository.count());
    }
}
