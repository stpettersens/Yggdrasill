/*
    Yggdrasill
    RMI-based distributed HTTP.

    Copyright (c) 2014 Sam Saint-Pettersen.
*/
import java.rmi.registry.LocateRegistry;
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
            Shell shell = new Shell(display);
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
            Yggdrasill yProxy = (Yggdrasill) LocateRegistry.getRegistry().lookup("YggdrasillService");

            //System.out.println("\nYggdrasill client started...");
            List response = yProxy.sendRespond("GET " + defaultPage + " HTTP/1.1", false);
            //System.out.println(response.get(0));
            YggdrasillDecoder yDecoder = new YggdrasillDecoder();
            String output = yDecoder.decodeResponse(response);
            //System.out.println(output);
            
            Browser browser;
            try {
                browser = new Browser(shell, SWT.BORDER);
                browser.setText(output);
                browser.setBounds(5, 75, 640, 400);
            }
            catch(SWTError e) {
                MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
                messageBox.setMessage("Browser cannot be initialized.");
                messageBox.setText("Exit");
                messageBox.open();
                System.exit(-1);
            }
            
            shell.open();
            
            while(!shell.isDisposed()) 
                if(!display.readAndDispatch())
                    display.sleep();
            display.dispose();
            
        }
        catch (Exception e) {
            //System.out.println("\nClien+t problem: " + e);           
        }
        //System.out.println("Client terminated");
    }
}
