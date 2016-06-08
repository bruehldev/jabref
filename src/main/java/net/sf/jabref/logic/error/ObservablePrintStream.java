package net.sf.jabref.logic.error;

import java.io.PrintStream;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ObservablePrintStream extends TeeStream {

    private ObservableList<String> streamContent = FXCollections.observableList(new ArrayList());

    public ObservablePrintStream(PrintStream out1, PrintStream out2) {
        super(out1, out2);
    }

    @Override
    public void write(byte[] buf, int off, int len) {
        super.write(buf, off, len);
        String s = new String(buf, off, len);
        if (!s.equals(System.lineSeparator())) {
            streamContent.add(s.replaceAll(System.lineSeparator(), ""));
        }
    }


    public ObservableList<String> getStreamContent() {
        return streamContent;
    }
}
