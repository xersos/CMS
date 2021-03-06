package be.zwaldeck.zcms.repository.api.exception;

import be.zwaldeck.zcms.core.common.error.ZCMSError;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RepositoryError implements ZCMSError {

    SITE_NAME_NOT_UNIQUE("site_name_not_unique"),
    SITE_PATH_NOT_UNIQUE("site_path_not_unique"),
    PAGE_NAME_NOT_UNIQUE("page_name_not_unique");

    @Override
    public String getKey() {
        return key;
    }

    private String key;
}
