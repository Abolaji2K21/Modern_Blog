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

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping
    public ResponseEntity<?> addPost(@RequestBody CreatePostRequest createPostRequest) {
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
}
