package wails.Icons;

import com.intellij.ide.IconProvider;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.YAMLFileType;

import javax.swing.*;
/*
*   AuroraFrameworkIcon
*   getIcon ���Զ��ض����ļ��ṩ�Զ���ͼ��
* */
public class WailsFrameworkIcon extends IconProvider {
    @Override
    public @Nullable Icon getIcon(@NotNull PsiElement element, int flags) {
        PsiFile psiFile = element.getContainingFile();
        if (psiFile == null) {
            return null;
        }
        // ���� ����Ŷ�ļ�ͼ��
        if (psiFile.getFileType().equals(YAMLFileType.YML)) {
            VirtualFile virtualFile = psiFile.getViewProvider().getVirtualFile();
            String name = virtualFile.getName();
            if (name.startsWith("application")) {
                return Icons.WailsIcon;
            }
        }

        // todo ͨ����ʼ����ɨ�� mod Ϊÿ��mod ��Ŀ¼���ͼ��

        // ���� ��̬��Դ�ļ���ͼ��
        return null;
    }
}
