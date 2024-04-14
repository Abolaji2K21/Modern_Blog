package africa.semicolon.controllers;

import africa.semicolon.BlogException.BigBlogException;
import africa.semicolon.dtos.requests.CreatePostRequest;
import africa.semicolon.dtos.requests.DeletePostRequest;
import africa.semicolon.dtos.requests.EditPostRequest;
import africa.semicolon.dtos.responds.ApiResponse;
import africa.semicolon.dtos.responds.CreatePostResponse;
import africa.semicolon.dtos.responds.EditPostResponse;
import africa.semicolon.services.PostService;
import africa.semicolon.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody CreatePostRequest createPostRequest) {
        try {
            CreatePostResponse createPostResponse = postService.createPostWith(createPostRequest);
            return new ResponseEntity<>(createPostResponse, CREATED);
        } catch (BigBlogException message) {
            return new ResponseEntity<>(message.getMessage(), BAD_REQUEST);
        }
    }

    @PutMapping("/edit-post")
    public ResponseEntity<?> editPost(@RequestBody EditPostRequest editPostRequest) {
        try {
            var result = postService.edit(editPostRequest);
            return new ResponseEntity<>(new ApiResponse(true, result), CREATED);
        }
        catch (BigBlogException message) {
            return new ResponseEntity<>(new ApiResponse(false, message.getMessage()), BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete-post")
    public ResponseEntity<?> deletePost(@RequestBody DeletePostRequest deletePostRequest) {
        try {
            var result = postService.delete(deletePostRequest);
            return new ResponseEntity<>(new ApiResponse(true, result), CREATED);
        }
        catch (BigBlogException message) {
            return new ResponseEntity<>(new ApiResponse(false, message.getMessage()), BAD_REQUEST);
        }
    }

    @GetMapping("/{postId}/{username}")
    public ResponseEntity<?> getPost(@PathVariable(name = "postId") String postId, @PathVariable(name = "username") String username) {
        try {
            var post = postService.getPost(postId,username);
            if (post != null) {
                return new ResponseEntity<>(new ApiResponse(true, post), OK);
            } else {
                return new ResponseEntity<>(new ApiResponse(false, "Post not found"), HttpStatus.NOT_FOUND);
            }
        } catch (BigBlogException message) {
            return new ResponseEntity<>(new ApiResponse(false, message.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getAllPosts")
    public ResponseEntity<?> getAllPosts() {
        try {
            var posts = postService.getAllPosts();
            if (!posts.isEmpty()) {
                return new ResponseEntity<>(new ApiResponse(true, posts), OK);
            } else {
                return new ResponseEntity<>(new ApiResponse(false, "No posts found"),NOT_FOUND);
            }
        } catch (BigBlogException message) {
            return new ResponseEntity<>(new ApiResponse(false, message.getMessage()),BAD_REQUEST);
        }
    }

}
