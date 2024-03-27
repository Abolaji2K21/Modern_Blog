package africa.semicolon.data.repositories;

import africa.semicolon.data.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class UserRepositoryTest {

    @Autowired
    private UserRepository myRepository;

    @Test
    void testThatUserCanSave(){
        User user = new User();
        myRepository.save(user);
        assertThat(myRepository.count(), is(1L));

    }

}