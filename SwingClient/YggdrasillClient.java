//package
/*
    Yggdrasill
    RMI-based distributed HTTP.

    Copyright (c) 2014, 2016 Sam Saint-Pettersen.
*/
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
import net.sf.lipermi.net.Client;
import net.sf.lipermi.handler.CallHandler;

public class YggdrasillClient {
    private static String title;
    private static String currentPage;
    private static String html;
    private static String serverHtml;

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        ClientWindow w = new ClientWindow();
    }

    static void setTitle(String newTitle) {
        title = newTitle;
    }
    static String getTitle() {
        return title;
    }

    static void setPage(String newPage) {
        currentPage = newPage;
    }
    static String getPage() {
        return currentPage;
    }

    static void setHtml(String newHtml) {
        html = newHtml;
    }
    static String getHtml() {
        return html;
    }

    static void setServerHtml(String newHtml) {
        serverHtml = newHtml;
    }
    static String getServerHtml() {
        return serverHtml;
    }
}

@SuppressWarnings("unchecked")
class ClientWindow extends JFrame implements ActionListener {
    private static Queue<String> history;
    private static List serverLog;
    private static List fileProperties;
    private static JEditorPane browser;
    private static JTextField txtUri;
    private static Yggdrasill yProxy;
    private static YggdrasillDecoder yDecoder;

    public ClientWindow() {
        super("Yggdrasill Client");
        YggdrasillClient.setTitle("Yggdrasill Client");
        YggdrasillClient.setPage("/index.html");
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
        btnViewSource.addActionListener(this);
        ca.add(btnViewSource);

        JButton btnFileProps = new JButton("File Properties");
        btnFileProps.addActionListener(this);
        ca.add(btnFileProps);

        JButton btnServerLog = new JButton("Server Log");
        btnServerLog.addActionListener(this);
        ca.add(btnServerLog);

        JButton btnAbout = new JButton("About");
        btnAbout.addActionListener(this);
        ca.add(btnAbout);

        txtUri = new JTextField(YggdrasillClient.getPage(), 60);
        ca.add(txtUri);

        browser = new JEditorPane();
        browser.setContentType("text/html");
        browser.setEditable(false);

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
            serverLog.add(String.format("\n%s\n%s", request, response.get(0)));

            yDecoder = new YggdrasillDecoder();
            YggdrasillClient.setHtml(yDecoder.decodeResponse(response, YggdrasillClient.getPage()));

            YggdrasillClient.setTitle(String.format("%s %s", "Yggdrasill Client -", response.get(2)));
            setTitle(YggdrasillClient.getTitle());

            fileProperties.clear(); // Force clear of list first.
            fileProperties.add(response.get(1));
            fileProperties.add(response.get(2));
            fileProperties.add(response.get(3));
            fileProperties.add(response.get(4));
            
            if (YggdrasillClient.getPage().endsWith(".html")) {
                YggdrasillClient.setServerHtml(YggdrasillClient.getHtml());
                YggdrasillClient.setHtml(yDecoder.processHtml(YggdrasillClient.getHtml()));
            }
            else {
                YggdrasillClient.setServerHtml(YggdrasillClient.getHtml());
            }
            browser.setText(YggdrasillClient.getHtml());
        }
        catch(Exception e) {
            System.out.println("An error occurred whilst retrieving HTTP resource:");
            System.out.println(e);
        }
    }

    private void initialize() {
      try {
          // A call handler is always needed!
          CallHandler callHandler = new CallHandler();
          // Connect to client.
          Client client = new Client("localhost", 4455, callHandler);
          yProxy = (Yggdrasill)client.getGlobal(Yggdrasill.class);
          makeRequest();
      }
      catch(Exception e) {
          System.out.println("An error occurred whilst setting up the client:");
          System.out.println(e);
      }
  }

  public void actionPerformed(ActionEvent event) {
    String command = event.getActionCommand();
    if(command.equals("Go")) {
        makeRequest();
    }
    else if(command.equals("View Source")) {
        YggdrasillSourceDialog sourceDialog =
        new YggdrasillSourceDialog(this, YggdrasillClient.getHtml(), YggdrasillClient.getServerHtml());
    }
    else if(command.equals("File Properties")) {
        YggdrasillPropsDialog filePropsDialog = new YggdrasillPropsDialog(this, fileProperties);
    }
    else if(command.equals("Server Log")) {
        YggdrasillServerLogDialog serverLogDialog = new YggdrasillServerLogDialog(this, serverLog);
    }
    else if(command.equals("About")) {
        YggdrasillAboutDialog aboutDialog = new YggdrasillAboutDialog(this);
    }
  }
}
