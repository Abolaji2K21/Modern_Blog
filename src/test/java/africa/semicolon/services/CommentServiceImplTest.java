package africa.semicolon.services;

import africa.semicolon.BlogException.BigBlogException;
import africa.semicolon.BlogException.PostNotFoundException;
import africa.semicolon.data.repositories.CommentRepository;
import africa.semicolon.data.repositories.PostRepository;
import africa.semicolon.data.repositories.UserRepository;
import africa.semicolon.dtos.requests.*;
import africa.semicolon.dtos.responds.CreateCommentResponse;
import africa.semicolon.dtos.responds.CreatePostResponse;
import africa.semicolon.dtos.responds.EditCommentResponse;
import africa.semicolon.dtos.responds.LoginUserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class CommentServiceImplTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostService postService;
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;


    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
        postRepository.deleteAll();
        commentRepository.deleteAll();

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
    void testThatUserCanCommentOnACreatedPost(){
        CreatePostRequest createPostRequest = new CreatePostRequest();
        createPostRequest.setUsername("penisup");
        createPostRequest.setTitle("On a Monday Morning 4am PenIsStillUp");
        createPostRequest.setContent("You Dont Want To Even Know");
        CreatePostResponse createPostResponse = postService.createPostWith(createPostRequest);

        CreateCommentRequest createCommentRequest = new CreateCommentRequest();
        createCommentRequest.setUsername("penisup");
        createCommentRequest.setPostId(createPostResponse.getPostId());
        createCommentRequest.setContent("This is a comment.");
        CreateCommentResponse createCommentResponse = commentService.createCommentWith(createCommentRequest);

        assertEquals(createPostResponse.getPostId(), createCommentResponse.getPostId());
        assertEquals("This is a comment.", createCommentResponse.getContent());
        assertEquals("penisup", createCommentResponse.getUsername());
    }


    @Test
    void testThatMoreThanOneUserCanCommentOnACreatedPost(){
        RegisterUserRequest registerRequest = new RegisterUserRequest();
        registerRequest.setFirstname("PenIs");
        registerRequest.setLastname("Down");
        registerRequest.setUsername("PenIsDown");
        registerRequest.setPassword("NoHoles");
        userService.register(registerRequest);

        LoginUserRequest loginRequest = new LoginUserRequest();
        loginRequest.setUsername("PenIsDown");
        loginRequest.setPassword("NoHoles");
        LoginUserResponse loginResponse = userService.login(loginRequest);
        assertThat(loginResponse.getUsername(), is("penisdown"));

        CreatePostRequest createPostRequest = new CreatePostRequest();
        createPostRequest.setUsername("penisup");
        createPostRequest.setTitle("On a Monday Morning 4am PenIsStillUp");
        createPostRequest.setContent("You Dont Want To Even Know");
        CreatePostResponse createPostResponse = postService.createPostWith(createPostRequest);

        CreateCommentRequest createCommentRequest = new CreateCommentRequest();
        createCommentRequest.setUsername("penisup");
        createCommentRequest.setPostId(createPostResponse.getPostId());
        createCommentRequest.setContent("Motherfucker");
        CreateCommentResponse createCommentResponse = commentService.createCommentWith(createCommentRequest);

        CreateCommentRequest createCommentRequestOne = new CreateCommentRequest();
        createCommentRequestOne.setUsername("penisdown");
        createCommentRequestOne.setPostId(createPostResponse.getPostId());
        createCommentRequestOne.setContent("Bloody");
        CreateCommentResponse createCommentResponseOne = commentService.createCommentWith(createCommentRequestOne);

        assertEquals(createPostResponse.getPostId(), createCommentResponse.getPostId());
        assertEquals("Motherfucker", createCommentResponse.getContent());
        assertEquals("penisup", createCommentResponse.getUsername());

        assertEquals(createPostResponse.getPostId(), createCommentResponseOne.getPostId());
        assertEquals("Bloody", createCommentResponseOne.getContent());
        assertEquals("penisdown", createCommentResponseOne.getUsername());

    }

    @Test
    void testThatAUserCanOnlyCommentOnACreatedPost(){
        CreatePostRequest createPostRequest = new CreatePostRequest();
        createPostRequest.setUsername("penisup");
        createPostRequest.setTitle("On a Monday Morning 4am PenIsStillUp");
        createPostRequest.setContent("You Dont Want To Even Know");
        CreatePostResponse createPostResponse = postService.createPostWith(createPostRequest);

        CreateCommentRequest createCommentRequest = new CreateCommentRequest();
        createCommentRequest.setUsername("penisup");
        createCommentRequest.setPostId("You Dont Say");
        createCommentRequest.setContent("This comment should fail.");
//        CreateCommentResponse createCommentResponse = commentService.createCommentWith(createCommentRequest);

        assertThrows(BigBlogException.class, () -> commentService.createCommentWith(createCommentRequest));

    }

//    @Test
//    void testThatYouCanEditACreatedComment(){
//        CreatePostRequest createPostRequest = new CreatePostRequest();
//        createPostRequest.setUsername("penisup");
//        createPostRequest.setTitle("On a Monday Morning 4am PenIsStillUp");
//        createPostRequest.setContent("You Dont Want To Even Know");
//        CreatePostResponse createPostResponse = postService.createPostWith(createPostRequest);
//
//        CreateCommentRequest createCommentRequest = new CreateCommentRequest();
//        createCommentRequest.setUsername("penisup");
//        createCommentRequest.setPostId(createPostResponse.getPostId());
//        createCommentRequest.setContent("Oga Oga Oga.");
//        CreateCommentResponse createCommentResponse = commentService.createCommentWith(createCommentRequest);
//
//        EditCommentRequest editCommentRequest = new EditCommentRequest();
//        editCommentRequest.setCommentId(createPostResponse.getPostId());
//        editCommentRequest.setContent("You must not and should not");
//        EditCommentResponse editCommentResponse = commentService.editCommentWith(createCommentRequest);
//        assertEquals("This is the edited comment", editCommentResponse.getContent());
//
//
//    }


}