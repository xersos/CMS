package be.zwaldeck.zcms.core.common.rest.pagination;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PaginatedResponse<T> {

    private List<T> items;
    private int currentPage;
    private int count;
    private int lastPage;
    private long total;
    private PaginationLinks links;
    private boolean first;
    private boolean last;

    public static <T> PaginatedResponse<T> createResponse(Page<T> page, String baseUrl) {
        return createResponse(page, baseUrl, null);
    }

    public static <T> PaginatedResponse<T> createResponse(Page<T> page, String baseUrl, String queryVars) {
        var currentPage = page.getNumber() + 1;
        var linkPostfix = "&size=" + page.getNumberOfElements();

        if (!StringUtils.isEmpty(queryVars)) {
            linkPostfix += "&" + queryVars;
        }

        var first = baseUrl + "?page=1" + linkPostfix;
        var last = baseUrl + "?page=" + page.getTotalPages() + linkPostfix;
        var self = baseUrl + "?page=" + currentPage + linkPostfix;
        var prev = currentPage > 1 ? baseUrl + "?page=" + (currentPage - 1) + linkPostfix : null;
        var next = currentPage < page.getTotalPages() ? baseUrl + "?page=" + (currentPage + 1) + linkPostfix : null;

        var links = new PaginationLinks(first, last, self, prev, next);

        return new PaginatedResponse<>(page.getContent(), currentPage, page.getNumberOfElements(), page.getTotalPages(),
                page.getTotalElements(), links, page.isFirst(), page.isLast());
    }

}
