//package
/*
    Yggdrasill
    RMI-based distributed HTTP.

    Copyright (c) 2014, 2016 Sam Saint-Pettersen.
*/
import java.awt.*;
import javax.swing.*;

public class YggdrasillClient {
    public static void main(String[] args) {
        Window w = new Window();
    }
}

class Window extends JFrame {
    public Window() {
        super("Yggdrasill Client");
        setSize(400, 100);
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
    }
}
