package wails;

import com.intellij.application.options.ModulesComboBox;
import com.intellij.execution.configuration.EnvironmentVariablesTextFieldWithBrowseButton;
import com.intellij.execution.impl.CheckableRunConfigurationEditor;
import com.intellij.execution.ui.CommonParameterFragments;
import com.intellij.execution.ui.CommonProgramParametersPanel;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.RawCommandLineEditor;
import com.intellij.ui.components.JBBox;
import com.intellij.util.ui.FormBuilder;
import groovyjarjarantlr4.v4.runtime.misc.NotNull;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Arrays;
import java.util.Map;


public class WailsSettingsEditor extends SettingsEditor<WailsRunConfiguration> implements CheckableRunConfigurationEditor<WailsRunConfiguration> {

    public final JPanel myPanel;

    // 项目工作目录
    public final TextFieldWithBrowseButton rootFolder;

    // wails 产出目录
    public final TextFieldWithBrowseButton outFolder;

    // wails.json
    public final TextFieldWithBrowseButton jsonPathField;

    // 运行指令
    public final TextFieldWithBrowseButton execute;

    // 指令脚本
    public final ComboBox<String> script;

    // 运行参数配置
    public RawCommandLineEditor myParamsField;
    // 包信息
    public ModulesComboBox myModulesComboBox;

    public EnvironmentVariablesTextFieldWithBrowseButton myEnvironmentField;

    private Project project;

    public WailsSettingsEditor(Project project) {
        this.project = project;

        // 表单
        FormBuilder formBuilder = FormBuilder.createFormBuilder();
        myPanel = formBuilder.getPanel();

        // 配置文件选择器
        jsonPathField = new TextFieldWithBrowseButton();
        jsonPathField.addBrowseFolderListener("Select Wails.Json Config", "Wails project config", this.project,
                FileChooserDescriptorFactory.createSingleFileDescriptor());

        rootFolder = new TextFieldWithBrowseButton();
        FileChooserDescriptor rootDir = FileChooserDescriptorFactory.createMultipleFoldersDescriptor();

        // 默认 设置当前项目文件夹
        VirtualFile baseDir = project.getBaseDir();
        rootDir.setRoots(baseDir);
        rootFolder.addBrowseFolderListener("Set Working Path", "Working directory", null, rootDir);
        rootFolder.setText(baseDir.getPath());
        rootFolder.getTextField().setForeground(Color.lightGray);

        outFolder = new TextFieldWithBrowseButton();
        outFolder.setText(baseDir.getPath());


        execute = new TextFieldWithBrowseButton();
        execute.addBrowseFolderListener("Select Wails.Json Config", "Wails project config", null,
                FileChooserDescriptorFactory.createSingleFileOrExecutableAppDescriptor());
        execute.getTextField().setForeground(Color.lightGray);

        Map<String, String> getenv = System.getenv();
        String goroot = getenv.get("GOPATH");
        if (goroot != null) {
            File bin = new File(goroot, "bin");
            if (bin.exists()) {
                File[] files = bin.listFiles();
                for (File file : files) {
                    if (file.isFile()) {
                        if (file.getName().startsWith("wails")) {
                            String filePath = file.getPath();
                            filePath = filePath.replaceAll("\\\\", "/");
                            execute.setText(filePath);
                        }
                    }
                }
            }
        }
        script = new ComboBox<>();
        script.addItem("dev");
        script.addItem("build");
        // 加载持久配置
        WailsState state = WailsState.getInstance().getState();

        String name = state.scriptName;
        if (name != null && !name.isEmpty()) {
            switch (name) {
                case "dev" -> script.setSelectedIndex(0);
                case "build" -> script.setSelectedIndex(1);
            }
        }
        String root = state.executePath;
        if (root != null && !root.isEmpty()) {
            execute.setText(root);
        }
        String dir = state.RootDirectory;
        if (dir != null && !dir.isEmpty()) {
            rootFolder.setText(dir);
        }

        JBBox box = JBBox.createHorizontalBox();
        box.add(script);

        // 模块选择器
        myModulesComboBox = new ModulesComboBox();
        Module[] modules = ModuleManager.getInstance(project).getSortedModules();
        myModulesComboBox.setModules(Arrays.stream(modules).toList());
        myModulesComboBox.setSelectedIndex(0);
        JBBox jbBox = JBBox.createHorizontalBox();
        jbBox.add(myModulesComboBox);
        formBuilder.addLabeledComponent("Module:", jbBox);


        formBuilder.addLabeledComponent("Working directory:", rootFolder);
        // 暂时取消 配置文件读取
        formBuilder.addLabeledComponent("Wails.json:", jsonPathField);
        formBuilder.addLabeledComponent("Execute:", execute);
        formBuilder.addLabeledComponent("Script:", box);


        myEnvironmentField = new EnvironmentVariablesTextFieldWithBrowseButton();
        formBuilder.addLabeledComponent("Environment:", myEnvironmentField);

        Map env = state.environment;
        if (env != null && !env.isEmpty()) {
            myEnvironmentField.setEnvs(env);
        }        // 应用程序参数选择器
        myParamsField = new RawCommandLineEditor();
        CommonParameterFragments.setMonospaced(myParamsField.getEditorField());
        CommonProgramParametersPanel.addMacroSupport(myParamsField.getEditorField());
        formBuilder.addLabeledComponent("Program arguments:", myParamsField);

        String params = state.params;
        if (params != null && !params.isEmpty()) {
            myParamsField.setText(params);
        }
    }

    @Override
    protected void resetEditorFrom(WailsRunConfiguration runConfiguration) {
//        rootFolder.setText(runConfiguration.getRootDirectory());
//        execute.setText(runConfiguration.getExecutePath());
//        script.setSelectedItem(runConfiguration.getScript());
//        myParamsField.setText(runConfiguration.getParams());
//        myEnvironmentField.setEnvs(runConfiguration.getEvironment());
    }

    @Override
    protected void applyEditorTo(@NotNull WailsRunConfiguration runConfiguration) {
        // 读取执行器
        String cmd = execute.getText();

        // 读取运行脚本
        int index = script.getSelectedIndex();
        String s = script.getItemAt(index);

        // 读取运行参数
        String paramsFieldText = myParamsField.getText();

        // 读取环境信息
        Map<String, String> envs = myEnvironmentField.getEnvs();

        // 读取工作目录
        String root = rootFolder.getText();

        WailsState.getInstance().environment = envs;
        WailsState.getInstance().scriptName = s;
        WailsState.getInstance().executePath = cmd;
        WailsState.getInstance().params = paramsFieldText;
        WailsState.getInstance().RootDirectory = root;
        runConfiguration.setEvironment(envs);
        runConfiguration.setParams(paramsFieldText);
        runConfiguration.setRootDirectory(root);
        runConfiguration.setExecutePath(cmd);
        runConfiguration.setScript(s);
    }

    @NotNull
    @Override
    protected JComponent createEditor() {
        return myPanel;
    }

    @Override
    public void checkEditorData(WailsRunConfiguration s) {
//        String p = s.getParams();
//        if (p == null || p.isEmpty()) {
//            try {
//                throw new RuntimeConfigurationError("param error");
//            } catch (RuntimeConfigurationError e) {
//                throw new RuntimeException(e);
//            }
//        }
    }
}