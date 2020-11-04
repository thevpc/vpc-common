package net.thevpc.common.lanterna;


import com.googlecode.lanterna.gui2.*;

import java.util.ArrayList;
import java.util.List;

public class TabbedPane extends PanelExt {
    private PanelExt toolbar = new PanelExt(new GridBagLayout());
    private PanelExt container = new PanelExt(new GridBagLayout());
    private List<Component> components=new ArrayList<>();
    private List<Button> buttons=new ArrayList<>();
    private List<String> names=new ArrayList<>();
    private boolean combo=false;
    private ComboBox comboList;
    private int current=0;

    public TabbedPane(boolean combo) {
        this.combo=combo;
        setLayoutManager(new GridBagLayout());
        if(combo){
            comboList = new ComboBox();
            comboList.addListener(new ComboBox.Listener() {
                @Override
                public void onSelectionChanged(int selectedIndex, int previousSelection) {
                    setSelected(selectedIndex);
                }
            });
            toolbar.addComponent(comboList.setLayoutData(new GridCell(">")));
        }else{
//            toolbar.addComponent(new Label("."));
        }
        this.addComponent(toolbar.setLayoutData(new GridCell(">."))
//                .withBorder(Borders.singleLine())
        );
        this.addComponent(container.setLayoutData(new GridCell("<+*>.")));
        invalidate();
    }

    public void addTab(String name, final Component component){
        components.add(component);
        final int index = components.size() - 1;
        names.add(name);
        if(combo){
            comboList.addItem(name);
        }else {
            Button b = new Button(name, new Runnable() {
                @Override
                public void run() {
                    setSelected(index);
                }
            });
            buttons.add(b);

            toolbar.addComponent(b.setLayoutData(new GridCell(">")));
        }
        invalidate();
        setSelected(index);
    }

    public void setSelected(int index){
        current=index;
        container.removeAllComponents();
        container.addComponent(components.get(index).setLayoutData(new GridCell("<+*>.")).withBorder(Borders.singleLine(names.get(index))));
        invalidate();
    }
    public int size(){
        return components.size();
    }

    public Component getTabComponent(int index){
        return components.get(index);
    }
}
