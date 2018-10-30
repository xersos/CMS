package be.zwaldeck.zcms.repository.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Site {

    private String id;
    private String name;
    private String path;
    private Date createdAt;
    private Date updatedAt;
}
