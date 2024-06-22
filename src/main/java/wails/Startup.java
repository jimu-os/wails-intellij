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
 *   ��Ŀ��������
 * */
public class Startup implements ProjectActivity {

    /*
     *   StartupActivity runActivity
     * */
    public void runActivity(@NotNull Project project) {
        // ��ʼ�� mod ��Ϣ
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
        // ��ʼ�� mod ��Ϣ
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
     *   ɨ�� root�µ� mod ģ��
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
            //  ��ʼ��ģ����Ϣ�洢����
            Module module = new Module();
            module.Root = parent;
            module.Name = name;
            module.vgoFile = modFile;
            // ���� aurora ������ʵ��
            mod.put(parent, module);
        }
        return mod;
    }

}
