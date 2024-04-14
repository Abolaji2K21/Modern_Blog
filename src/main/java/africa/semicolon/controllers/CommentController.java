package africa.semicolon.controllers;

import africa.semicolon.BlogException.BigBlogException;
import africa.semicolon.dtos.requests.CreateCommentRequest;
import africa.semicolon.dtos.requests.DeleteCommentRequest;
import africa.semicolon.dtos.requests.EditCommentRequest;
import africa.semicolon.dtos.responds.ApiResponse;
import africa.semicolon.services.CommentService;
import africa.semicolon.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PatchMapping("/comment")
    public ResponseEntity<?> CreateComment(@RequestBody CreateCommentRequest createCommentRequest) {
        try {
            var result = commentService.createCommentWith(createCommentRequest);
            return new ResponseEntity<>(new ApiResponse(true, result), CREATED);
        }
        catch (BigBlogException message) {
            return new ResponseEntity<>(new ApiResponse(false, message.getMessage()), BAD_REQUEST);
        }
    }
    @PatchMapping("/edit-comment")
    public ResponseEntity<?> editComment(@RequestBody EditCommentRequest editCommentRequest) {
        try {
            var result = commentService.editCommentWith(editCommentRequest);
            return new ResponseEntity<>(new ApiResponse(true, result), CREATED);
        } catch (BigBlogException message) {
            return new ResponseEntity<>(new ApiResponse(false, message.getMessage()), BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete-comment")
    public ResponseEntity<?> deleteComment(@RequestBody DeleteCommentRequest deleteCommentRequest) {
        try {
            var result = commentService.deleteCommentWith(deleteCommentRequest);
            return new ResponseEntity<>(new ApiResponse(true, result), CREATED);
        } catch (BigBlogException message) {
            return new ResponseEntity<>(new ApiResponse(false, message.getMessage()), BAD_REQUEST);
        }
    }

    @GetMapping("/get-comments/{postId}")
    public ResponseEntity<?> getCommentsByPost(@PathVariable(name = "postId") String postId) {
        try {
            var result = commentService.getCommentBy(postId);
            return new ResponseEntity<>(new ApiResponse(true, result), OK);
        } catch (BigBlogException message) {
            return new ResponseEntity<>(new ApiResponse(false, message.getMessage()), BAD_REQUEST);
        }
    }

    @GetMapping("/get-comments/{postId}/{userId}")
    public ResponseEntity<?> getCommentsByUser(@PathVariable(name = "postId") String postId, @PathVariable(name = "userId") String userId) {
        try {
            var result = commentService.getCommentsByUser(postId, userId);
            return new ResponseEntity<>(new ApiResponse(true, result), OK);
        } catch (BigBlogException message) {
            return new ResponseEntity<>(new ApiResponse(false, message.getMessage()), BAD_REQUEST);
        }
    }
}
