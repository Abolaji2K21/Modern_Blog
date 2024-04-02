package africa.semicolon.services;

import africa.semicolon.BlogException.BigBlogException;
import africa.semicolon.BlogException.PostNotFoundException;
import africa.semicolon.BlogException.UserNotFoundException;
import africa.semicolon.data.model.Comment;
import africa.semicolon.data.model.Post;
import africa.semicolon.data.model.User;
import africa.semicolon.data.repositories.CommentRepository;
import africa.semicolon.data.repositories.PostRepository;
import africa.semicolon.data.repositories.UserRepository;
import africa.semicolon.dtos.requests.CreateCommentRequest;
import africa.semicolon.dtos.requests.DeleteCommentRequest;
import africa.semicolon.dtos.requests.EditCommentRequest;
import africa.semicolon.dtos.responds.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;


    @Override
    public List<Comment> getCommentBy(String postId){
        Post post = postRepository.findById(postId).orElseThrow(()-> new PostNotFoundException("Post not found")) ;
        return  commentRepository.findByPostId(post.getId());
    }

    @Override
    public List<Comment> getCommentsByUser(String postId, String userId){
        User user = userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException("User not found"));
        Post post = postRepository.findById(postId).orElseThrow(()-> new PostNotFoundException("Post not found")) ;
        return  commentRepository.findByPostId(post.getId());
    }


    @Override
    public CreateCommentResponse createCommentWith(CreateCommentRequest createCommentRequest) {
        User user = findUserBy(createCommentRequest.getUsername());

        Post post = postRepository.findById(createCommentRequest.getPostId())
                .orElseThrow(() -> new PostNotFoundException("Post not found"));

        Comment newComment = new Comment();
        newComment.setUserId(user.getId());
        newComment.setPost(post);
        newComment.setComment(createCommentRequest.getContent());
        commentRepository.save(newComment);

        CreateCommentResponse response = new CreateCommentResponse();
        response.setPostId(post.getId());
        response.setContent(newComment.getComment());
        response.setUsername(user.getUsername());
        return response;
    }

    @Override
    public EditCommentResponse editCommentWith(CreateCommentRequest createCommentRequest) {

        Post post = postRepository.findById(createCommentRequest.getPostId())
                .orElseThrow(() -> new PostNotFoundException("Post not found"));


        Comment comment = commentRepository.findByPostAndUserId(post, createCommentRequest.getUsername())
                .orElseThrow(() -> new BigBlogException("Comment not found for postId: "));


        if (!createCommentRequest.getUsername().equals(comment.getUserId())) {
            throw new UserNotFoundException("User does not have permission to edit this comment");
        }

        comment.setComment(createCommentRequest.getContent());

        commentRepository.save(comment);

        EditCommentResponse response = new EditCommentResponse();
        response.setPostId(post.getId());
        response.setContent(comment.getComment());
        response.setUsername(comment.getUserId());

        return response;
    }

    @Override
    public DeleteCommentResponse deleteCommentWith(DeleteCommentRequest deleteCommentRequest) {
        User user = findUserBy(deleteCommentRequest.getUsername());
        Comment comment = commentRepository.findById(deleteCommentRequest.getCommentId())
                .orElseThrow(() -> new BigBlogException("Comment not found"));

        if (!user.getId().equals(comment.getUserId())) {
            throw new UserNotFoundException("You are not authorized to delete this post");
        }
        Post post = comment.getPost();
        post.getComments().remove(comment);
        postRepository.save(post);
        commentRepository.delete(comment);
        return new DeleteCommentResponse();
    }

    @Override
    public User findUserBy(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UserNotFoundException("User with username " + username + " not found");
        }
        return user;
    }
}
