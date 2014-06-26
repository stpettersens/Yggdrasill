//package
/*
    Yggdrasill
    RMI-based distributed HTTP.

    Copyright (c) 2014 Sam Saint-Pettersen.
*/
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.util.List;
import java.util.ArrayList;
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.browser.*;

public class YggdrasillClient {
    
    private static String title;
    private static String defaultPage;
    private static String html;
    private static List history;
    private static List serverLog;
    private static int pointer;

    @SuppressWarnings("unchecked")
    public static void main(String args[]) 
    {  
        try {   
            title = "Yggdrasill Client - ";
            defaultPage = "/index.html";
            html = "";
            history = new ArrayList();
            serverLog = new ArrayList();
            pointer = -1;
            
            Display display = new Display();
            final Shell shell = new Shell(display);
            shell.setText(title);
            shell.setSize(700, 550);
            
            ToolBar toolbar = new ToolBar(shell, SWT.NONE);
            toolbar.setBounds(5, 5, 300, 30);
            
            ToolItem backButton = new ToolItem(toolbar, SWT.PUSH);
            backButton.setText("<");
            
            ToolItem goButton = new ToolItem(toolbar, SWT.PUSH);
            goButton.setText("Go");
            
            ToolItem fwdButton = new ToolItem(toolbar, SWT.PUSH);
            fwdButton.setText(">");
            
            ToolItem viewSourceButton = new ToolItem(toolbar, SWT.PUSH);
            viewSourceButton.setText("View Source");
            
            ToolItem serverLogButton = new ToolItem(toolbar, SWT.PUSH);
            serverLogButton.setText("Server Log");
            
            final Text text = new Text(shell, SWT.BORDER);
            text.setBounds(5, 35, 400, 25);
            text.setText(defaultPage);
            
            /* # Use the network name established in YggdrasillServer to get a
            proxy to an object implementing the Yggdrasill interface. */
            final Yggdrasill yProxy = (Yggdrasill)LocateRegistry.getRegistry().lookup("YggdrasillService");

            //System.out.println("\nYggdrasill client started...");
            String request = String.format("GET %s HTTP/1.1", defaultPage);
            List response = yProxy.sendRespond(request);
            history.add(defaultPage);
            serverLog.add("\n"+request+"\n");
            serverLog.add(response.get(0));
            pointer++;
            System.out.println(pointer);
            
            //System.out.println(response.get(0));
            final YggdrasillDecoder yDecoder = new YggdrasillDecoder();
            html = yDecoder.decodeResponse(response);
            shell.setText(title + response.get(2));
            //System.out.println(output);
            
            final Browser browser;
            browser = new Browser(shell, SWT.BORDER);
            browser.setText(html);
            browser.setBounds(5, 75, 670, 420);
            
            Listener listener = new Listener() {
                public void handleEvent(Event event) {
                    ToolItem item = (ToolItem)event.widget;
                    String string = item.getText();
                    if (string.equals("<")) {
                        try {
                            pointer--;
                            String page = (String)history.get(pointer);
                            text.setText(page);
                            String request = String.format("GET %s HTTP/1.1", page);
                            List response = yProxy.sendRespond(request);
                            serverLog.add("\n"+request+"\n");
                            serverLog.add(response.get(0));
                            html = yDecoder.decodeResponse(response);
                            shell.setText(title + response.get(2));
                            browser.setText(html);
                            history.add(page);
                            pointer++;
                        }
                        catch(RemoteException e)  {
                            //...
                        }
                    }
                    else if (string.equals("Go")) {
                        try { 
                            String page = text.getText();
                            String request = String.format("GET %s HTTP/1.1", page);
                            List response = yProxy.sendRespond(request);
                            serverLog.add("\n"+request+"\n");
                            serverLog.add(response.get(0));
                            html = yDecoder.decodeResponse(response);
                            shell.setText(title + response.get(2));
                            browser.setText(html);
                            history.add(page);
                            pointer++;
                            System.out.println(pointer);
                            //System.out.println(response.get(0));
                        }
                        catch(RemoteException e) {
                            System.out.println("An error occurred whilst retrieving HTTP resource:");
                            System.out.println(e);
                        }  
                    }
                    else if(string.equals("View Source")) {
                        YggdrasillSourceDialog ysd = new YggdrasillSourceDialog(shell); 
                        ysd.open(html);
                    }
                    else if(string.equals("Server Log")) {
                        YggdrasillServerLogDialog ysld = new YggdrasillServerLogDialog(shell);
                        ysld.open(serverLog);
                    }
                }
            };
            backButton.addListener(SWT.Selection, listener);
            goButton.addListener(SWT.Selection, listener);
            fwdButton.addListener(SWT.Selection, listener);
            viewSourceButton.addListener(SWT.Selection, listener);
            serverLogButton.addListener(SWT.Selection, listener);
            shell.open();
            
            while(!shell.isDisposed()) 
                if(!display.readAndDispatch())
                    display.sleep();
            display.dispose();
            
        }
        catch (Exception e) {
            //System.out.println("\nClient problem: " + e);           
        }
        //System.out.println("Client terminated");
    }
}
