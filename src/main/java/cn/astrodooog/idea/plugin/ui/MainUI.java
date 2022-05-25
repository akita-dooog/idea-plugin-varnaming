package cn.astrodooog.idea.plugin.ui;

import cn.astrodooog.idea.plugin.model.TransferRes;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.intellij.openapi.ui.Messages;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MainUI {
    private JPanel mainPanel;
    private JTextField textField;
    private JButton searchBtn;
    private JList<String> list;
    private JButton xtBtn;
    private JButton ulBtn;
    private JButton scBtn;

    private static final Integer XT_MODEL = 1;

    private static final Integer UD_MODEL = 2;

    private static final Integer SC_MODEL = 3;

    private final DefaultListModel<String> listModel;

    private final List<String> dataList = new ArrayList<>();

    public static final String FANYI_API = "http://fanyi.youdao.com/openapi.do?&keyfrom=CoderVar&key=802458398&type=data&doctype=json&version=1.1&q={}";

    public MainUI() {
        listModel = new DefaultListModel<>();
        list.setModel(listModel);
        list.addKeyListener(copyListener());

        xtBtn.addActionListener(e -> refreshList(XT_MODEL));
        ulBtn.addActionListener(e -> refreshList(UD_MODEL));
        scBtn.addActionListener(e -> refreshList(SC_MODEL));

        searchBtn.addActionListener(e -> {
            String text = textField.getText();
            if (StrUtil.isBlank(text)) {
                return;
            }

            dataList.clear();

            try {
                String res = HttpUtil.get(StrUtil.format(FANYI_API, text));
                System.out.println(res);
                TransferRes transferRes = JSONUtil.toBean(res, TransferRes.class, false);

                if (Objects.nonNull(transferRes.getTranslation())
                        && transferRes.getTranslation().size() > 0) {
                    dataList.addAll(transferRes.getTranslation());
                }

                if (Objects.nonNull(transferRes.getWeb())
                        && transferRes.getWeb().size() > 0) {
                    transferRes.getWeb().forEach(web -> {
                        dataList.addAll(web.getValue());
                    });
                }

                refreshList(XT_MODEL);
            } catch (Exception exception) {
                Messages.showMessageDialog("出现异常", "错误", Messages.getErrorIcon());
            }
        });
    }

    private void refreshList(Integer model) {
        listModel.clear();

        List<String> varList = dataList.stream().map(oriSentence -> {
            if (UD_MODEL.equals(model)) {
                return toUnderline(oriSentence);
            }

            if (SC_MODEL.equals(model)) {
                return toConstantVar(oriSentence);
            }

            return toCamelCase(oriSentence);
        }).collect(Collectors.toList());

        listModel.addAll(varList);
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

        return defaultToolkit.getSystemClipboard();
    }


    public JComponent getComponent() {
        return mainPanel;
    }
}
