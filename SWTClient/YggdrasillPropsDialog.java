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

public class YggdrasillPropsDialog extends Dialog {
    public YggdrasillPropsDialog(Shell parent) {
        super(parent);
    }

    public void open(List fileProps) {
        Shell shell = new Shell(getParent());
        shell.setText("File Properties");

        Label label = new Label(shell, SWT.NONE);
        label.setSize(300,30);
        label.setLocation(10, 5);
        label.setText("Properties for current file:");

        Text text = new Text(shell, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.READ_ONLY);
        text.setBounds(5, 35, 400, 420);

        text.setText(
        String.format("Title / name:\t\t\t\t%s\nMIME type:\t\t\t\t%s\nCategory:\t\t\t\t\t%s\nIs binary?\t\t\t\t\t%s",
        fileProps.get(1), fileProps.get(2), fileProps.get(3), fileProps.get(0))
        );

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
