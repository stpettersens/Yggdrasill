//package
/*
        Yggdrasill
        RMI-based distributed HTTP.

        Copyright (c) 2014, 2016 Sam Saint-Pettersen.
*/
import java.util.List;
import javax.swing.*;
import java.awt.*;

public class YggdrasillServerLogDialog extends JDialog {
    public YggdrasillServerLogDialog(JFrame owner, List serverLog) {
        super(owner, "Server Log", JDialog.DEFAULT_MODALITY_TYPE);

        Container ca = getContentPane();
        ca.setBackground(Color.lightGray);
        FlowLayout flm = new FlowLayout();
        ca.setLayout(flm);
        
        JLabel label = new JLabel("HTTP requests and responses:", SwingConstants.LEFT);
        ca.add(label);
        
        JTextArea log = new JTextArea("", 450, 200);
        log.setFont(new Font("monospaced", Font.PLAIN, 11));
        log.setEditable(false);
        log.setLineWrap(true);
        log.setWrapStyleWord(true);
        
        String finalLog = "";
        for(int i = 0; i < serverLog.size(); i++) {
            finalLog += serverLog.get(i);
        }
        log.setText(finalLog);

        JScrollPane logScroll = new JScrollPane(log);
        logScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        logScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        logScroll.setPreferredSize(new Dimension(450, 250));

        ca.add(logScroll);

        setSize(500, 320);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setVisible(true);
    }
}
