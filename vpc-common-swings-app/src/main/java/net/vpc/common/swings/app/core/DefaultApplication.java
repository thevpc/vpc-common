package net.vpc.common.swings.app.core;

import net.vpc.common.prpbind.*;
import net.vpc.common.prpbind.impl.AppPropertyBinding;
import net.vpc.common.prpbind.impl.PropertyContainerSupport;
import net.vpc.common.swings.app.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class DefaultApplication implements Application {
    protected WritablePValue<AppStatus> status = Props.of("status").valueOf(AppStatus.class, AppStatus.NONE);
    protected WritablePValue<String> name = Props.of("name").valueOf(String.class, "");
    protected WritablePList<AppMessage> messages = Props.of("messages").linkedListOf(AppMessage.class);
    protected WritablePList<AppMessage> log = Props.of("log").linkedListOf(AppMessage.class);
    protected WritablePList<AppMessageProducer> messageProducers = Props.of("messageProducers").listOf(AppMessageProducer.class);
    protected PropertyContainerSupport support = new PropertyContainerSupport("app", this);
    private List<AppToolContainer> rootContainers = new ArrayList<>();
    private GlobalAppTools tools = new GlobalAppTools(this);
    private AppHistory history = new DefaultAppUndoManager(this);
    private Map<String, ButtonGroup> buttonGroups = new HashMap<>();
    private AppWindow mainWindow;
    private ApplicationBuilderImpl applicationBuilderImpl = new ApplicationBuilderImpl(this);
    private int maxMessageEntries = 1000;
    private int maxLogEntries = 1000;
    private boolean _updateMessages;

    public DefaultApplication() {
        support.add(name);
        support.add(status.readOnly());
        messages.listeners().add(new PropertyListener() {
            @Override
            public void propertyUpdated(PropertyEvent event) {
                switch (event.getAction()) {
                    case ADD: {
                        while (maxMessageEntries > 0 && messages.size() > maxMessageEntries) {
                            messages.remove(0);
                        }
                        break;
                    }
                }
            }
        });
        log.listeners().add(new PropertyListener() {
            @Override
            public void propertyUpdated(PropertyEvent event) {
                switch (event.getAction()) {
                    case ADD: {
                        while (maxLogEntries > 0 && log.size() > maxLogEntries) {
                            messages.remove(0);
                        }
                        break;
                    }
                }
            }
        });
        builder().mainWindowBuilder().get().windowFactory().set(Applications.Windows.Default());
        builder().mainWindowBuilder().get().menuBarFactory().set(Applications.MenuBars.Default());
        builder().mainWindowBuilder().get().statusBarFactory().set(Applications.StatusBars.Default());
        builder().mainWindowBuilder().get().toolBarFactory().set(Applications.ToolBars.Default());
        builder().mainWindowBuilder().get().workspaceFactory().set(Applications.Workspaces.Default());
    }

    @Override
    public PValue<String> name() {
        return name;
    }

    @Override
    public PValue<AppStatus> status() {
        return status.readOnly();
    }

    @Override
    public void start() {
        initImpl();
        status.set(AppStatus.INIT);
        status.set(AppStatus.STARTING);
        startImpl();
        status.set(AppStatus.STARTED);
        log().add(new StringAppMessage(Level.INFO, "Application Started"));
    }

    @Override
    public void shutdown() {
        log().add(new StringAppMessage(Level.INFO, "Application Shutting down"));
        status.set(AppStatus.CLOSING);
        shutdownImpl();
        status.set(AppStatus.CLOSED);
    }

    @Override
    public AppTools tools() {
        return tools;
    }

    public AppWindow mainWindow() {
        if (mainWindow == null) {
            throw new IllegalArgumentException("Menu Bar is not initialized");
        }
        return mainWindow;
    }

    @Override
    public AppNode[] nodes() {
        return rootContainers
                .stream().map(x -> x.rootNode()).toArray(AppNode[]::new);
    }

    @Override
    public ApplicationBuilder builder() {
        return applicationBuilderImpl;
    }

    @Override
    public AppHistory history() {
        return history;
    }

    @Override
    public PList<AppMessage> messages() {
        return messages;
    }

    @Override
    public void updateMessages() {
        if (_updateMessages) {
            return;
        }
        try {
            _updateMessages = true;
            messages.removeAll();
            for (AppMessageProducer messageProducer : messageProducers) {
                messageProducer.produceMessages(this, messages);
            }
        } finally {
            _updateMessages = false;
        }
    }

    @Override
    public WritablePList<AppMessage> log() {
        return log;
    }

    @Override
    public WritablePList<AppMessageProducer> messageProducers() {
        return messageProducers;
    }


//    protected void setStatusBar(AppStatusBar statusBar) {
//        if (status().get().ordinal() >= AppStatus.STARTED.ordinal()) {
//            throw new IllegalArgumentException("Already started");
//        }
//        removeRootContainer(this.statusBar);
//        this.statusBar = statusBar;
//        addRootContainer(this.statusBar);
//    }
//
//    protected void setMenuBar(AppMenuBar menuBar) {
//        if (status().get().ordinal() >= AppStatus.STARTED.ordinal()) {
//            throw new IllegalArgumentException("Already started");
//        }
//        removeRootContainer(this.menuBar);
//        this.menuBar = menuBar;
//        addRootContainer(this.menuBar);
//    }
//
//    protected void setToolBar(AppToolBar toolBar) {
//        if (status().get().ordinal() >= AppStatus.STARTED.ordinal()) {
//            throw new IllegalArgumentException("Already started");
//        }
//        removeRootContainer(this.toolBar);
//        this.toolBar = toolBar;
//        addRootContainer(this.toolBar);
//    }

    public ButtonGroup getButtonGroup(String name) {
        ButtonGroup p = buttonGroups.get(name);
        if (p == null) {
            p = new ButtonGroup();
            buttonGroups.put(name, p);
        }
        return p;
    }

    protected void initImpl() {
        AppWindowBuilder wb = builder().mainWindowBuilder().get();
        if (wb == null) {
            throw new IllegalArgumentException("Missing AppWindowBuilder");
        }
        AppWindow window = wb.createWindow("mainWindow", this);
        setMainWindow(window);
        window.state().listeners().add(event -> {
            AppWindowStateSet s = event.getNewValue();
            if (s.is(AppWindowState.CLOSING)) {
                shutdown();
            }
        });
    }

    protected void shutdownImpl() {
        AppStatus appStatus = status().get();
        if (appStatus != AppStatus.CLOSED) {
            status.set(AppStatus.CLOSED);
        }
        //System.exit(0);
    }

    @Override
    public AppPropertyBinding[] getProperties() {
        return support.getProperties();
    }

    @Override
    public PropertyListeners listeners() {
        return support.listeners();
    }

    protected DefaultApplication setMainWindow(AppWindow mainWindow) {
        if (status().get().ordinal() >= AppStatus.STARTED.ordinal()) {
            throw new IllegalArgumentException("Already started");
        }
        addRootContainer(this.mainWindow);
        this.mainWindow = mainWindow;
        addRootContainer(this.mainWindow);
        return this;
    }

    protected void addRootContainer(AppToolContainer c) {
        if (c != null) {
            rootContainers.add(c);
            tools.addRootContainer(c);
        }
    }

    protected void removeRootContainer(AppToolContainer c) {
        if (c != null) {
            rootContainers.remove(c);
            tools.removeRootContainer(c);
        }
    }

    protected void startImpl() {
        mainWindow().state().add(AppWindowState.OPENED);
    }

    private class ApplicationBuilderImpl implements ApplicationBuilder {
        private final WritablePValue<AppWindowBuilder> windowBuilder = Props.of("windowBuilder").valueOf(AppWindowBuilder.class, new DefaultAppWindowBuilder());

        public ApplicationBuilderImpl(DefaultApplication a) {
            PropertyVeto already_started_veto = new PropertyVeto() {
                @Override
                public void vetoableChange(PropertyEvent e) {
                    if (a.status().get().ordinal() >= AppStatus.STARTED.ordinal()) {
                        throw new IllegalArgumentException("Application is already started");
                    }
                }
            };
            windowBuilder.vetos().add(already_started_veto);
        }

        @Override
        public PValue<AppWindowBuilder> mainWindowBuilder() {
            return windowBuilder.readOnly();
        }
    }
}
