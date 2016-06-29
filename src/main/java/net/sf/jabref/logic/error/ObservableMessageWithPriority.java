package net.sf.jabref.logic.error;

public class ObservableMessageWithPriority {

    private String message;

    private int priority;

    public ObservableMessageWithPriority(String message, int priority) {
        this.message = message;
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
