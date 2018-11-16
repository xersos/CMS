package be.zwaldeck.zcms.core.common.rest.pagination;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.Map;

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
        var currentPage = page.getNumber() + 1;
        var linkPostfix = getQueryStringPostfix();

        var first = baseUrl + "?page=1" + linkPostfix;
        var last = baseUrl + "?page=" + page.getTotalPages() + linkPostfix;
        var self = baseUrl + "?page=" + currentPage + linkPostfix;
        var prev = currentPage > 1 ? baseUrl + "?page=" + (currentPage - 1) + linkPostfix : null;
        var next = currentPage < page.getTotalPages() ? baseUrl + "?page=" + (currentPage + 1) + linkPostfix : null;

        var links = new PaginationLinks(first, last, self, prev, next);

        return new PaginatedResponse<>(page.getContent(), currentPage, page.getNumberOfElements(), page.getTotalPages(),
                page.getTotalElements(), links, page.isFirst(), page.isLast());
    }

    private static String getQueryStringPostfix() {
        var requestAttributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        if (requestAttributes == null) {
            return "";
        }

        var paramMap = requestAttributes.getRequest().getParameterMap();
        final var postfix = new StringBuilder();

        paramMap.entrySet().stream()
                .filter(e -> !e.getKey().equalsIgnoreCase("page"))
                .forEach(entry -> postfix
                        .append("&")
                        .append(entry.getKey())
                        .append("=")
                        .append(String.join(",", entry.getValue())));

        return postfix.toString();
    }

}
