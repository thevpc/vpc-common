package net.thevpc.jshell;

public interface JShellVarListener {
    default void varAdded(JShellVar jShellVar) {

    }

    default void varValueUpdated(JShellVar jShellVar, String oldValue) {

    }

    default void varExportUpdated(JShellVar jShellVar, boolean oldValue) {

    }

    default void varRemoved(JShellVar jShellVar) {

    }
}
