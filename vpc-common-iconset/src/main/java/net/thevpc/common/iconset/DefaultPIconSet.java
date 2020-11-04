/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.iconset;

import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;

import net.thevpc.common.props.PMap;
import net.thevpc.common.props.PValue;
import net.thevpc.common.props.PropertyEvent;
import net.thevpc.common.props.PropertyListener;
import net.thevpc.common.props.Props;
import net.thevpc.common.props.WritablePValue;

/**
 *
 * @author vpc
 */
public class DefaultPIconSet implements PIconSet {

    protected WritablePValue<String> id = Props.of("id").valueOf(String.class, null);
    private Map<String, WritablePValue<ImageIcon>> icons = new HashMap<>();
    private PMap<String, IconSet> iconSets;

    public DefaultPIconSet(PMap<String, IconSet> iconSets0) {
        this.iconSets = iconSets0;
        id.listeners().add(new PropertyListener() {
            @Override
            public void propertyUpdated(PropertyEvent event) {
                IconSet s = iconSets.get((String) event.getNewValue());
                for (Map.Entry<String, WritablePValue<ImageIcon>> entry : icons.entrySet()) {
                    entry.getValue().set(s == null ? null : s.getIcon(entry.getKey()));
                }
            }
        });
    }

    @Override
    public WritablePValue<String> id() {
        return id;
    }

    @Override
    public PValue<ImageIcon> icon(String id) {
        WritablePValue<ImageIcon> i = icons.get(id);
        if (i == null) {
            IconSet s = iconSets.get(this.id.get());
            i = Props.of("icon-" + id).valueOf(ImageIcon.class, s == null ? null : s.getIcon(id));
            icons.put(id, i);
        }
        return i;
    }

}
