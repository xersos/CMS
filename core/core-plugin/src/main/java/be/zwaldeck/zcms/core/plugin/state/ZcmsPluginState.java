package be.zwaldeck.zcms.core.plugin.state;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZcmsPluginState {

    public static final String[] ALLOWED_PLUGIN_STATES = {"STARTED, STOPPED"};

    private String id;
    private String state;
}
