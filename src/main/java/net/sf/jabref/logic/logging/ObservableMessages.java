package net.sf.jabref.logic.logging;

import javafx.beans.Observable;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

import net.sf.jabref.logic.error.ObservableMessageWithPriority;

/**
 * Created by motokito on 20.06.2016.
 */
public enum ObservableMessages {
    INSTANCE;

    private final ListProperty<ObservableMessageWithPriority> messages = new SimpleListProperty<>(FXCollections.observableArrayList((item ->
            new Observable[]{item.isFilteredProperty()})));

    public ListProperty<ObservableMessageWithPriority> messagesPropety() {
        return messages;
    }

    public void add(ObservableMessageWithPriority s) {
        messages.add(s);
    }

}
