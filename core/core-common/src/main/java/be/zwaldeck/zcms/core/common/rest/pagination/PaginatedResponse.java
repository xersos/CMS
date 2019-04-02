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

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public PaginationLinks getLinks() {
        return links;
    }

    public void setLinks(PaginationLinks links) {
        this.links = links;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

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
