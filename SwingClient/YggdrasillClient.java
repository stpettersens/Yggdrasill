//package
/*
    Yggdrasill
    RMI-based distributed HTTP.

    Copyright (c) 2014, 2016 Sam Saint-Pettersen.
*/
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;
import java.util.List;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Queue;
import java.util.LinkedList;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class YggdrasillClient {
    private static String title;
    private static String currentPage;
    private static String html;

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        Window w = new Window();
    }

    public static void setTitle(String newTitle) {
        title = newTitle;
    }
    public static String getTitle() {
        return title;
    }

    public static void setPage(String newPage) {
        currentPage = newPage;
    }
    public static String getPage() {
        return currentPage;
    }

    public static void setHtml(String newHtml) {
        html = newHtml;
    }
    public static String getHtml() {
        return html;
    }
}

@SuppressWarnings("unchecked")
class Window extends JFrame implements ActionListener {
    private static Queue<String> history;
    private static List serverLog;
    private static List fileProperties;
    private static JTextArea browser;

    public Window() {
        super("Yggdrasill Client");
        YggdrasillClient.setTitle("Yggdrasill Client -");
        YggdrasillClient.setPage("/index.html"); // /index.html
        YggdrasillClient.setHtml("");
        //history = new ArrayList();
        history = new LinkedList<String>();
        fileProperties = new ArrayList();
        serverLog = new ArrayList();
        //pointer = -1;

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

        JTextField txtUri = new JTextField(YggdrasillClient.getPage(), 60);
        ca.add(txtUri);

        browser = new JTextArea("Response appears here...", 30, 60);
        browser.setFont(new Font("monospaced", Font.PLAIN, 11));
        ca.add(browser);

        setContentPane(ca);
        lookUpUri();
    }

    private void lookUpUri() {
      try {
          /* Use the network name established in YggdrasillServer to get a
          proxy to an object implementing the Yggdrasill interface. */
          final Yggdrasill yProxy = (Yggdrasill)LocateRegistry.getRegistry().lookup("YggdrasillService");

          //System.out.println("\nYggdrasill client started...");
          String request = String.format("GET %s HTTP/1.1", YggdrasillClient.getPage());
          List response = yProxy.sendRespond(request);
          history.add(YggdrasillClient.getPage());
          serverLog.add(String.format("\n%s\n", request));
          //pointer++;
          //System.out.println(pointer);

          //System.out.println(response.get(0));
          final YggdrasillDecoder yDecoder = new YggdrasillDecoder();
          YggdrasillClient.setHtml(yDecoder.decodeResponse(response));
          //shell.setText(String.format("%s%s", title, response.get(2)));

          fileProperties.add(response.get(1));
          fileProperties.add(response.get(2));
          fileProperties.add(response.get(3));
          fileProperties.add(response.get(4));

          browser.setText(YggdrasillClient.getHtml());
          System.out.println(YggdrasillClient.getHtml());
      }
      catch(NotBoundException nbe) {
          System.out.println("An error occurred whilst setting up the proxy:");
          System.out.println(nbe);
      }
      catch(RemoteException re) {
          System.out.println("An error occurred whilst retrieving HTTP resource:");
          System.out.println(re);
      }
  }

  public void actionPerformed(ActionEvent event) {
    String command = event.getActionCommand();
    if (command == "About") {
      JOptionPane.showMessageDialog(null, "Yggdrasill Client (Swing)\nCopyright 2016 Sam Saint-Pettersen.", "Yddrasill Client", JOptionPane.INFORMATION_MESSAGE);
    }
  }
}
