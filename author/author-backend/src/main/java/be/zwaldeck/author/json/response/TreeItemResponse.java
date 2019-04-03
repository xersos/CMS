package be.zwaldeck.author.json.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class TreeItemResponse {

    private String id;

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

    public List<TreeItemResponse> getChildren() {
        return children;
    }

    public void setChildren(List<TreeItemResponse> children) {
        this.children = children;
    }

    private String name;
    private List<TreeItemResponse> children = new ArrayList<>();

    public TreeItemResponse(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public void addChildItem(TreeItemResponse child) {
        this.children.add(child);
    }
}
