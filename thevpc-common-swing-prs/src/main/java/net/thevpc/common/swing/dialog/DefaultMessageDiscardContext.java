package net.thevpc.common.swing.dialog;

public class DefaultMessageDiscardContext implements MessageDiscardContext{
    private boolean discarded;
    public DefaultMessageDiscardContext() {
    }

    public boolean isDiscarded() {
        return discarded;
    }

    public void setDiscarded(boolean discarded) {
        this.discarded = discarded;
    }
}
