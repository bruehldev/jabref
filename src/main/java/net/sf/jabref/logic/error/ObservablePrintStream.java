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
    public void println(String s) {

        streamContent.add(s);
        super.println(s); //needed?
    }

    public ObservableList<String> getStreamContent() {
        return streamContent;
    }
}
