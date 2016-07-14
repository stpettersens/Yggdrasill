//package
/*
    Yggdrasill 
    RMI-based distributed HTTP.

    Copyright (c) 2014, 2016 Sam Saint-Pettersen.
*/
import javax.swing.*;

public class YggdrasillClient {
    public static void main(String[] args) {
        Window w = new Window();
    }
}

class Window extends JFrame {
    public Window() {
        super("Yggdrasill");
        setSize(400, 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
