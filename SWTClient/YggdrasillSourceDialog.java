//package
/*
    Yggdrasill
    RMI-based distributed HTTP.

    Copyright (c) 2014, 2016 Sam Saint-Pettersen.
*/
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;

public class YggdrasillSourceDialog extends Dialog {
    public YggdrasillSourceDialog(Shell parent) {
        super(parent);
    }

    public void open(String sourceCode, String serverSourceCode) {
        Shell shell = new Shell(getParent());
        shell.setText("View Source");

        Button[] rdoSrcs = new Button[2];

        rdoSrcs[0] = new Button(shell, SWT.RADIO);
        rdoSrcs[0].setSelection(true);
        rdoSrcs[0].setText("Client source");
        rdoSrcs[0].setBounds(80, 5, 130, 30);

        rdoSrcs[1] = new Button(shell, SWT.RADIO);
        rdoSrcs[1].setSelection(false);
        rdoSrcs[1].setText("Server source");
        rdoSrcs[1].setBounds(210, 5, 130, 30);

        Text src = new Text(shell, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.READ_ONLY | SWT.V_SCROLL);
        src.setBounds(5, 35, 400, 420);
        src.setText(sourceCode);

        SelectionListener selectionListener = new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                Button item = (Button)event.widget;
                String[] command = item.getText().split(" ", 2);
                if(command[0].equals("Client")) {
                    src.setText(sourceCode);
                }
                else if(command[0].equals("Server")) {
                    src.setText(serverSourceCode);
                }
            }
        };
        rdoSrcs[0].addSelectionListener(selectionListener);
        rdoSrcs[1].addSelectionListener(selectionListener);

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
