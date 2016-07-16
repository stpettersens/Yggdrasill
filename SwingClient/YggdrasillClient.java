//package
/*
    Yggdrasill
    RMI-based distributed HTTP.

    Copyright (c) 2014, 2016 Sam Saint-Pettersen.
*/
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;
import java.io.File;
import java.io.IOException;
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
    private static String rawHtml;

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

    public static void setRawHtml(String newHtml) {
        rawHtml = newHtml;
    }
    public static String getRawHtml() {
        return rawHtml;
    }
}

@SuppressWarnings("unchecked")
class Window extends JFrame implements ActionListener {
    private static Queue<String> history;
    private static List serverLog;
    private static List fileProperties;
    //private static JTextArea browser;
    private static JEditorPane browser; // JPanel
    private static JTextField txtUri;
    private static Yggdrasill yProxy;
    private static YggdrasillDecoder yDecoder;

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

        txtUri = new JTextField(YggdrasillClient.getPage(), 60);
        ca.add(txtUri);

        /*browser = new JTextArea("Response appears here...", 140, 90);
        browser.setFont(new Font("monospaced", Font.PLAIN, 11));
        browser.setEditable(false);
        browser.setLineWrap(true);
        browser.setWrapStyleWord(true);*/

        browser = new JEditorPane();
        browser.setContentType("text/html");
        browser.setEditable(false);
        
        /*browser = new JPanel(new BorderLayout());
        String img = "tyr.jpg"; //String.format("cache/%s", YggdrasillClient.getPage());
        System.out.println(String.format("Rendering image: %s", img));
        ImageIcon image = new ImageIcon(img);
        JLabel label = new JLabel("", image, JLabel.CENTER);
        browser.add(label, BorderLayout.CENTER);*/

        JScrollPane browserScroll = new JScrollPane(browser);
        browserScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        browserScroll.setPreferredSize(new Dimension(660, 450));

        ca.add(browserScroll);
        setContentPane(ca);
        initialize();
    }

    private void makeRequest() {
        try {
            YggdrasillClient.setPage(txtUri.getText());
            String request = String.format("GET %s HTTP/1.1", YggdrasillClient.getPage());
            List response = yProxy.sendRespond(request);
            history.add(YggdrasillClient.getPage());
            serverLog.add(String.format("\n%s\n", request));
            //pointer++;
            //System.out.println(pointer);

            //System.out.println(response.get(0));
            yDecoder = new YggdrasillDecoder();
            YggdrasillClient.setHtml(yDecoder.decodeResponse(response, YggdrasillClient.getPage()));
            //shell.setText(String.format("%s%s", title, response.get(2)));

            fileProperties.add(response.get(1));
            fileProperties.add(response.get(2));
            fileProperties.add(response.get(3));
            fileProperties.add(response.get(4));
            
            browser.setText(yDecoder.processHtml(YggdrasillClient.getHtml()));
            //System.out.println(yDecoder.processHtml(YggdrasillClient.getHtml()));

        }
        catch(RemoteException re) {
            System.out.println("An error occurred whilst retrieving HTTP resource:");
            System.out.println(re);
        }
    }

    private void initialize() {
      try {
          /* Use the network name established in YggdrasillServer to get a
          proxy to an object implementing the Yggdrasill interface. */
          yProxy = (Yggdrasill)LocateRegistry.getRegistry().lookup("YggdrasillService");
          makeRequest();
      }
      catch(RemoteException re) {
          System.out.println("An error occurred whilst retrieving HTTP resource:");
          System.out.println(re);
      }
      catch(NotBoundException nbe) {
          System.out.println("An error occurred whilst setting up the proxy:");
          System.out.println(nbe);
      }
  }

  private void displayAbout() {
      JOptionPane.showMessageDialog(null,
      "Yggdrasill Client (Swing)\nCopyright 2016 Sam Saint-Pettersen.",
      "Yddrasill Client", JOptionPane.INFORMATION_MESSAGE);
  }

  public void actionPerformed(ActionEvent event) {
    String command = event.getActionCommand();
    if(command.equals("Go")) {
        makeRequest();
    }
    else if(command.equals("About")) {
        displayAbout();
    }
  }
}
