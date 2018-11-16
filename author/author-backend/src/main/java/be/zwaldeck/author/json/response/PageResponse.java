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
public class PageResponse {

    private String id;
    private String name;
    private String title;
    private String description;
    private boolean published;
    private SiteResponse site;
    private Date createdAt;
    private Date updatedAt;
}
