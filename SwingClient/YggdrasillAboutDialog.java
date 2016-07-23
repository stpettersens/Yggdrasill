//package
/*
        Yggdrasill
        RMI-based distributed HTTP.

        Copyright (c) 2014, 2016 Sam Saint-Pettersen.
*/
import java.util.List;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class YggdrasillAboutDialog extends JDialog {
    public YggdrasillAboutDialog(JFrame owner) {
        super(owner, "About Yggdrasill", JDialog.DEFAULT_MODALITY_TYPE);

        Container ca = getContentPane();
        ca.setBackground(Color.lightGray);
        FlowLayout flm = new FlowLayout();
        ca.setLayout(flm);
        
        JTextArea about = new JTextArea("", 450, 200);
        about.setFont(new Font("monospaced", Font.PLAIN, 11));
        about.setEditable(false);
        about.setLineWrap(true);
        about.setWrapStyleWord(true);
        
        String aboutText = "";
        try {
            aboutText = Files.toString(new File("about.txt"), Charsets.UTF_8);
        }
        catch(IOException ioe) {
            System.out.println(ioe);
        }
        about.setText(aboutText);
        about.setCaretPosition(0);

        JScrollPane aboutScroll = new JScrollPane(about);
        aboutScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        aboutScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        aboutScroll.setPreferredSize(new Dimension(450, 250));
        
        ca.add(aboutScroll);

        setSize(500, 320);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setVisible(true);
    }
}
