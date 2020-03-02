package net.vpc.common.swings.app.core;

import net.vpc.common.prpbind.Props;
import net.vpc.common.prpbind.WritablePValue;
import net.vpc.common.swings.app.AppTool;

import javax.swing.*;

public class AbstractAppTool implements AppTool {
    private WritablePValue<Boolean> enabled = Props.of("enabled").valueOf( Boolean.class,true);
    private WritablePValue<Boolean> visible = Props.of("visible").valueOf( Boolean.class,true);
    private WritablePValue<String> title = Props.of("title").valueOf( String.class,"");
    private WritablePValue<ImageIcon> smallIcon = Props.of("smallIcon").valueOf( ImageIcon.class, null);
    private WritablePValue<ImageIcon> largeIcon = Props.of("largeIcon").valueOf( ImageIcon.class, null);
    private String id;

    public AbstractAppTool(String id) {
        this.id = id;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public WritablePValue<ImageIcon> smallIcon() {
        return smallIcon;
    }

    @Override
    public WritablePValue<ImageIcon> largeIcon() {
        return largeIcon;
    }

    @Override
    public WritablePValue<Boolean> enabled() {
        return enabled;
    }

    @Override
    public WritablePValue<Boolean> visible() {
        return visible;
    }

    @Override
    public WritablePValue<String> title() {
        return title;
    }
}
