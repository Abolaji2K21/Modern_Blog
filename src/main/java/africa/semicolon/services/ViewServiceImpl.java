package africa.semicolon.services;

import africa.semicolon.data.model.View;
import africa.semicolon.data.repositories.ViewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ViewServiceImpl implements ViewService{
    @Autowired
    private ViewRepository viewRepository;


    public View addViewCount(String postId, String userId) {
        View view = viewRepository.findByPostId(postId);
        if (view == null) {
            view = new View();
            view.setPostId(postId);
        }
        view.getUserId().add(userId);
       return viewRepository.save(view);
    }

    public View getByPostId(String id) {
        return viewRepository.findByPostId(id);
    }
}
