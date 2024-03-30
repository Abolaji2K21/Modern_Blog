package africa.semicolon.services;

import africa.semicolon.data.model.View;

public interface ViewService {
    View addViewCount(String postId, String userId);
    View getByPostId(String id);
}
