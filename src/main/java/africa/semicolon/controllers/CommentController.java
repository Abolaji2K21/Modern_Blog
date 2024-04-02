package africa.semicolon.controllers;

import africa.semicolon.BlogException.BigBlogException;
import africa.semicolon.dtos.requests.CreateCommentRequest;
import africa.semicolon.dtos.responds.ApiResponse;
import africa.semicolon.services.CommentService;
import africa.semicolon.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PatchMapping("/comment")
    public ResponseEntity<?> addComment(@RequestBody CreateCommentRequest createCommentRequest) {
        try {
            var result = commentService.createCommentWith(createCommentRequest);
            return new ResponseEntity<>(new ApiResponse(true, result), CREATED);
        }
        catch (BigBlogException message) {
            return new ResponseEntity<>(new ApiResponse(false, message.getMessage()), BAD_REQUEST);
        }
    }
}
