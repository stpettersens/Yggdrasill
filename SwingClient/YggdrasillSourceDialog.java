//package
/*
        Yggdrasill
        RMI-based distributed HTTP.

        Copyright (c) 2014. 2016 Sam Saint-Pettersen.
*/
import javax.swing.*;
import java.awt.*;

public class YggdrasillSourceDialog extends JDialog {
    public YggdrasillSourceDialog(JFrame owner, String sourceCode) {
        super(owner, "View Source", JDialog.DEFAULT_MODALITY_TYPE);

        Container ca = getContentPane();
        ca.setBackground(Color.lightGray);
        FlowLayout flm = new FlowLayout();
        ca.setLayout(flm);

        JLabel label = new JLabel("Source code for current document:");
        ca.add(label);

        JTextArea src = new JTextArea(sourceCode, 450, 200);
        src.setFont(new Font("monospaced", Font.PLAIN, 11));
        src.setEditable(false);
        src.setLineWrap(true);
        src.setWrapStyleWord(true);
        
        JScrollPane srcScroll = new JScrollPane(src);
        srcScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        srcScroll.setPreferredSize(new Dimension(450, 250));
        
        ca.add(srcScroll);

        setSize(500, 320);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setVisible(true);
    }
}
