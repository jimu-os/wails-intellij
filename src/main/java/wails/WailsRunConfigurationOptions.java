package wails;

import com.intellij.execution.configurations.RunConfigurationOptions;
import com.intellij.openapi.components.StoredProperty;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;

public class WailsRunConfigurationOptions extends RunConfigurationOptions {

    private  StoredProperty<String> RootDirectory =
            string("").provideDelegate(this, "wails root");

    private  StoredProperty<String> ExecutePath =
            string("").provideDelegate(this, "wails cmd");

    private  StoredProperty<String> Script =
            string("").provideDelegate(this, "wails script");

    private  @NotNull Map<String, String> Environment = new LinkedHashMap();

    private  StoredProperty<String> Params =string("").provideDelegate(this, "params");
    
    public String getExecutePath() {
        return ExecutePath.getValue(this);
    }

    public void setExecutePath(String cmd) {
        ExecutePath.setValue(this, cmd);
    }

    public String getScript() {
        return Script.getValue(this);
    }

    public void setScript(String scriptName) {
        Script.setValue(this, scriptName);
    }

    public String getRootDirectory() {
        return RootDirectory.getValue(this);
    }

    public void setRootDirectory(String rootDirectory) {
        RootDirectory.setValue(this, rootDirectory);
    }

    public void setEnvironment(Map<String,String> env){
        this.Environment=env;
    }

    public Map<String,String> getEnvironment(){
        return this.Environment;
    }

    public void setParams(String params){
        this.Params.setValue(this,params);
    }

    public String getParams(){
        return this.Params.getValue(this);
    }

}