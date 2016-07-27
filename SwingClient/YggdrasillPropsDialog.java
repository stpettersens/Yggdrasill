//package
/*
        Yggdrasill
        RMI-based distributed HTTP.

        Copyright (c) 2014, 2016 Sam Saint-Pettersen.
*/
import java.util.List;
import javax.swing.*;
import java.awt.*;

public class YggdrasillPropsDialog extends JDialog {
    public YggdrasillPropsDialog(JFrame owner, List fileProps) {
        super(owner, "File Properties", JDialog.DEFAULT_MODALITY_TYPE);

        Container ca = getContentPane();
        ca.setBackground(Color.lightGray);
        FlowLayout flm = new FlowLayout();
        ca.setLayout(flm);
        
        JLabel label = new JLabel("Properties for current file:", SwingConstants.LEFT);
        ca.add(label);
        
        JTextArea props = new JTextArea("", 450, 200);
        props.setFont(new Font("monospaced", Font.PLAIN, 11));
        props.setEditable(false);
        props.setLineWrap(true);
        props.setWrapStyleWord(true);
        
        props.setText(
        String.format("Title / name:\t\t%s\nMIME type:\t\t\t%s\nCategory:\t\t\t%s\nIs binary?\t\t\t%s",
        fileProps.get(1), fileProps.get(2), fileProps.get(3), fileProps.get(0)));
        
        JScrollPane propsScroll = new JScrollPane(props);
        propsScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        propsScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        propsScroll.setPreferredSize(new Dimension(450, 250));

        ca.add(propsScroll);

        setSize(500, 320);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setVisible(true);
    }
}
