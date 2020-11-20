package net.thevpc.jshell;

public class AutoCompleteCandidate {
    private String value;
    private String display;

    public AutoCompleteCandidate(String value) {
        this.value = value;
        this.display = value;
    }

    public AutoCompleteCandidate(String value, String display) {
        this.value = value;
        this.display = display;
    }

    public String getDisplay() {
        return display;
    }

    public String getValue() {
        return value;
    }
}
