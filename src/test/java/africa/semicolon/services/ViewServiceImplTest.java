package africa.semicolon.services;

import africa.semicolon.data.model.User;
import africa.semicolon.data.model.View;
import africa.semicolon.data.repositories.PostRepository;
import africa.semicolon.data.repositories.UserRepository;
import africa.semicolon.data.repositories.ViewRepository;
import africa.semicolon.dtos.requests.CreatePostRequest;
import africa.semicolon.dtos.requests.LoginUserRequest;
import africa.semicolon.dtos.requests.RegisterUserRequest;
import africa.semicolon.dtos.responds.CreatePostResponse;
import africa.semicolon.dtos.responds.LoginUserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ViewServiceImplTest {
    @Autowired
    private ViewRepository viewRepository;
    @Autowired
    private ViewService viewService;


    @Test
    void testThatMyViewCountsIncrease() {

        View view = viewService.addViewCount("postId", "userIdOne");
        Set<String> userIds = view.getUserIds();
        assertTrue(userIds.contains("userIdOne"));
        assertEquals(1, viewRepository.count());
    }

}