//package
/*
    Yggdrasill
    RMI-based distributed HTTP.

    Copyright (c) 2014, 2016 Sam Saint-Pettersen.
*/
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.util.List;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Queue;
import java.util.LinkedList;
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
        btnGo.addActionListener(this);
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

        JTextArea browser = new JTextArea("Response appears here...", 30, 60);
        ca.add(browser);

        setContentPane(ca);
    }

    public void actionPerformed(ActionEvent event) {
      String command = event.getActionCommand();
      if (command == "About") {
        JOptionPane.showMessageDialog(null, "Yggdrasill Client (Swing)\nCopyright 2016 Sam Saint-Pettersen.", "Yddrasill Client", JOptionPane.INFORMATION_MESSAGE);
      }
    }
}
