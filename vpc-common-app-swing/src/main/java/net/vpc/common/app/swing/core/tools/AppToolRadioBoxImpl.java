package net.vpc.common.app.swing.core.tools;

import net.vpc.common.props.Props;
import net.vpc.common.props.WritablePValue;
import net.vpc.common.app.AppToolRadioBox;
import net.vpc.common.app.AbstractAppTool;

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
