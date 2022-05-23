package cn.astrodooog.idea.plugin.factory;

import cn.astrodooog.idea.plugin.ui.MainUI;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

public class NamingFactory implements ToolWindowFactory {

    private MainUI mainUI = new MainUI();

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(mainUI.getComponent(), "", false);
        toolWindow.getContentManager().addContent(content);
    }
}
