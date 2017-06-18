package io.stpettersens.yggdrasill.client;
/*
        Yggdrasill
        RMI-based distributed HTTP.

        Copyright (c) 2014, 2016 Sam Saint-Pettersen.
*/
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class YggdrasillSourceDialog extends JDialog implements ActionListener {
    private JTextArea src;
    private JRadioButton rdoClientSrc;
    private JRadioButton rdoServerSrc;
    private String sourceCode;
    private String serverSourceCode;

    public YggdrasillSourceDialog(JFrame owner, String sourceCode, String serverSourceCode) {
        super(owner, "View Source", JDialog.DEFAULT_MODALITY_TYPE);

        this.sourceCode = sourceCode;
        this.serverSourceCode = serverSourceCode;

        Container ca = getContentPane();
        ca.setBackground(Color.lightGray);
        FlowLayout flm = new FlowLayout();
        ca.setLayout(flm);

        rdoClientSrc = new JRadioButton("Client source");
        rdoClientSrc.setMnemonic(KeyEvent.VK_C);
        rdoClientSrc.setActionCommand("client");
        rdoClientSrc.setSelected(true);
        rdoClientSrc.addActionListener(this);
        ca.add(rdoClientSrc);

        rdoServerSrc = new JRadioButton("Server source");
        rdoServerSrc.setMnemonic(KeyEvent.VK_S);
        rdoServerSrc.setActionCommand("server");
        rdoServerSrc.setSelected(false);
        rdoServerSrc.addActionListener(this);
        ca.add(rdoServerSrc);

        src = new JTextArea(sourceCode, 450, 250);
        src.setFont(new Font("monospaced", Font.PLAIN, 11));
        src.setEditable(false);
        src.setLineWrap(true);
        src.setWrapStyleWord(true);

        JScrollPane srcScroll = new JScrollPane(src);
        srcScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        srcScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        srcScroll.setPreferredSize(new Dimension(450, 270));

        ca.add(srcScroll);

        setSize(500, 350);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if(command == "client") {
            rdoServerSrc.setSelected(false);
            src.setText(sourceCode);
            src.setCaretPosition(0);
        }
        else if(command == "server") {
            rdoClientSrc.setSelected(false);
            src.setText(serverSourceCode);
            src.setCaretPosition(0);
        }
    }
}
