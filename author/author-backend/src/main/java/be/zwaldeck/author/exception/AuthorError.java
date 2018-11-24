package be.zwaldeck.author.exception;

import be.zwaldeck.zcms.core.common.error.ZCMSError;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthorError implements ZCMSError {

    PAGE_PARENT_NOT_FOUND("page_parent_not_found");

    private String key;
}
