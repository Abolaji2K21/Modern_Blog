package africa.semicolon.services;

import africa.semicolon.BlogException.BigBlogException;
import africa.semicolon.data.model.Post;
import africa.semicolon.data.model.User;
import africa.semicolon.data.repositories.PostRepository;
import africa.semicolon.data.repositories.UserRepository;
import africa.semicolon.data.repositories.ViewRepository;
import africa.semicolon.dtos.requests.*;
import africa.semicolon.dtos.responds.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostServiceImplTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostService postService;
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ViewRepository viewRepository;
    private RegisterUserResponse response;

    private RegisterUserResponse anotherResponse;


    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
        postRepository.deleteAll();
        viewRepository.deleteAll();


        RegisterUserRequest registerRequest = new RegisterUserRequest();
        registerRequest.setFirstname("PenIs");
        registerRequest.setLastname("Up");
        registerRequest.setUsername("PenIsUp");
        registerRequest.setPassword("Holes");
        response = userService.register(registerRequest);

        LoginUserRequest loginRequest = new LoginUserRequest();
        loginRequest.setUsername("PenIsUp");
        loginRequest.setPassword("Holes");
        LoginUserResponse loginResponse = userService.login(loginRequest);
        assertThat(loginResponse.getUsername(), is("penisup"));


        registerRequest.setFirstname("PenIs");
        registerRequest.setLastname("Up");
        registerRequest.setUsername("PenIsDown");
        registerRequest.setPassword("Holes");
        anotherResponse = userService.register(registerRequest);

        loginRequest = new LoginUserRequest();
        loginRequest.setUsername("PenIsDown");
        loginRequest.setPassword("Holes");
        loginResponse = userService.login(loginRequest);
        assertThat(loginResponse.getUsername(), is("penisdown"));
    }

    @Test
    void testThatAfterSetupUserCanCreatePostWithEmptyContent() {
        assertTrue(userRepository.existsByUsername("penisup"));

        CreatePostRequest createPostRequest = new CreatePostRequest();
        createPostRequest.setUserId(response.getUserId());
        createPostRequest.setTitle("Empty Content Post");
        createPostRequest.setContent("");
        CreatePostResponse createPostResponse = postService.createPostWith(createPostRequest);
        assertEquals("", createPostResponse.getContent());

        assertThat(postRepository.count(), is(1L));
    }

    @Test
    void testThatAfterSetupUserCanCreatAPost() {
        assertTrue(userRepository.existsByUsername("penisup"));

        CreatePostRequest createPostRequest = new CreatePostRequest();
        createPostRequest.setUserId(response.getUserId());
        createPostRequest.setTitle("On a Monday Morning 4am PenIsStillUp");
        createPostRequest.setContent("You Dont Want To Even Know");

        CreatePostResponse createPostResponse = postService.createPostWith(createPostRequest);
        assertEquals("On a Monday Morning 4am PenIsStillUp", createPostResponse.getTitle());
        assertEquals("You Dont Want To Even Know", createPostResponse.getContent());

        List<Post> posts = postRepository.findByUserId(createPostRequest.getUserId());
        assertFalse(posts.isEmpty());
        Post post = posts.get(0);
        assertEquals("On a Monday Morning 4am PenIsStillUp", post.getTitle());


    }

    @Test
    void testThatAfterSetupUserCanCreateMultiplePosts() {
        assertTrue(userRepository.existsByUsername("penisup"));

        CreatePostRequest createPostRequest = new CreatePostRequest();
        createPostRequest.setUserId(response.getUserId());
        createPostRequest.setTitle("On a Monday Morning 4am PenIsStillUp");
        createPostRequest.setContent("You Dont Want To Even Know");
        CreatePostResponse createPostResponse = postService.createPostWith(createPostRequest);

        assertEquals("On a Monday Morning 4am PenIsStillUp", createPostResponse.getTitle());
        assertEquals("You Dont Want To Even Know", createPostResponse.getContent());

        CreatePostRequest createPostRequestTwo = new CreatePostRequest();
        createPostRequestTwo.setUserId(response.getUserId());
        createPostRequestTwo.setTitle("On a Monday Morning 4:30am PenIsStillUp");
        createPostRequestTwo.setContent("You Dont Want To Even Know");
        CreatePostResponse createPostResponseTwo = postService.createPostWith(createPostRequestTwo);

        assertEquals("On a Monday Morning 4:30am PenIsStillUp", createPostResponseTwo.getTitle());
        assertEquals("You Dont Want To Even Know", createPostResponseTwo.getContent());

        User user = userRepository.findByUsername("penisup");
        List<Post> userPosts = postRepository.findByUserId(user.getUserId());

        assertEquals(2, userPosts.size());
    }


    @Test
    void testThatAfterSetupUserCanEditACreatedPost() {
        assertTrue(userRepository.existsByUsername("penisup"));

        CreatePostRequest createPostRequest = new CreatePostRequest();
        createPostRequest.setUserId(response.getUserId());
        createPostRequest.setTitle("On a Monday Morning 4am PenIsStillUp");
        createPostRequest.setContent("You Dont Want To Even Know");
        CreatePostResponse createPostResponse = postService.createPostWith(createPostRequest);

        EditPostRequest editPostRequest = new EditPostRequest();
        editPostRequest.setUserId(createPostRequest.getUserId());
        editPostRequest.setPostId(createPostResponse.getPostId());
        editPostRequest.setTitle("On a Monday Morning 4:30am PenIsStillUp");
        editPostRequest.setContent("You Still Dont Want To Even Know");
        postService.edit(editPostRequest);

        Post updatedPost = postRepository.findByPostId(createPostResponse.getPostId()).orElseThrow();
        assertNotNull(updatedPost);
        assertEquals("On a Monday Morning 4:30am PenIsStillUp", updatedPost.getTitle());
        assertEquals("You Still Dont Want To Even Know", updatedPost.getContent());
    }


    @Test
    void testThatAfterSetupUserCantEditANonCreatedPost() {
        assertTrue(userRepository.existsByUsername("penisup"));

        CreatePostRequest createPostRequest = new CreatePostRequest();
        createPostRequest.setUserId(response.getUserId());
        createPostRequest.setTitle("On a Monday Morning 4am PenIsStillUp");
        createPostRequest.setContent("You Dont Want To Even Know");
        CreatePostResponse createPostResponse = postService.createPostWith(createPostRequest);

        EditPostRequest editPostRequest = new EditPostRequest();
        editPostRequest.setUserId(response.getUserId());
        editPostRequest.setPostId("nonExistingPostId");
        editPostRequest.setTitle("Edited Title");
        editPostRequest.setContent("Edited Content");

        try {
            postService.edit(editPostRequest);
        } catch (BigBlogException message) {
            assertEquals("Post not found", message.getMessage());

        }
        assertFalse(postRepository.existsById(editPostRequest.getPostId()));
    }

    @Test
    void testThatAfterSetupAnotherUserCantEditAnotherUserCreatedPost() {
        assertTrue(userRepository.existsByUsername("penisup"));

        CreatePostRequest createPostRequest = new CreatePostRequest();
        createPostRequest.setUserId(response.getUserId());
        createPostRequest.setTitle("On a Monday Morning 4am PenIsStillUp");
        createPostRequest.setContent("You Dont Want To Even Know");
        CreatePostResponse createPostResponse = postService.createPostWith(createPostRequest);

        RegisterUserRequest registerRequest = new RegisterUserRequest();
        registerRequest.setFirstname("PenIs");
        registerRequest.setLastname("Dowp");
        registerRequest.setUsername("penisupagain");
        registerRequest.setPassword("NoHoles");
        RegisterUserResponse responseOne = userService.register(registerRequest);

        LoginUserRequest loginRequest = new LoginUserRequest();
        loginRequest.setUsername("penisupagain");
        loginRequest.setPassword("NoHoles");
        LoginUserResponse loginResponse = userService.login(loginRequest);
        assertThat(loginResponse.getUsername(), is("penisupagain"));
        assertTrue(userRepository.existsByUsername("penisupagain"));


        EditPostRequest editPostRequest = new EditPostRequest();
        editPostRequest.setUserId(responseOne.getUserId());
        editPostRequest.setPostId(createPostResponse.getPostId());
        editPostRequest.setTitle("Edited Title");
        editPostRequest.setContent("Edited Content");

        try {
            postService.edit(editPostRequest);
        } catch (BigBlogException message) {
            assertEquals("Post does not belong to user", message.getMessage());

        }
        Optional<Post> optionalPost = postRepository.findByPostId(createPostResponse.getPostId());
        assertTrue(optionalPost.isPresent());
        assertEquals("On a Monday Morning 4am PenIsStillUp", optionalPost.get().getTitle());
        assertEquals("You Dont Want To Even Know", optionalPost.get().getContent());


    }


    @Test
    void testThatAfterSetupUserCanDeleteACreatedPost() {
        assertTrue(userRepository.existsByUsername("penisup"));

        CreatePostRequest createPostRequest = new CreatePostRequest();
        createPostRequest.setUserId(response.getUserId());
        createPostRequest.setTitle("On a Monday Morning 4am PenIsStillUp");
        createPostRequest.setContent("You Dont Want To Even Know");
        CreatePostResponse createPostResponse = postService.createPostWith(createPostRequest);

        assertThat(postRepository.count(), is(1L));

        DeletePostRequest deletePostRequest = new DeletePostRequest();
        deletePostRequest.setUserId(response.getUserId());
        deletePostRequest.setPostId(createPostResponse.getPostId());
        postService.delete(deletePostRequest);

        assertFalse(postRepository.existsById(createPostResponse.getPostId()));
    }

    @Test
    void testThatAfterSetupUserPostCantBeDeletedByAnotherRegisterUser() {
        assertTrue(userRepository.existsByUsername("penisup"));

        CreatePostRequest createPostRequest = new CreatePostRequest();
        createPostRequest.setUserId(response.getUserId());
        createPostRequest.setTitle("On a Monday Morning 4am PenIsStillUp");
        createPostRequest.setContent("You Dont Want To Even Know");
        CreatePostResponse createPostResponse = postService.createPostWith(createPostRequest);

        assertThat(postRepository.count(), is(1L));

        RegisterUserRequest registerRequest = new RegisterUserRequest();
        registerRequest.setFirstname("PenIs");
        registerRequest.setLastname("Dowp");
        registerRequest.setUsername("penisdownagain");
        registerRequest.setPassword("NoHoles");
        RegisterUserResponse registerUserResponse = userService.register(registerRequest);

        LoginUserRequest loginRequest = new LoginUserRequest();
        loginRequest.setUsername("penisdownagain");
        loginRequest.setPassword("NoHoles");
        LoginUserResponse loginResponse = userService.login(loginRequest);
        assertThat(loginResponse.getUsername(), is("penisdownagain"));

        DeletePostRequest deletePostRequest = new DeletePostRequest();
        deletePostRequest.setUserId(registerUserResponse.getUserId());
        deletePostRequest.setPostId(createPostResponse.getPostId());
        try {
            postService.delete(deletePostRequest);
        } catch (BigBlogException message) {
            assertEquals("You are not authorized to delete this post", message.getMessage());
        }

        assertThat(postRepository.count(), is(1L));
    }

    @Test
    void testThatAfterSetupUserPostCantBeDeletedByANonRegisterUser() {
        assertTrue(userRepository.existsByUsername("penisup"));

        CreatePostRequest createPostRequest = new CreatePostRequest();
        createPostRequest.setUserId(response.getUserId());
        createPostRequest.setTitle("On a Monday Morning 4am PenIsStillUp");
        createPostRequest.setContent("You Dont Want To Even Know");
        CreatePostResponse createPostResponse = postService.createPostWith(createPostRequest);

        assertThat(postRepository.count(), is(1L));

        DeletePostRequest deletePostRequest = new DeletePostRequest();
        deletePostRequest.setUserId("penisdown");
        deletePostRequest.setPostId(createPostResponse.getPostId());
        try {
            postService.delete(deletePostRequest);
        } catch (BigBlogException message) {
            assertEquals("User with username penisdown not found", message.getMessage());

        }
        assertThat(postRepository.count(), is(1L));

    }


    @Test
    public void testThatWhenUserGetsPostTheViewIncreases() {
        assertTrue(userRepository.existsByUsername("penisup"));

        CreatePostRequest createPostRequest = new CreatePostRequest();
        createPostRequest.setUserId(response.getUserId());
        createPostRequest.setTitle("On a Monday Morning 4am PenIsStillUp");
        createPostRequest.setContent("You Dont Want To Even Know");
        CreatePostResponse createPostResponse = postService.createPostWith(createPostRequest);

        assertThat(postRepository.findByUserId(response.getUserId()).size(), is(1));

        ActivitiesResponse activitiesResponse = postService.getPost(createPostResponse.getPostId(), "penisdown");

        Set<String> userIdsInView = activitiesResponse.getView().getUserId();
        assertTrue(userIdsInView.contains(anotherResponse.getUserId()));

        assertEquals(1, viewRepository.count());
    }

    @Test
    void testThatAfterSetupUserCanGetAllPosts() {
        assertTrue(userRepository.existsByUsername("penisup"));

        CreatePostRequest createPostRequest1 = new CreatePostRequest();
        createPostRequest1.setUserId(response.getUserId());
        createPostRequest1.setTitle("Post 1");
        createPostRequest1.setContent("Content for post 1");
        postService.createPostWith(createPostRequest1);

        CreatePostRequest createPostRequest2 = new CreatePostRequest();
        createPostRequest2.setUserId(response.getUserId());
        createPostRequest2.setTitle("Post 2");
        createPostRequest2.setContent("Content for post 2");
        postService.createPostWith(createPostRequest2);

        List<ActivitiesResponse> allPosts = postService.getAllPosts();

        assertEquals(2, allPosts.size());
    }

    @Test
    void testThatAfterSetupUserCanGetUserPosts() {
        assertTrue(userRepository.existsByUsername("penisup"));

        CreatePostRequest createPostRequest1 = new CreatePostRequest();
        createPostRequest1.setUserId(response.getUserId());
        createPostRequest1.setTitle("Post 1");
        createPostRequest1.setContent("Content for post 1");
        postService.createPostWith(createPostRequest1);

        CreatePostRequest createPostRequest2 = new CreatePostRequest();
        createPostRequest2.setUserId(response.getUserId());
        createPostRequest2.setTitle("Post 2");
        createPostRequest2.setContent("Content for post 2");
        postService.createPostWith(createPostRequest2);

        List<Post> userPosts = postService.getUserPost("penisup");

        assertEquals(2, userPosts.size());
    }

    @Test
    void testThatAfterSetupUserCanGetPost() {
        assertTrue(userRepository.existsByUsername("penisup"));

        CreatePostRequest createPostRequest = new CreatePostRequest();
        createPostRequest.setUserId(response.getUserId());
        createPostRequest.setTitle("Test Post");
        createPostRequest.setContent("This is a test post.");
        CreatePostResponse createPostResponse = postService.createPostWith(createPostRequest);

        ActivitiesResponse activitiesResponse = postService.getPost(createPostResponse.getPostId(), "penisdown");

        assertEquals("Test Post", activitiesResponse.getPost().getTitle());
        assertEquals("This is a test post.", activitiesResponse.getPost().getContent());
    }

}