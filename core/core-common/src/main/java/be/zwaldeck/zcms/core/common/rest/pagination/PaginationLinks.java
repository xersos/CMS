package be.zwaldeck.zcms.core.common.rest.pagination;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaginationLinks {

    private String first;
    private String last;
    private String self;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String prev;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String next;
}
