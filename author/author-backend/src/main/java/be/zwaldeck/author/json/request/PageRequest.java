package be.zwaldeck.author.json.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageRequest {

    @NotBlank(message = "page.title.blank")
    @Length(min = 1, max = 255, message = "page.title.length")
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    @NotBlank(message = "page.name.blank")
    @Pattern(regexp = "^[a-zA-Z0-9-_=+]*$", message = "page.name.invalid")
    @Length(min = 1, max = 255, message = "page.name.length")
    private String name;

    @NotBlank(message = "page.parent.blank")
    private String parent;
}
