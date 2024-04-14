package africa.semicolon.services;

import africa.semicolon.BlogException.BigBlogException;
import africa.semicolon.BlogException.PostNotFoundException;
import africa.semicolon.BlogException.UserNotFoundException;
import africa.semicolon.data.model.Comment;
import africa.semicolon.data.model.Post;
import africa.semicolon.data.model.User;
import africa.semicolon.data.model.View;
import africa.semicolon.data.repositories.PostRepository;
import africa.semicolon.data.repositories.UserRepository;
import africa.semicolon.dtos.requests.CreatePostRequest;
import africa.semicolon.dtos.requests.DeletePostRequest;
import africa.semicolon.dtos.requests.EditPostRequest;
import africa.semicolon.dtos.responds.CreatePostResponse;
import africa.semicolon.dtos.responds.DeletePostResponse;
import africa.semicolon.dtos.responds.EditPostResponse;
import africa.semicolon.dtos.responds.ActivitiesResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ViewServiceImpl viewService;

    @Autowired
    private CommentService commentService;

    @Override
    public CreatePostResponse createPostWith(CreatePostRequest createPostRequest) {
        User user = findUserByUserId(createPostRequest.getUserId());
        Post newPost = new Post();
        BeanUtils.copyProperties(createPostRequest, newPost);
        newPost.setDateTimeCreated(LocalDateTime.now());
        postRepository.save(newPost);

        CreatePostResponse response = new CreatePostResponse();
        BeanUtils.copyProperties(newPost, response);
        response.setDateCreated(newPost.getDateTimeCreated().toString());
        return response;
    }

    @Override
    public List<Post> getUserPost(String username) {
        User user = userRepository.findByUsername(username);
        return postRepository.findByUserId(user.getUserId());

    }

    @Override
    public ActivitiesResponse getPost(String postId, String username) {
        User user = findUserBy(username);
        Post post = postRepository.findByPostId(postId).orElseThrow();
        View view = null;
        if (!user.getUserId().equals(post.getUserId())) {
            view = viewService.addViewCount(postId, user.getUserId());
        }
        List<Comment> comments = commentService.getCommentBy(postId);
        ActivitiesResponse activitiesResponse = new ActivitiesResponse();
        activitiesResponse.setPost(post);
        activitiesResponse.setView(view);
        activitiesResponse.setComments(comments);
        return activitiesResponse;

    }

    @Override
    public List<ActivitiesResponse> getAllPosts() {

        List<ActivitiesResponse> activitiesResponseList = new ArrayList<>();
        List<Post> posts = postRepository.findAll();
        for (Post post : posts) {
            ActivitiesResponse activitiesResponse = new ActivitiesResponse();
            List<Comment> comments = commentService.getCommentBy(post.getPostId());
            View view = viewService.getByPostId(post.getPostId());
            post.setComments(comments);
            activitiesResponse.setView(view);
            activitiesResponse.setPost(post);
            activitiesResponseList.add(activitiesResponse);
        }
        return activitiesResponseList;
    }

    @Override
    public EditPostResponse edit(EditPostRequest editPostRequest) {
        User user = findUserByUserId(editPostRequest.getUserId());
        Post post = postRepository.findById(editPostRequest.getPostId())
                .orElseThrow(() -> new PostNotFoundException("Post not found"));

        if (!user.getUserId().equals(post.getUserId())) {
            throw new UserNotFoundException("Post does not belong to user");
        }

        String newTitle = editPostRequest.getTitle();
        String newContent = editPostRequest.getContent();

        if (newTitle != null && !newTitle.isEmpty()) {
            post.setTitle(newTitle);
        }

        if (newContent != null && !newContent.isEmpty()) {
            post.setContent(newContent);
        }

        post.setDateTimeUpdated(LocalDateTime.now());
        postRepository.save(post);
        EditPostResponse response = new EditPostResponse();
        BeanUtils.copyProperties(post, response);
        response.setDateCreated(post.getDateTimeCreated().toString());
        return response;
    }



    @Override
    public DeletePostResponse delete(DeletePostRequest deletePostRequest) {
        User user = findUserByUserId(deletePostRequest.getUserId());
        Post post = postRepository.findById(deletePostRequest.getPostId()).orElseThrow(() -> new PostNotFoundException("post not found"));
        if (!post.getUserId().equals(user.getUserId())) {
            throw new BigBlogException("You are not authorized to delete this post");
        }
        postRepository.delete(post);
        return new DeletePostResponse();
    }

    @Override
    public User findUserBy(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UserNotFoundException("User with username " + username + " not found");
        }
        return user;
    }
    private  User findUserByUserId(String userId){
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new UserNotFoundException("User with username " + userId + " not found");
        }
        return user;

    }
}
