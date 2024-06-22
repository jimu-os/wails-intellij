package wails.contributor;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;

import org.jetbrains.annotations.NotNull;
import wails.WailsPlugin;

import java.util.Objects;

/*
 *   添加代码提示 扩展，
 *   对 Aurora 特定的字符串给出提示
 * */
public class WailsContributor extends CompletionContributor {
    @Override
    public void fillCompletionVariants(@NotNull CompletionParameters parameters, @NotNull CompletionResultSet result) {
        WailsPlugin.LoadLookupElement(parameters, result);

        super.fillCompletionVariants(parameters, result);
    }

    public static PsiFile Psi(CompletionParameters parameters) {
        Editor editor = parameters.getEditor();
        Document document = editor.getDocument();
        VirtualFile file = FileDocumentManager.getInstance().getFile(document);
        PsiFile psiFile = PsiManager.getInstance(Objects.requireNonNull(editor.getProject())).findFile(file);
        return psiFile;
    }

}
