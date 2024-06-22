package wails;

import com.goide.GoFileType;
import com.goide.psi.*;
import com.goide.psi.impl.GoStructTypeImpl;
import com.goide.vgo.mod.VgoFileType;
import com.goide.vgo.mod.psi.VgoFile;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.ProjectActivity;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.util.PsiTreeUtil;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wails.GoModule.Module;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/*
 *   项目启动调用
 * */
public class Startup implements ProjectActivity {

    /*
     *   StartupActivity runActivity
     * */
    public void runActivity(@NotNull Project project) {
        // 初始化 mod 信息
        String basePath = project.getBasePath();
        LocalFileSystem fileSystem = LocalFileSystem.getInstance();
        if (basePath == null) return;
        VirtualFile rootPath = fileSystem.findFileByPath(basePath);
        PsiManager instance = PsiManager.getInstance(project);
        GoProject.mods = ScanGoModule(rootPath, instance);
    }


    @Nullable
    @Override
    public Object execute(@NotNull Project project, @NotNull Continuation<? super Unit> continuation) {
        // 初始化 mod 信息
        String basePath = project.getBasePath();
        LocalFileSystem fileSystem = LocalFileSystem.getInstance();
        if (basePath == null) return false;
        VirtualFile rootPath = fileSystem.findFileByPath(basePath);
        if (rootPath == null) return false;
        PsiManager instance = PsiManager.getInstance(project);
        GoProject.mods = ScanGoModule(rootPath, instance);
        LocalFileSystem.getInstance().refresh(true);
        return true;
    }

    /*
     *   扫描 root下的 mod 模块
     * */
    public static ConcurrentHashMap<VirtualFile, Module> ScanGoModule(VirtualFile root, PsiManager manager) {
        ConcurrentHashMap<VirtualFile, Module> mod = new ConcurrentHashMap<>();
        if (root.isDirectory()) {
            VirtualFile[] files = root.getChildren();
            for (VirtualFile file : files) {
                Map<VirtualFile, Module> mods = ScanGoModule(file, manager);
                mod.putAll(mods);
            }
            return mod;
        }
        if (root.getFileType().equals(VgoFileType.INSTANCE)) {
            VgoFile modFile = (VgoFile) manager.findFile(root);
            if (modFile == null) return mod;
            String name = modFile.getModuleName();
            VirtualFile parent = root.getParent();
            //  初始化模块信息存储数据
            Module module = new Module();
            module.Root = parent;
            module.Name = name;
            module.vgoFile = modFile;
            // 检索 aurora 服务器实例
            mod.put(parent, module);
        }
        return mod;
    }

}
