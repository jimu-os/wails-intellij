package wails;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.components.BaseState;
import com.intellij.openapi.project.Project;
import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import groovyjarjarantlr4.v4.runtime.misc.Nullable;

// ConfigurationFactory
public class WailsConfigurationFactory extends ConfigurationFactory {

    protected WailsConfigurationFactory(ConfigurationType type) {
        super(type);
    }

    @Override
    public @NotNull String getId() {
        return WailsRunConfigurationType.ID;
    }

    @NotNull
    @Override
    public RunConfiguration createTemplateConfiguration(
            @NotNull Project project) {
        return new WailsRunConfiguration(project, this, "wails");
    }

    // WailsRunConfigurationOptions.class
    @Nullable
    @Override
    public Class<? extends BaseState> getOptionsClass() {
        return WailsRunConfigurationOptions.class;
    }

}
