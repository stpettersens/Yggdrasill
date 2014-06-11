/*
    Yggdrasill
    RMI-based distributed HTTP.

    Copyright (c) 2014 Sam Saint-Pettersen.
*/
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.util.List;
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.browser.*;

public class YggdrasillClient {
    
    private static String defaultPage = "/index.html";

    public static void main(String args[]) 
    {    
        try {
            Display display = new Display();
            final Shell shell = new Shell(display);
            shell.setText("Yggdrasill Client");
            shell.setSize(680, 520);
            
            ToolBar toolbar = new ToolBar(shell, SWT.NONE);
            toolbar.setBounds(5, 5, 200, 30);
            
            ToolItem goButton = new ToolItem(toolbar, SWT.PUSH);
            goButton.setText("Go");
            
            final Text text = new Text(shell, SWT.BORDER);
            text.setBounds(5, 35, 400, 25);
            text.setText(defaultPage);
            
            /* # Use the network name established in YggdrasillServer to get a
            proxy to an object implementing the Yggdrasill interface. */
            final Yggdrasill yProxy = (Yggdrasill) LocateRegistry.getRegistry().lookup("YggdrasillService");

            //System.out.println("\nYggdrasill client started...");
            List response = yProxy.sendRespond("GET " + defaultPage + " HTTP/1.1", false);
            //System.out.println(response.get(0));
            final YggdrasillDecoder yDecoder = new YggdrasillDecoder();
            String output = yDecoder.decodeResponse(response);
            //System.out.println(output);
            
            final Browser browser;
            browser = new Browser(shell, SWT.BORDER);
            browser.setText(output);
            browser.setBounds(5, 75, 640, 400);
            
            Listener listener = new Listener() {
                public void handleEvent(Event event) {
                    ToolItem item = (ToolItem) event.widget;
                    String string = item.getText();
                    if (string.equals("Go")) {
                        try {              
                            List response = yProxy.sendRespond("GET " + text.getText() + " HTTP/1.1", false);
                            browser.setText(yDecoder.decodeResponse(response));
                        }
                        catch(RemoteException e) {
                            System.out.println("An error occurred whilst retrieving HTTP resource:");
                            System.out.println(e);
                        }  
                    }
                }
            };
            goButton.addListener(SWT.Selection, listener);
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
