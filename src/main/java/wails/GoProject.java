package wails;

import com.intellij.openapi.vfs.VirtualFile;

import java.util.concurrent.ConcurrentHashMap;

import wails.GoModule.Module;

public class GoProject {
    public static ConcurrentHashMap<VirtualFile, Module> mods = new ConcurrentHashMap<>();
}
