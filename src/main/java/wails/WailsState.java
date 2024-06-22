package wails;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

@Service
@State(name = "wails", storages = {@Storage(value = "wails.xml")})
public final class WailsState implements PersistentStateComponent<WailsState> {

    public static WailsState getInstance() {
        return ApplicationManager.getApplication().getService(WailsState.class);
    }

    public String RootDirectory;
    public String executePath;
    public String scriptName;
    public Map<String, String> environment;
    public String params;

    @Nullable
    @Override
    public WailsState getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull WailsState state) {
        XmlSerializerUtil.copyBean(state, this);
    }


}
