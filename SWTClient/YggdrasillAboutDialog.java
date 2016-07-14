//package
/*
    Yggdrasill
    RMI-based distributed HTTP.

    Copyright (c) 2014 Sam Saint-Pettersen.
*/
import java.io.*;
import java.util.List;
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class YggdrasillAboutDialog extends Dialog {
    private String about;

    public YggdrasillAboutDialog(Shell parent) {
        super(parent);
        try {
            this.about = Files.toString(new File("about.txt"), Charsets.UTF_8);
        }
        catch(IOException ioe) {
            System.out.println(ioe);
        }
    }

    public void open() {
        Shell shell = new Shell(getParent());
        shell.setText("About Yggdrasill");

        Text text = new Text(shell, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.READ_ONLY | SWT.V_SCROLL);
        text.setBounds(5, 15, 400, 420); // 5, 35, 400, 420

        text.setText(this.about);

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
