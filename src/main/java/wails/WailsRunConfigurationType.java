package wails;

import com.intellij.execution.configurations.ConfigurationTypeBase;
import com.intellij.openapi.util.NotNullLazyValue;
import wails.Icons.Icons;

public class WailsRunConfigurationType extends ConfigurationTypeBase {

    static final String ID = "WailsRunConfiguration";

    protected WailsRunConfigurationType() {
        super(ID, "Wails", "wails run configuration type",
                NotNullLazyValue.createValue(() -> Icons.WailsIcon));
        addFactory(new WailsConfigurationFactory(this));
    }

}