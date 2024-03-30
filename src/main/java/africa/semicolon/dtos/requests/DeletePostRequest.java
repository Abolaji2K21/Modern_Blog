package africa.semicolon.dtos.requests;

import lombok.Data;

@Data
public class DeletePostRequest {
    private String id;
    private String username;
}