package be.zwaldeck.zcms.core.common.rest.pagination;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaginationLinks {

    public PaginationLinks(Object first, String last, String self, String prev, String next) {

    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public String getPrev() {
        return prev;
    }

    public void setPrev(String prev) {
        this.prev = prev;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    private String first;
    private String last;
    private String self;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String prev;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String next;
}
