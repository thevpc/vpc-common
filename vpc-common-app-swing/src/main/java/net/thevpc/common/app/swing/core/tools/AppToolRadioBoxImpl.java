package net.thevpc.common.app.swing.core.tools;

import net.thevpc.common.app.AbstractAppTool;
import net.thevpc.common.app.AppToolRadioBox;
import net.thevpc.common.props.Props;
import net.thevpc.common.props.WritablePValue;

public class AppToolRadioBoxImpl extends AbstractAppTool implements AppToolRadioBox {
    private String id;
    private WritablePValue<String> group = Props.of("group").valueOf( String.class, null);
    private WritablePValue<Boolean> selected = Props.of("selected").valueOf( Boolean.class,false);


    public AppToolRadioBoxImpl(String id, String group) {
        super(id);
        this.id = id;
    }


    @Override
    public String id() {
        return id;
    }

    @Override
    public WritablePValue<String> group() {
        return group;
    }

    @Override
    public WritablePValue<Boolean> selected() {
        return selected;
    }
}