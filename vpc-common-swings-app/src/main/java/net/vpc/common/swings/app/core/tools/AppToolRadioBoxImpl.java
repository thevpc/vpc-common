package net.vpc.common.swings.app.core.tools;

import net.vpc.common.prpbind.Props;
import net.vpc.common.prpbind.WritablePValue;
import net.vpc.common.swings.app.AppToolRadioBox;
import net.vpc.common.swings.app.core.AbstractAppTool;

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
