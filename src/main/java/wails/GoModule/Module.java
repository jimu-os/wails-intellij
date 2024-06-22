package wails.GoModule;

import com.goide.psi.impl.GoFunctionDeclarationImpl;
import com.goide.psi.impl.GoFunctionLitImpl;
import com.goide.psi.impl.GoMethodDeclarationImpl;
import com.goide.vgo.mod.psi.VgoFile;
import com.intellij.openapi.vfs.VirtualFile;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/*
 *   �������洢��ǰ���ļ���Ŀ�е� Go Mod ��Ϣ
 * */
public class Module {

    // mod ����
    public String Name;

    // go module ��Ŀ¼
    public VirtualFile Root;

    public VgoFile vgoFile;

    public Map<String, String> auroraEngine;
}
