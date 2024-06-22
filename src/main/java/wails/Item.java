package wails;

import com.intellij.ui.components.JBBox;
import com.intellij.ui.components.JBLabel;

import javax.swing.*;
import java.awt.*;

public class Item extends JLabel implements ListCellRenderer{
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

        String v = value.toString();
        String substring = v.substring(0, 1);
        JBLabel label = new JBLabel(v);
        label.setOpaque(true);
        label.setForeground(Color.RED);



        JBLabel label2 = new JBLabel(v);
        label2.setOpaque(true);
        label2.setText(" text");
        label2.setForeground(Color.BLUE);

        JBBox horizontalBox = JBBox.createHorizontalBox();
        horizontalBox.add(label);
        horizontalBox.setOpaque(true);
        horizontalBox.setBackground(new Color(0,0,0,0));
//        horizontalBox.add(label2);
        return horizontalBox;
    }
}
