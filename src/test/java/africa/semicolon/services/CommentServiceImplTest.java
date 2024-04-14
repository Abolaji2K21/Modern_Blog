package africa.semicolon.services;

import africa.semicolon.BlogException.BigBlogException;
import africa.semicolon.BlogException.PostNotFoundException;
import africa.semicolon.data.model.Comment;
import africa.semicolon.data.repositories.CommentRepository;
import africa.semicolon.data.repositories.PostRepository;
import africa.semicolon.data.repositories.UserRepository;
import africa.semicolon.dtos.requests.*;
import africa.semicolon.dtos.responds.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

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

    private RegisterUserResponse userResponse;

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
        userResponse = userService.register(registerRequest);

        LoginUserRequest loginRequest = new LoginUserRequest();
        loginRequest.setUsername("PenIsUp");
        loginRequest.setPassword("Holes");
        LoginUserResponse loginResponse = userService.login(loginRequest);
        assertThat(loginResponse.getUsername(), is("penisup"));

    }

    @Test
    void testThatUserCanCommentOnACreatedPost() {
        CreatePostRequest createPostRequest = new CreatePostRequest();
        createPostRequest.setUserId(userResponse.getUserId());
        createPostRequest.setTitle("On a Monday Morning 4am PenIsStillUp");
        createPostRequest.setContent("You Dont Want To Even Know");
        CreatePostResponse createPostResponse = postService.createPostWith(createPostRequest);

        CreateCommentRequest createCommentRequest = new CreateCommentRequest();
        createCommentRequest.setUserId(userResponse.getUserId());
        createCommentRequest.setPostId(createPostResponse.getPostId());
        createCommentRequest.setComment("This is a comment.");
        CreateCommentResponse createCommentResponse = commentService.createCommentWith(createCommentRequest);

        assertEquals(createPostResponse.getPostId(), createCommentResponse.getPostId());
        assertEquals("This is a comment.", createCommentResponse.getComment());
        assertEquals(userResponse.getUserId(), createCommentResponse.getUserId());
    }


    @Test
    void testThatMoreThanOneUserCanCommentOnACreatedPost() {
        RegisterUserRequest registerRequest = new RegisterUserRequest();
        registerRequest.setFirstname("PenIs");
        registerRequest.setLastname("Down");
        registerRequest.setUsername("PenIsDown");
        registerRequest.setPassword("NoHoles");
        RegisterUserResponse anotherResponse = userService.register(registerRequest);

        LoginUserRequest loginRequest = new LoginUserRequest();
        loginRequest.setUsername("PenIsDown");
        loginRequest.setPassword("NoHoles");
        LoginUserResponse loginResponse = userService.login(loginRequest);
        assertThat(loginResponse.getUsername(), is("penisdown"));

        CreatePostRequest createPostRequest = new CreatePostRequest();
        createPostRequest.setUserId(userResponse.getUserId());
        createPostRequest.setTitle("On a Monday Morning 4am PenIsStillUp");
        createPostRequest.setContent("You Dont Want To Even Know");
        CreatePostResponse createPostResponse = postService.createPostWith(createPostRequest);

        CreateCommentRequest createCommentRequest = new CreateCommentRequest();
        createCommentRequest.setUserId(userResponse.getUserId());
        createCommentRequest.setPostId(createPostResponse.getPostId());
        createCommentRequest.setComment("Motherfucker");
        CreateCommentResponse createCommentResponse = commentService.createCommentWith(createCommentRequest);

        CreateCommentRequest createCommentRequestOne = new CreateCommentRequest();
        createCommentRequestOne.setUserId(anotherResponse.getUserId());
        createCommentRequestOne.setPostId(createPostResponse.getPostId());
        createCommentRequestOne.setComment("Bloody");
        CreateCommentResponse createCommentResponseOne = commentService.createCommentWith(createCommentRequestOne);

        assertEquals(createPostResponse.getPostId(), createCommentResponse.getPostId());
        assertEquals("Motherfucker", createCommentResponse.getComment());
        assertEquals(userResponse.getUserId(), createCommentResponse.getUserId());

        assertEquals(createPostResponse.getPostId(), createCommentResponseOne.getPostId());
        assertEquals("Bloody", createCommentResponseOne.getComment());
        assertEquals(anotherResponse.getUserId(), createCommentResponseOne.getUserId());

    }

    @Test
    void testThatAUserCanOnlyCommentOnACreatedPost() {
        CreatePostRequest createPostRequest = new CreatePostRequest();
        createPostRequest.setUserId(userResponse.getUserId());
        createPostRequest.setTitle("On a Monday Morning 4am PenIsStillUp");
        createPostRequest.setContent("You Dont Want To Even Know");
        CreatePostResponse createPostResponse = postService.createPostWith(createPostRequest);

        CreateCommentRequest createCommentRequest = new CreateCommentRequest();
        createCommentRequest.setUserId(userResponse.getUserId());
        createCommentRequest.setPostId("You Dont Say");
        createCommentRequest.setComment("This comment should fail.");
//        CreateCommentResponse createCommentResponse = commentService.createCommentWith(createCommentRequest);

        assertThrows(BigBlogException.class, () -> commentService.createCommentWith(createCommentRequest));

    }

    @Test
    void testThatYouCanEditACreatedComment() {
        CreatePostRequest createPostRequest = new CreatePostRequest();
        createPostRequest.setUserId(userResponse.getUserId());
        createPostRequest.setTitle("On a Monday Morning 4am PenIsStillUp");
        createPostRequest.setContent("You Dont Want To Even Know");
        CreatePostResponse createPostResponse = postService.createPostWith(createPostRequest);

        CreateCommentRequest createCommentRequest = new CreateCommentRequest();
        createCommentRequest.setUserId(userResponse.getUserId());
        createCommentRequest.setPostId(createPostResponse.getPostId());
        createCommentRequest.setComment("Oga Oga Oga.");
        CreateCommentResponse createCommentResponse = commentService.createCommentWith(createCommentRequest);

        EditCommentRequest editCommentRequest = new EditCommentRequest();
        editCommentRequest.setCommentId(createCommentResponse.getCommentId());
        editCommentRequest.setPostId(createPostResponse.getPostId());
        editCommentRequest.setUserId(userResponse.getUserId());
        editCommentRequest.setComment("You must not and should not");
        EditCommentResponse editCommentResponse = commentService.editCommentWith(editCommentRequest);

        assertEquals("You must not and should not", editCommentResponse.getComment());
    }
    @Test
    void testThatUserCanDeleteTheirComment() {
        CreatePostRequest createPostRequest = new CreatePostRequest();
        createPostRequest.setUserId(userResponse.getUserId());
        createPostRequest.setTitle("On a Monday Morning 4am PenIsStillUp");
        createPostRequest.setContent("You Dont Want To Even Know");
        CreatePostResponse createPostResponse = postService.createPostWith(createPostRequest);

        CreateCommentRequest createCommentRequest = new CreateCommentRequest();
        createCommentRequest.setUserId(userResponse.getUserId());
        createCommentRequest.setPostId(createPostResponse.getPostId());
        createCommentRequest.setComment("This is a comment to be deleted.");
        CreateCommentResponse createCommentResponse = commentService.createCommentWith(createCommentRequest);

        DeleteCommentRequest deleteCommentRequest = new DeleteCommentRequest();
        deleteCommentRequest.setCommentId(createCommentResponse.getCommentId());
        deleteCommentRequest.setUserId(userResponse.getUserId());
        commentService.deleteCommentWith(deleteCommentRequest);

        assertFalse(commentRepository.existsById(createCommentResponse.getCommentId()));
    }

    @Test
    void testThatYouCannotEditNonExistentComment() {
        EditCommentRequest editCommentRequest = new EditCommentRequest();
        editCommentRequest.setCommentId("nonExistentCommentId");
        editCommentRequest.setPostId("nonExistentPostId");
        editCommentRequest.setUserId("nonExistentUserId");
        editCommentRequest.setComment("This comment should not exist.");

        assertThrows(BigBlogException.class, () -> commentService.editCommentWith(editCommentRequest));
    }

    @Test
    void testThatUserCannotEditCommentCreatedByAnotherUser() {
        RegisterUserRequest registerRequest = new RegisterUserRequest();
        registerRequest.setFirstname("Another");
        registerRequest.setLastname("User");
        registerRequest.setUsername("AnotherUser");
        registerRequest.setPassword("AnotherPassword");
        RegisterUserResponse anotherUserResponse = userService.register(registerRequest);

        CreatePostRequest createPostRequest = new CreatePostRequest();
        createPostRequest.setUserId(userResponse.getUserId());
        createPostRequest.setTitle("On a Monday Morning 4am PenIsStillUp");
        createPostRequest.setContent("You Dont Want To Even Know");
        CreatePostResponse createPostResponse = postService.createPostWith(createPostRequest);

        CreateCommentRequest createCommentRequest = new CreateCommentRequest();
        createCommentRequest.setUserId(userResponse.getUserId());
        createCommentRequest.setPostId(createPostResponse.getPostId());
        createCommentRequest.setComment("This comment should not be edited by AnotherUser.");
        CreateCommentResponse createCommentResponse = commentService.createCommentWith(createCommentRequest);

        EditCommentRequest editCommentRequest = new EditCommentRequest();
        editCommentRequest.setCommentId(createCommentResponse.getCommentId());
        editCommentRequest.setPostId(createPostResponse.getPostId());
        editCommentRequest.setUserId(anotherUserResponse.getUserId());
        editCommentRequest.setComment("This comment should not be edited by AnotherUser.");

        assertThrows(BigBlogException.class, () -> commentService.editCommentWith(editCommentRequest));
    }

    @Test
    void testThatYouCannotDeleteNonExistentComment() {
        DeleteCommentRequest deleteCommentRequest = new DeleteCommentRequest();
        deleteCommentRequest.setCommentId("nonExistentCommentId");
        deleteCommentRequest.setUserId("nonExistentUserId");

        assertThrows(BigBlogException.class, () -> commentService.deleteCommentWith(deleteCommentRequest));
    }

    @Test
    void testThatUserCannotDeleteCommentCreatedByAnotherUser() {
        RegisterUserRequest registerRequest = new RegisterUserRequest();
        registerRequest.setFirstname("Another");
        registerRequest.setLastname("User");
        registerRequest.setUsername("AnotherUser");
        registerRequest.setPassword("AnotherPassword");
        RegisterUserResponse anotherUserResponse = userService.register(registerRequest);

        CreatePostRequest createPostRequest = new CreatePostRequest();
        createPostRequest.setUserId(userResponse.getUserId());
        createPostRequest.setTitle("On a Monday Morning 4am PenIsStillUp");
        createPostRequest.setContent("You Dont Want To Even Know");
        CreatePostResponse createPostResponse = postService.createPostWith(createPostRequest);

        CreateCommentRequest createCommentRequest = new CreateCommentRequest();
        createCommentRequest.setUserId(userResponse.getUserId());
        createCommentRequest.setPostId(createPostResponse.getPostId());
        createCommentRequest.setComment("This comment should not be deleted by AnotherUser.");
        CreateCommentResponse createCommentResponse = commentService.createCommentWith(createCommentRequest);

        DeleteCommentRequest deleteCommentRequest = new DeleteCommentRequest();
        deleteCommentRequest.setCommentId(createCommentResponse.getCommentId());
        deleteCommentRequest.setUserId(anotherUserResponse.getUserId());

        assertThrows(BigBlogException.class, () -> commentService.deleteCommentWith(deleteCommentRequest));
    }

    @Test
    void testThatYouCanGetAllCommentsForAPost() {
        CreatePostRequest createPostRequest = new CreatePostRequest();
        createPostRequest.setUserId(userResponse.getUserId());
        createPostRequest.setTitle("On a Monday Morning 4am PenIsStillUp");
        createPostRequest.setContent("You Dont Want To Even Know");
        CreatePostResponse createPostResponse = postService.createPostWith(createPostRequest);

        CreateCommentRequest createCommentRequestOne = new CreateCommentRequest();
        createCommentRequestOne.setUserId(userResponse.getUserId());
        createCommentRequestOne.setPostId(createPostResponse.getPostId());
        createCommentRequestOne.setComment("This is the first comment.");
        CreateCommentResponse createCommentResponseOne = commentService.createCommentWith(createCommentRequestOne);

        CreateCommentRequest createCommentRequestTwo = new CreateCommentRequest();
        createCommentRequestTwo.setUserId(userResponse.getUserId());
        createCommentRequestTwo.setPostId(createPostResponse.getPostId());
        createCommentRequestTwo.setComment("This is the second comment.");
        CreateCommentResponse createCommentResponseTwo = commentService.createCommentWith(createCommentRequestTwo);

        List<Comment> comments = commentService.getCommentsByUser(createPostResponse.getPostId(),userResponse.getUserId());
        assertEquals(2, comments.size());

        boolean commentOneFound = false;
        boolean commentTwoFound = false;
        for (Comment comment : comments) {
            if (comment.getComment().equals(createCommentResponseOne.getComment())) {
                commentOneFound = true;
            }
            if (comment.getComment().equals(createCommentResponseTwo.getComment())) {
                commentTwoFound = true;
            }
        }

        assertTrue(commentOneFound);
        assertTrue(commentTwoFound);
    }

    @Test
    void testThatYouCannotGetCommentsForNonExistentPost() {
        assertThrows(PostNotFoundException.class, () -> commentService.getCommentBy("nonExistentPostId"));
    }


}