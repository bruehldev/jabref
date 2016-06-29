package net.sf.jabref.logic.logging;

import net.sf.jabref.logic.error.ObservableMessageWithPriority;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

/**
 * Created by motokito on 20.06.2016.
 */
public enum ObservableMessages {
    INSTANCE;

    private final ListProperty<ObservableMessageWithPriority> messages = new SimpleListProperty<>(FXCollections.observableArrayList());

    public ListProperty<ObservableMessageWithPriority> messagesPropety() {
        return messages;
    }

    public void add(ObservableMessageWithPriority s) {
        messages.add(s);
    }

//    public ArrayList <String> get (){
//        return messages.get().toArray();
//    }
}
