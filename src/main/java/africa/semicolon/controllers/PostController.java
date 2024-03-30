package africa.semicolon.controllers;

import africa.semicolon.dtos.requests.CreatePostRequest;
import africa.semicolon.dtos.responds.CreatePostResponse;
import africa.semicolon.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping
    public ResponseEntity<?> addPost(@RequestBody CreatePostRequest createPostRequest) {
        try {
            CreatePostResponse createPostResponse = postService.createPostWith(createPostRequest);
            return new ResponseEntity<>(createPostResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
