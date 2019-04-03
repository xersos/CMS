package be.zwaldeck.author.json.response;

import com.fasterxml.jackson.annotation.JsonInclude;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public SiteResponse getSite() {
        return site;
    }

    public void setSite(SiteResponse site) {
        this.site = site;
    }

    public PageResponse getParent() {
        return parent;
    }

    public void setParent(PageResponse parent) {
        this.parent = parent;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    private String title;
    private String path;
    private boolean published;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private SiteResponse site;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private PageResponse parent;
    private Date createdAt;
    private Date updatedAt;
}
