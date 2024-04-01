package africa.semicolon.services;

import africa.semicolon.BlogException.BigBlogException;
import africa.semicolon.data.model.Post;
import africa.semicolon.data.model.User;
import africa.semicolon.data.repositories.PostRepository;
import africa.semicolon.data.repositories.UserRepository;
import africa.semicolon.dtos.requests.*;
import africa.semicolon.dtos.responds.CreatePostResponse;
import africa.semicolon.dtos.responds.DeletePostResponse;
import africa.semicolon.dtos.responds.EditPostResponse;
import africa.semicolon.dtos.responds.LoginUserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

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

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
        postRepository.deleteAll();

        RegisterUserRequest registerRequest = new RegisterUserRequest();
        registerRequest.setFirstname("PenIs");
        registerRequest.setLastname("Up");
        registerRequest.setUsername("PenIsUp");
        registerRequest.setPassword("Holes");
        userService.register(registerRequest);

        LoginUserRequest loginRequest = new LoginUserRequest();
        loginRequest.setUsername("PenIsUp");
        loginRequest.setPassword("Holes");
        LoginUserResponse loginResponse = userService.login(loginRequest);
        assertThat(loginResponse.getUsername(), is("penisup"));

    }

    @Test
    void testThatAfterSetupUserCanCreatePostWithEmptyContent() {
        assertTrue(userRepository.existsByUsername("penisup"));

        CreatePostRequest createPostRequest = new CreatePostRequest();
        createPostRequest.setUsername("penisup");
        createPostRequest.setTitle("Empty Content Post");
        createPostRequest.setContent("");
        CreatePostResponse createPostResponse = postService.createPostWith(createPostRequest);
        assertEquals("", createPostResponse.getContent());

        User user = userRepository.findByUsername("penisup");
        assertThat(user.getPosts().size(), is(1));
    }

    @Test
    void testThatAfterSetupUserCanCreatAPost() {
        assertTrue(userRepository.existsByUsername("penisup"));

        CreatePostRequest createPostRequest = new CreatePostRequest();
        createPostRequest.setUsername("penisup");
        createPostRequest.setTitle("On a Monday Morning 4am PenIsStillUp");
        createPostRequest.setContent("You Dont Want To Even Know");

        CreatePostResponse createPostResponse = postService.createPostWith(createPostRequest);
        assertEquals("On a Monday Morning 4am PenIsStillUp", createPostResponse.getTitle());
        assertEquals("You Dont Want To Even Know", createPostResponse.getContent());
        User user = userRepository.findByUsername("penisup");
        assertEquals(1, user.getPosts().size());
        assertThat(user.getPosts().size(), is(1));

    }

    @Test
    void testThatAfterSetupUserCanCreatMultiplePost() {
        assertTrue(userRepository.existsByUsername("penisup"));

        CreatePostRequest createPostRequest = new CreatePostRequest();
        createPostRequest.setUsername("penisup");
        createPostRequest.setTitle("On a Monday Morning 4am PenIsStillUp");
        createPostRequest.setContent("You Dont Want To Even Know");

        CreatePostResponse createPostResponse = postService.createPostWith(createPostRequest);
        assertEquals("On a Monday Morning 4am PenIsStillUp", createPostResponse.getTitle());
        assertEquals("You Dont Want To Even Know", createPostResponse.getContent());

        CreatePostRequest createPostRequestOne = new CreatePostRequest();
        createPostRequestOne.setUsername("penisup");
        createPostRequestOne.setTitle("On a Monday Morning 4:30am PenIsStillUp");
        createPostRequestOne.setContent("You Dont Want To Even Know");

        CreatePostResponse createPostResponseOne = postService.createPostWith(createPostRequestOne);
        assertEquals("On a Monday Morning 4:30am PenIsStillUp", createPostResponseOne.getTitle());
        assertEquals("You Dont Want To Even Know", createPostResponseOne.getContent());
        User user = userRepository.findByUsername("penisup");

        assertEquals(2, user.getPosts().size());
        assertThat(user.getPosts().size(), is(2));

    }

    @Test
    void testThatAfterSetupUserCanEditACreatedPost() {
        assertTrue(userRepository.existsByUsername("penisup"));

        CreatePostRequest createPostRequest = new CreatePostRequest();
        createPostRequest.setUsername("penisup");
        createPostRequest.setTitle("On a Monday Morning 4am PenIsStillUp");
        createPostRequest.setContent("You Dont Want To Even Know");
        CreatePostResponse createPostResponse = postService.createPostWith(createPostRequest);

        EditPostRequest editPostRequest = new EditPostRequest();
        editPostRequest.setUsername("penisup");
        editPostRequest.setPostId(createPostResponse.getPostId());
        editPostRequest.setTitle("Edited Title");
        editPostRequest.setContent("Edited Content");
        postService.editPostWith(editPostRequest);

        User foundUser = userRepository.findByUsername("penisup");
        Post updatedPost = foundUser.getPosts().getFirst();
        assertEquals(1, foundUser.getPosts().size());
        assertTrue(updatedPost.getTitle().contains("Edited Title"));
        assertTrue(updatedPost.getContent().contains("Edited Content"));

    }

    @Test
    void testThatAfterSetupUserCantEditANonCreatedPost() {
        assertTrue(userRepository.existsByUsername("penisup"));

        CreatePostRequest createPostRequest = new CreatePostRequest();
        createPostRequest.setUsername("penisup");
        createPostRequest.setTitle("On a Monday Morning 4am PenIsStillUp");
        createPostRequest.setContent("You Dont Want To Even Know");
        CreatePostResponse createPostResponse = postService.createPostWith(createPostRequest);

        EditPostRequest editPostRequest = new EditPostRequest();
        editPostRequest.setUsername("penisup");
        editPostRequest.setPostId("nonExistingPostId");
        editPostRequest.setTitle("Edited Title");
        editPostRequest.setContent("Edited Content");

        try{
            postService.editPostWith(editPostRequest);
        } catch (BigBlogException message){
            assertEquals("post not found", message.getMessage());

        }
        assertThat(userRepository.findByUsername("penisup").getPosts().size(), is(1));


    }

    @Test
    void testThatAfterSetupAnotherUserCantEditAnotherUserCreatedPost() {
        assertTrue(userRepository.existsByUsername("penisup"));

        CreatePostRequest createPostRequest = new CreatePostRequest();
        createPostRequest.setUsername("penisup");
        createPostRequest.setTitle("On a Monday Morning 4am PenIsStillUp");
        createPostRequest.setContent("You Dont Want To Even Know");
        CreatePostResponse createPostResponse = postService.createPostWith(createPostRequest);

        RegisterUserRequest registerRequest = new RegisterUserRequest();
        registerRequest.setFirstname("PenIs");
        registerRequest.setLastname("Dowp");
        registerRequest.setUsername("PenIsDown");
        registerRequest.setPassword("NoHoles");
        userService.register(registerRequest);

        LoginUserRequest loginRequest = new LoginUserRequest();
        loginRequest.setUsername("PenIsDown");
        loginRequest.setPassword("NoHoles");
        LoginUserResponse loginResponse = userService.login(loginRequest);
        assertThat(loginResponse.getUsername(), is("penisdown"));
        assertTrue(userRepository.existsByUsername("penisdown"));


        EditPostRequest editPostRequest = new EditPostRequest();
        editPostRequest.setUsername("penisdown");
        editPostRequest.setPostId(createPostResponse.getPostId());
        editPostRequest.setTitle("Edited Title");
        editPostRequest.setContent("Edited Content");

        try{
            postService.editPostWith(editPostRequest);
        } catch (BigBlogException message){
            assertEquals("Post does not belong to user", message.getMessage());

        }
        assertThat(userRepository.findByUsername("penisup").getPosts().size(), is(1));
        assertThat(userRepository.findByUsername("penisdown").getPosts().size(), is(0));


    }


    @Test
    void testThatAfterSetupUserCanDeleteACreatedPost() {
        assertTrue(userRepository.existsByUsername("penisup"));

        CreatePostRequest createPostRequest = new CreatePostRequest();
        createPostRequest.setUsername("penisup");
        createPostRequest.setTitle("On a Monday Morning 4am PenIsStillUp");
        createPostRequest.setContent("You Dont Want To Even Know");
        CreatePostResponse createPostResponse = postService.createPostWith(createPostRequest);
//        assertEquals(1, user.getPosts().size());
        assertThat(userRepository.findByUsername("penisup").getPosts().size(), is(1));

        DeletePostRequest deletePostRequest = new DeletePostRequest();
        deletePostRequest.setUsername("penisup");
        deletePostRequest.setPostId(createPostResponse.getPostId());
        postService.deletePostWith(deletePostRequest);
        assertFalse(postRepository.existsById(createPostResponse.getPostId()));
//        System.out.println("User's list of posts before deletion: " + user.getPosts());
//        User user =userRepository.findByUsername("penisup");
        assertThat(userRepository.findByUsername("penisup").getPosts().size(), is(0));
    }


    @Test
    void testThatAfterSetupUserPostCantBeDeletedByARegisterUser() {
        assertTrue(userRepository.existsByUsername("penisup"));

        CreatePostRequest createPostRequest = new CreatePostRequest();
        createPostRequest.setUsername("penisup");
        createPostRequest.setTitle("On a Monday Morning 4am PenIsStillUp");
        createPostRequest.setContent("You Dont Want To Even Know");
        CreatePostResponse createPostResponse = postService.createPostWith(createPostRequest);
        assertThat(userRepository.findByUsername("penisup").getPosts().size(), is(1));

        RegisterUserRequest registerRequest = new RegisterUserRequest();
        registerRequest.setFirstname("PenIs");
        registerRequest.setLastname("Dowp");
        registerRequest.setUsername("PenIsDown");
        registerRequest.setPassword("NoHoles");
        userService.register(registerRequest);

        LoginUserRequest loginRequest = new LoginUserRequest();
        loginRequest.setUsername("PenIsDown");
        loginRequest.setPassword("NoHoles");
        LoginUserResponse loginResponse = userService.login(loginRequest);
        assertThat(loginResponse.getUsername(), is("penisdown"));

        DeletePostRequest deletePostRequest = new DeletePostRequest();
        deletePostRequest.setUsername("penisdown");
        deletePostRequest.setPostId(createPostResponse.getPostId());
            try{
                postService.deletePostWith(deletePostRequest);
            } catch (BigBlogException message){
                assertEquals("You are not authorized to delete this post", message.getMessage());

            }
        assertThat(userRepository.findByUsername("penisup").getPosts().size(), is(1));

    }

    @Test
    void testThatAfterSetupUserPostCantBeDeletedByANonRegisterUser() {
        assertTrue(userRepository.existsByUsername("penisup"));

        CreatePostRequest createPostRequest = new CreatePostRequest();
        createPostRequest.setUsername("penisup");
        createPostRequest.setTitle("On a Monday Morning 4am PenIsStillUp");
        createPostRequest.setContent("You Dont Want To Even Know");
        CreatePostResponse createPostResponse = postService.createPostWith(createPostRequest);
        assertThat(userRepository.findByUsername("penisup").getPosts().size(), is(1));

        DeletePostRequest deletePostRequest = new DeletePostRequest();
        deletePostRequest.setUsername("penisdown");
        deletePostRequest.setPostId(createPostResponse.getPostId());
        try{
            postService.deletePostWith(deletePostRequest);
        } catch (BigBlogException message){
            assertEquals("User with username penisdown not found", message.getMessage());

        }
        assertThat(userRepository.findByUsername("penisup").getPosts().size(), is(1));

    }


}