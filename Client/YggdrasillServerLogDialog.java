//package
/*
    Yggdrasill
    RMI-based distributed HTTP.

    Copyright (c) 2014 Sam Saint-Pettersen.
*/
import java.util.List;
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;

public class YggdrasillServerLogDialog extends Dialog
{   
    public YggdrasillServerLogDialog(Shell parent)
    {
        super(parent);
    }
    
    public void open(List serverLog) 
    {
        Shell shell = new Shell(getParent());
        shell.setText("Server Log");
        
        Label label = new Label(shell, SWT.NONE);
        label.setSize(300,30);
        label.setLocation(10, 5);
        label.setText("HTTP requests and responses:");
        
        Text text = new Text(shell, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.READ_ONLY | SWT.V_SCROLL);
        text.setBounds(5, 35, 400, 420);
        
        String finalLog = "";
        for(int i = 0; i < serverLog.size(); i++) {
            finalLog += serverLog.get(i);
        }
        text.setText(finalLog);
        
        shell.pack();
        shell.open();
        
        Display display = getParent().getDisplay();
        while(!shell.isDisposed()) {
            if(!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }
}
