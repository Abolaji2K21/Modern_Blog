package africa.semicolon.dtos.responds;

import lombok.Data;

@Data
public class ViewPostResponse {
    private String viewerId;
    private String viewer;
    private String timeOfView;
}