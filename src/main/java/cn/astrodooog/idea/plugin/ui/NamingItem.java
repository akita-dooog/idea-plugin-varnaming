package cn.astrodooog.idea.plugin.ui;

import cn.hutool.core.util.StrUtil;

import javax.swing.*;
import java.util.Locale;

public class NamingItem {
    private JPanel itemPanel;
    private JTextPane detailPanel;
    private JButton xtBtn;
    private JButton dtBtn;
    private JButton dlBtn;
    private JButton auBtn;
    private JLabel titleLb;

    public NamingItem() {

    }

    public NamingItem(String title, String detail) {
        titleLb.setText(title);
        detailPanel.setText(detail);
        xtBtn.setText(StrUtil.toCamelCase(title));
        dtBtn.setText(StrUtil.toCamelCase(title));
        dlBtn.setText(StrUtil.toUnderlineCase(title));
        auBtn.setText(title.toUpperCase());
    }

    public JComponent getComponent() {
        return itemPanel;
    }
}
