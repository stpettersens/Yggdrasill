//package
/*
    Yggdrasill
    RMI-based distributed HTTP.

    Copyright (c) 2014 Sam Saint-Pettersen.
*/
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;

public class YggdrasillSourceDialog extends Dialog
{   
    public YggdrasillSourceDialog(Shell parent)
    {
        super(parent);
    }
    
    public void open(String sourceCode) 
    {
        Shell shell = new Shell(getParent());
        shell.setText("View Source");
        
        Label label = new Label(shell, SWT.NONE);
        label.setSize(300,30);
        label.setLocation(10, 5);
        label.setText("Source code for current document:");
        
        Text text = new Text(shell, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.READ_ONLY | SWT.V_SCROLL);
        text.setBounds(5, 35, 400, 420);
        text.setText(sourceCode);
        
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
