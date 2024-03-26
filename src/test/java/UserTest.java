import africa.semicolon.data.model.User;
import africa.semicolon.data.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest
public class UserTest {
    private UserRepository repository;
    @Test
    public void testThatRepositoryCanSave(){
        User commenter = new User();
        commenter.setId("commenter");
        repository.save(commenter);
        assertEquals(repository.count(),1);

    }
}
