package be.zwaldeck.zcms.core.plugin.exception;

import be.zwaldeck.zcms.core.common.error.ZCMSError;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PluginError implements ZCMSError {

    PLUGIN_NOT_FOUND("plugin_not_found"),
    INSTALL_ERROR("install_error"),
    DELETE_ERROR("delete_error");

    private String key;
}
