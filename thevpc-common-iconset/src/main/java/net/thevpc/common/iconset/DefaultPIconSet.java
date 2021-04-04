/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.iconset;

import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;

import net.thevpc.common.props.PropertyEvent;
import net.thevpc.common.props.PropertyListener;
import net.thevpc.common.props.Props;
import net.thevpc.common.props.WritableValue;
import net.thevpc.common.props.ObservableMap;
import net.thevpc.common.props.ObservableValue;

/**
 *
 * @author thevpc
 */
public class DefaultPIconSet implements PIconSet {

    protected WritableValue<String> id = Props.of("id").valueOf(String.class, null);
    private Map<String, WritableValue<ImageIcon>> icons = new HashMap<>();
    private Map<String, IconSet> iconSets;

    public DefaultPIconSet(Map<String, IconSet> iconSets0) {
        this.iconSets = iconSets0;
        id.listeners().add(new PropertyListener() {
            @Override
            public void propertyUpdated(PropertyEvent event) {
                IconSet s = iconSets.get((String) event.getNewValue());
                for (Map.Entry<String, WritableValue<ImageIcon>> entry : icons.entrySet()) {
                    entry.getValue().set(s == null ? null : s.getIcon(entry.getKey()));
                }
            }
        });
    }

    @Override
    public WritableValue<String> id() {
        return id;
    }

    @Override
    public ObservableValue<ImageIcon> icon(String id) {
        WritableValue<ImageIcon> i = icons.get(id);
        if (i == null) {
            IconSet s = iconSets.get(this.id.get());
            i = Props.of("icon-" + id).valueOf(ImageIcon.class, s == null ? null : s.getIcon(id));
            icons.put(id, i);
        }
        return i;
    }

}
