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
public class SiteRequest {

    @NotBlank(message = "site.name.blank")
    @Length(min = 1, max = 255, message = "site.name.length")
    private String name;

    @NotBlank(message = "site.path.blank")
    @Pattern(regexp = "^[a-zA-Z0-9-_/]*$", message = "page.path.invalid")
    private String path;
}
