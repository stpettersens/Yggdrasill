//package
/*
    Yggdrasill
    RMI-based distributed HTTP.

    Copyright (c) 2014, 2016 Sam Saint-Pettersen.
*/
import java.util.List;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Queue;
import java.util.LinkedList;
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.browser.*;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.events.TraverseEvent;
import net.sf.lipermi.net.Client;
import net.sf.lipermi.handler.CallHandler;

@SuppressWarnings("unchecked")
public class YggdrasillClient {
    private static String title;
    private static String currentPage;
    private static String html;
    private static String serverHtml;
    private static Queue<String> history;
    private static List serverLog;
    private static List fileProperties;
    private static Text txtUri;
    private static Yggdrasill yProxy;
    private static YggdrasillDecoder yDecoder;
    private static Shell shell;
    private static Browser browser;

    public static void main(String args[]) {
        try {
            title = "Yggdrasill Client - ";
            currentPage = "/index.html";
            html = "";
            history = new LinkedList<String>();
            fileProperties = new ArrayList();
            serverLog = new ArrayList();

            Display display = new Display();
            shell = new Shell(display);
            shell.setText(title);
            shell.setSize(700, 550);

            ToolBar toolbar = new ToolBar(shell, SWT.NONE);
            toolbar.setBounds(5, 5, 350, 30);

            ToolItem backButton = new ToolItem(toolbar, SWT.PUSH);
            backButton.setText("<");

            ToolItem goButton = new ToolItem(toolbar, SWT.PUSH);
            goButton.setText("Go");

            ToolItem fwdButton = new ToolItem(toolbar, SWT.PUSH);
            fwdButton.setText(">");

            ToolItem viewSourceButton = new ToolItem(toolbar, SWT.PUSH);
            viewSourceButton.setText("View Source");

            ToolItem filePropsButton = new ToolItem(toolbar, SWT.PUSH);
            filePropsButton.setText("File Properties");

            ToolItem serverLogButton = new ToolItem(toolbar, SWT.PUSH);
            serverLogButton.setText("Server Log");

            ToolItem aboutButton = new ToolItem(toolbar, SWT.PUSH);
            aboutButton.setText("About");

            txtUri = new Text(shell, SWT.BORDER);
            txtUri.setBounds(5, 35, 400, 25); // 5, 35, 400, 25
            txtUri.setText(currentPage);

            browser = new Browser(shell, SWT.NONE);
            browser.setBounds(5, 75, 670, 420);

            initialize();

            browser.addLocationListener(new LocationListener() {
                public void changing(LocationEvent event) {
                    String uri = event.location.replace("about:", "");
                    if(uri.charAt(0) != '/') uri = "/" + uri; // All URIs should begin with "/".
                    makeRequest(uri, true);
                }
                public void changed(LocationEvent event) {
                    // ...
                }
            });

            txtUri.addTraverseListener(new TraverseListener() {
                public void keyTraversed(TraverseEvent event) {
                    if(event.detail == SWT.TRAVERSE_RETURN) {
                        makeRequest(txtUri.getText(), true);
                    }
                }
            });

            Listener listener = new Listener() {
                public void handleEvent(Event event) {
                    ToolItem item = (ToolItem)event.widget;
                    String command = item.getText();
                    if(command.equals("<")) {
                        makeRequest(history.remove(), false);
                    }
                    else if(command.equals("Go")) {
                        makeRequest(txtUri.getText(), true);
                    }
                    else if(command.equals(">")) {
                        makeRequest(history.peek(), true);
                    }
                    else if(command.equals("View Source")) {
                        YggdrasillSourceDialog sourceDialog = new YggdrasillSourceDialog(shell);
                        sourceDialog.open(html, serverHtml);
                    }
                    else if(command.equals("File Properties")) {
                        YggdrasillPropsDialog propertiesDialog = new YggdrasillPropsDialog(shell);
                        propertiesDialog.open(fileProperties);
                    }
                    else if(command.equals("Server Log")) {
                        YggdrasillServerLogDialog serverLogDialog = new YggdrasillServerLogDialog(shell);
                        serverLogDialog.open(serverLog);
                    }
                    else if(command.equals("About")) {
                        YggdrasillAboutDialog aboutDialog = new YggdrasillAboutDialog(shell);
                        aboutDialog.open();
                    }
                }
            };
            backButton.addListener(SWT.Selection, listener);
            goButton.addListener(SWT.Selection, listener);
            fwdButton.addListener(SWT.Selection, listener);
            viewSourceButton.addListener(SWT.Selection, listener);
            filePropsButton.addListener(SWT.Selection, listener);
            serverLogButton.addListener(SWT.Selection, listener);
            aboutButton.addListener(SWT.Selection, listener);
            shell.open();

            while(!shell.isDisposed())
                if(!display.readAndDispatch())
                    display.sleep();
            display.dispose();
       }
       catch (Exception e) {
            System.out.println("\nClient problem: " + e);
       }
    }

    private static void makeRequest(String uri, boolean addHistory) {
        if(uri.equals("/blank")) { // Do not make a request for about:blank.
            txtUri.setText(currentPage);
            return;
        }
        try {
            txtUri.setText(uri); currentPage = uri;
            String request = String.format("GET %s HTTP/1.1", currentPage);
            List response = yProxy.sendRespond(request);
            if(addHistory) history.add(currentPage);
            serverLog.add(String.format("\n%s\n%s", request, response.get(0)));

            yDecoder = new YggdrasillDecoder();
            html = yDecoder.decodeResponse(response, currentPage);
            shell.setText(String.format("Yggdrasill Client - %s", response.get(2)));

            fileProperties.clear(); // Force clear of list first.
            fileProperties.add(response.get(1));
            fileProperties.add(response.get(2));
            fileProperties.add(response.get(3));
            fileProperties.add(response.get(4));
            fileProperties.add(response.get(5));

            if(currentPage.endsWith(".html")) {
                serverHtml = html;
                html = yDecoder.processHtml(serverHtml);
            }
            else {
                serverHtml = html;
            }
            browser.setText(html);
        }
        catch(Exception e) {
            System.out.println("An error occurred whilst retrieving HTTP resource:");
            System.out.println(e);
        }
    }

    private static void initialize() {
        try {
            // A call handler is always needed!
            CallHandler callHandler = new CallHandler();
            // Connect to client.
            Client client = new Client("localhost", 4455, callHandler);
            yProxy = (Yggdrasill)client.getGlobal(Yggdrasill.class);
            makeRequest(txtUri.getText(), true);
        }
        catch(Exception e) {
            System.out.println("An error occurred whilst setting up the client:");
            System.out.println(e);
        }
    }
}
