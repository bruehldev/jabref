package net.sf.jabref.logic.logging;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Created by motokito on 06.06.2016.
 */
public enum ObservableCache {
    INSTANCE;

    private ObservableList<String> cacheContent = FXCollections.observableList(new ArrayList());

    public void add(String message) {
        cacheContent.add(message);
    }

    public ObservableList<String> getCacheContent() {
        return this.cacheContent;
    }
}
