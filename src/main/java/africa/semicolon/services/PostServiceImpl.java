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
        User foundUser = findUserBy(createPostRequest.getUsername());
        Post newPost = new Post();

        BeanUtils.copyProperties(createPostRequest, newPost);
        newPost.setDateTimeCreated(LocalDateTime.now());
        newPost.setUserId(foundUser.getId());

        Post mypost = postRepository.save(newPost);
        foundUser.getPosts().add(mypost);
        userRepository.save(foundUser);


        CreatePostResponse response = new CreatePostResponse();
        BeanUtils.copyProperties(newPost, response);
        response.setPostId(newPost.getId());
        response.setDateCreated(newPost.getDateTimeCreated().toString());
        return response;
    }

    @Override
    public List<Post> getUserPost(String username) {
        User user = userRepository.findByUsername(username);
        return postRepository.findByUserId(user.getId());

    }

    @Override
    public ActivitiesResponse getPost(String postId, String username) {
        User user = findUserBy(username);
        Post post = postRepository.findById(postId).orElseThrow();
        View view = null;
        if (!user.getId().equals(post.getUserId())) {
            view = viewService.addViewCount(postId, user.getId());
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
        // stream api
//        List<PostDto> postDtoList = new ArrayList<>();
//        postRepository.findAll().forEach(post -> {
//            PostDto postDto = new PostDto();
//            List<Comment> comments = commentService.getCommentBy(post.getId());
//            View view = viewService.getByPostId(post.getId());
//            post.setComments(comments);
//            postDto.setView(view);
//            postDto.setPost(post);
//            postDtoList.add(postDto);
//        });
//        return postDtoList;

        // for loop
        List<ActivitiesResponse> activitiesResponseList = new ArrayList<>();
        List<Post> posts = postRepository.findAll();
        for (Post post : posts) {
            ActivitiesResponse activitiesResponse = new ActivitiesResponse();
            List<Comment> comments = commentService.getCommentBy(post.getId());
            View view = viewService.getByPostId(post.getId());
            post.setComments(comments);
            activitiesResponse.setView(view);
            activitiesResponse.setPost(post);
            activitiesResponseList.add(activitiesResponse);
        }
        return activitiesResponseList;
    }

    @Override
    public EditPostResponse edit(EditPostRequest editPostRequest) {
        User user = findUserBy(editPostRequest.getUsername());
        Post post = postRepository.findById(editPostRequest.getPostId())
                .orElseThrow(() -> new PostNotFoundException("Post not found"));

        if (!user.getId().equals(post.getUserId())) {
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
        response.setPostId(post.getId());
        response.setDateCreated(post.getDateTimeCreated().toString());

        return response;
    }



    @Override
    public DeletePostResponse delete(DeletePostRequest deletePostRequest) {
        User user = findUserBy(deletePostRequest.getUsername());
        Post post = postRepository.findById(deletePostRequest.getPostId()).orElseThrow(() -> new PostNotFoundException("post not found"));
        if (!post.getUserId().equals(user.getId())) {
            throw new BigBlogException("You are not authorized to delete this post");
        }
        user.getPosts().remove(post);
        userRepository.save(user);
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
}
