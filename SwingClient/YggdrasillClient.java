//package
/*
    Yggdrasill
    RMI-based distributed HTTP.

    Copyright (c) 2014, 2016 Sam Saint-Pettersen.
*/
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class YggdrasillClient {
    public static void main(String[] args) {
        Window w = new Window();
    }
}

class Window extends JFrame implements ActionListener {
    public Window() {
        super("Yggdrasill Client");
        setSize(700, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        Container ca = getContentPane();
        ca.setBackground(Color.lightGray);
        FlowLayout flm = new FlowLayout();
        ca.setLayout(flm);

        JButton btnBack = new JButton("<");
        ca.add(btnBack);

        JButton btnGo = new JButton("Go");
        ca.add(btnGo);

        JButton btnForward = new JButton(">");
        ca.add(btnForward);

        JButton btnViewSource = new JButton("View Source");
        ca.add(btnViewSource);

        JButton btnFileProps = new JButton("File Properties");
        ca.add(btnFileProps);

        JButton btnServerLog = new JButton("Server Log");
        ca.add(btnServerLog);

        JButton btnAbout = new JButton("About");
        btnAbout.addActionListener(this);
        ca.add(btnAbout);

        JTextField txtUri = new JTextField("/index.html", 60);
        ca.add(txtUri);

        setContentPane(ca);
    }

    public void actionPerformed(ActionEvent event) {
      JOptionPane.showMessageDialog(null, "Yggdrasill Client (Swing)\nCopyright 2016 Sam Saint-Pettersen.", "Yddrasill Client", JOptionPane.INFORMATION_MESSAGE);
    }
}
