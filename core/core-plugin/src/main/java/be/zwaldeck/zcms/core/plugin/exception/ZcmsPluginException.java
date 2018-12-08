package be.zwaldeck.zcms.core.plugin.exception;

import be.zwaldeck.zcms.core.common.error.ZCMSException;

public class ZcmsPluginException extends ZCMSException {

    public ZcmsPluginException(PluginError error) {
        super(error);
    }

    public ZcmsPluginException(PluginError error, Throwable cause) {
        super(error, cause);
    }
}
