package wails;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.*;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.process.ProcessHandlerFactory;
import com.intellij.execution.process.ProcessTerminatedListener;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import groovyjarjarantlr4.v4.runtime.misc.Nullable;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class WailsRunConfiguration extends RunConfigurationBase<WailsRunConfigurationOptions> {

    private Project project;
    public String RootDirectory;
    public String ExecutePath;
    public String Script;
    public String Params;
    private @org.jetbrains.annotations.NotNull Map<String, String> Environment;


    protected WailsRunConfiguration(Project project,
                                    ConfigurationFactory factory,
                                    String name) {
        super(project, factory, name);
        this.project = project;
    }

    @NotNull
    @Override
    protected WailsRunConfigurationOptions getOptions() {
        return (WailsRunConfigurationOptions) super.getOptions();
    }

    public void setScript(String scriptName) {
        WailsRunConfigurationOptions options = getOptions();
        options.setScript(scriptName);

    }

    public void setExecutePath(String cmd) {
        WailsRunConfigurationOptions options = getOptions();
        options.setExecutePath(cmd);

    }

    public void setRootDirectory(String rootDir) {
        WailsRunConfigurationOptions options = getOptions();
        options.setRootDirectory(rootDir);

    }

    public void setEvironment(Map<String, String> evironment) {
        WailsRunConfigurationOptions options = getOptions();
        options.setEnvironment(evironment);

    }

    public void setParams(String params) {
        WailsRunConfigurationOptions options = getOptions();
        options.setParams(params);
    }


    public Map getEvironment() {
        return getOptions().getEnvironment();
    }


    public String getExecutePath() {
        return getOptions().getExecutePath();
    }

    public String getScript() {
        return getOptions().getScript();
    }

    public String getRootDirectory() {
        return getOptions().getRootDirectory();
    }

    public String getParams() {
        return getOptions().getParams();
    }


    @NotNull
    @Override
    public SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
        return new WailsSettingsEditor(this.project);
    }

    @Nullable
    @Override
    public RunProfileState getState(@NotNull Executor executor,
                                    @NotNull ExecutionEnvironment environment) {



        return new CommandLineState(environment) {
            @NotNull
            @Override
            protected ProcessHandler startProcess() throws ExecutionException {
                WailsRunConfigurationOptions options = getOptions();
//                String root = options.getRootDirectory();
//                String cmd = options.getExecutePath();
//                String script = options.getScript();
//                String params = options.getParams();
//                Map<String, String> environment1 = options.getEnvironment();
                WailsState state = WailsState.getInstance();
                String root = state.RootDirectory;
                String cmd = state.executePath;
                String script = state.scriptName;
                String params = state.params;
                Map<String, String> environment1 = state.environment;

                // 创建 执行命令 执行命令 和参数要分开 cmd 中只能包含可执行文件 参数传后面
                GeneralCommandLine commandLine = new GeneralCommandLine(cmd, script);
                commandLine.setWorkDirectory(root);
                if (params != null && !params.isEmpty()) {
                    commandLine.addParameters(params);
                }
                commandLine.setCharset(StandardCharsets.UTF_8);
                commandLine.withEnvironment(environment1);
                ProcessHandlerFactory instance = ProcessHandlerFactory.getInstance();
                OSProcessHandler processHandler = instance.createColoredProcessHandler(commandLine);
                ProcessTerminatedListener.attach(processHandler);
                return processHandler;
            }
        };
    }
}