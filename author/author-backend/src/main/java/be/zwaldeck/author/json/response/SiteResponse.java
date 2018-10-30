package be.zwaldeck.author.json.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SiteResponse {

    private String id;
    private String name;
    private String path;
    private Date createdAt;
    private Date updatedAt;
}
