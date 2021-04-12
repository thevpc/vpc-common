/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.iconset;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;
import net.thevpc.common.props.ObservableMap;
import net.thevpc.common.props.ObservableValue;
import net.thevpc.common.props.PropertyEvent;
import net.thevpc.common.props.PropertyUpdate;
import net.thevpc.common.props.Props;
import net.thevpc.common.props.WritableLiMap;
import net.thevpc.common.props.WritableLiMapAdapter;
import net.thevpc.common.props.WritableValue;
import net.thevpc.common.props.impl.PropertyListenersImpl;

/**
 *
 * @author vpc
 */
public class DefaultIconsets extends WritableLiMapAdapter<String, IconSet> implements IconSets {

    private WritableLiMap<String, IconSet> base;
    protected WritableValue<String> id = Props.of("id").valueOf(String.class, null);
    protected WritableValue<AppIconResolver> resolver = Props.of("id").valueOf(AppIconResolver.class, null);
    private Map<String, WritableValue<ImageIcon>> icons = new HashMap<>();
    private WritableValue<IconSetConfig> config = Props.of("id").valueOf(IconSetConfig.class, IconSetConfig.of(16));

    public DefaultIconsets(String name) {
        this.base = Props.of(name).lmapOf(String.class, IconSet.class, x -> x.getId());
        id.listeners().add(this::updateAllLoadedIcons);
        config.listeners().add(this::updateAllLoadedIcons);
    }

    @Override
    protected ObservableMap<String, IconSet> getObservableMapBase() {
        return base;
    }

    private void updateAllLoadedIcons(PropertyEvent event) {
        IconSet s = this.get(id().get());
        if (s == null) {
            for (Map.Entry<String, WritableValue<ImageIcon>> entry : icons.entrySet()) {
                entry.getValue().set(null);
            }
        } else {
            IconSetConfig cnf = config.get();
            for (Map.Entry<String, WritableValue<ImageIcon>> entry : icons.entrySet()) {
                ImageIcon i = s.getIcon(entry.getKey(), cnf);
                if(i!=null){
                    int iw = i.getIconHeight();
                    int cw=cnf.getWidth();
                    if(iw!=cw){
                        System.out.println("problem detected");
                        i = s.getIcon(entry.getKey(), cnf);
                    }
                }
                entry.getValue().set(i);
            }

        }
        ((PropertyListenersImpl) this.listeners()).firePropertyUpdated(new PropertyEvent(this, -1, false, true, "/", PropertyUpdate.UPDATE, "activeIconSetChanged"));
    }

    @Override
    public WritableValue<IconSetConfig> config() {
        return config;
    }

    @Override
    public WritableValue<String> id() {
        return id;
    }

    @Override
    public ObservableValue<ImageIcon> icon(String id) {
        WritableValue<ImageIcon> i = icons.get(id);
        if (i == null) {
            IconSet s = this.get(this.id.get());
            i = Props.of("icon-" + id).valueOf(ImageIcon.class, s == null ? null : s.getIcon(id, config.get()));
            icons.put(id, i);
        }
        return i;
    }

    @Override
    public WritableValue<AppIconResolver> resolver() {
        return resolver;
    }

    @Override
    public String iconIdForFile(File f, boolean selected, boolean expanded) {
        AppIconResolver r = resolver.get();
        if (r == null) {
            return null;
        }
        return r.iconIdForFile(f, selected, expanded);
    }

    @Override
    public String iconIdForFileName(String f, boolean selected, boolean expanded) {
        AppIconResolver r = resolver.get();
        if (r == null) {
            return null;
        }
        return r.iconIdForFileName(f, selected, expanded);
    }

    @Override
    public ObservableValue<ImageIcon> iconForFile(File f, boolean selected, boolean expanded) {
        return icon(iconIdForFile(f, selected, expanded));
    }

    @Override
    public ObservableValue<ImageIcon> iconForFileName(String f, boolean selected, boolean expanded) {
        return icon(iconIdForFileName(f, selected, expanded));
    }
}
