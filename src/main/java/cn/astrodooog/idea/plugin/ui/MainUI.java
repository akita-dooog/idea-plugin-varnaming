package cn.astrodooog.idea.plugin.ui;

import cn.astrodooog.idea.plugin.model.TransferRes;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.intellij.ui.content.ContentFactory;
import com.kenai.jffi.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class MainUI {
    private JPanel mainPanel;
    private JTabbedPane tabbedPane1;
    private JTextField textField;
    private JButton searchBtn;
    private JList xtList;
    private JList udList;
    private JList ctList;

    private DefaultListModel<String> xtListModel;
    private DefaultListModel<String> udListModel;
    private DefaultListModel<String> ctListModel;

    public static final String FANYI_API = "http://fanyi.youdao.com/openapi.do?&keyfrom=CoderVar&key=802458398&type=data&doctype=json&version=1.1&q={}";

    public MainUI() {
        xtListModel = new DefaultListModel<>();
        xtList.setModel(xtListModel);
        xtList.addKeyListener(copyListener());

        udListModel = new DefaultListModel<>();
        udList.setModel(udListModel);
        xtList.addKeyListener(copyListener());

        ctListModel = new DefaultListModel<>();
        ctList.setModel(ctListModel);
        ctList.addKeyListener(copyListener());

        searchBtn.addActionListener(e -> {
            String text = textField.getText();
            if (StrUtil.isBlank(text)) {
                return;
            }

            xtListModel.clear();
            udListModel.clear();

            String res = HttpUtil.get(StrUtil.format(FANYI_API, text));
            System.out.println(res);
            TransferRes transferRes = JSONUtil.toBean(res, TransferRes.class, true);

            transferRes.getTranslation().forEach(t -> {
                xtListModel.addElement(toCamelCase(t));
                udListModel.addElement(toUnderline(t));
                ctListModel.addElement(toConstantVar(t));
            });

            transferRes.getWeb().forEach(web -> {
                web.getValue().forEach(wi -> {
                    xtListModel.addElement(toCamelCase(wi));
                    udListModel.addElement(toUnderline(wi));
                    ctListModel.addElement(toConstantVar(wi));
                });
            });
        });
    }

    private KeyAdapter copyListener() {
        return new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_V && e.isControlDown()) {
                    Clipboard c = getSystemClipboard();
                    try {
                        String clip = (String) c.getData(DataFlavor.stringFlavor);
                        System.out.println("Clipboard contents when pasting: " + clip);
                    } catch (UnsupportedFlavorException | IOException e1) {   }
                }else if(e.getKeyCode() == KeyEvent.VK_C && e.isControlDown()) {
                    Clipboard c = getSystemClipboard();
                    StringSelection a = new StringSelection("Hello");
                    try {
                        System.out.println("StringSelection I just made: " + a.getTransferData(DataFlavor.stringFlavor));
                        c.setContents(a, null);
                        String clip = (String) c.getData(DataFlavor.stringFlavor);
                        System.out.println("Clipboard contents right after copying: " + clip);
                    } catch (UnsupportedFlavorException | IOException e1) {}
                }
            }
        };
    }

    private String toCamelCase(String sentence) {
        return StrUtil.toCamelCase(toUnderline(sentence));
    }

    private String toUnderline(String sentence) {
        sentence = StrUtil.replace(sentence, "-", "_");
        return StrUtil.replace(sentence.toLowerCase(), " ", "_");
    }

    private String toConstantVar(String sentence) {
        return toUnderline(sentence).toUpperCase();
    }

    private Clipboard getSystemClipboard(){
        Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        Clipboard systemClipboard = defaultToolkit.getSystemClipboard();

        return systemClipboard;
    }


    public JComponent getComponent() {
        return mainPanel;
    }
}
