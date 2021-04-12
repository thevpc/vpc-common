/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.swing.plaf;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.IntelliJTheme;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;

/**
 *
 * @author thevpc
 */
public class UIPlafManager {

    public static UIPlafManager INSTANCE = new UIPlafManager();
    private LinkedHashMap<String, UIPlaf> installed = new LinkedHashMap<>();
    private List<UIPlafListener> listeners = new ArrayList<>();
    private UIPlaf current;
    private float appFontRelative = Float.NaN;
    private float appFontAbsolute = Float.NaN;

    public UIPlafManager() {
        UIManager.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("lookAndFeel".equals(evt.getPropertyName())) {
                    onPreparePlaf();
                }
            }
        }
        );
        for (UIManager.LookAndFeelInfo p : UIManager.getInstalledLookAndFeels()) {
            switch (p.getName()) {
                case "Metal": {
                    add(new UIPlaf("Metal", "Metal", true, false, true, false) {
                        @Override
                        public void apply() throws Exception {
                            MetalLookAndFeel.setCurrentTheme(new OceanTheme());
                            UIManager.setLookAndFeel(new MetalLookAndFeel());
                        }
                    });
                    break;
                }
                case "Nimbus": {
                    add(new UIPlaf("Nimbus", "Nimbus", true, false, false, false) {
                        @Override
                        public void apply() throws Exception {
                            UIManager.setLookAndFeel(p.getClassName());
                        }
                    });
                    break;
                }
                case "CDE/Motif": {
                    add(new UIPlaf("CDE_Motif", "CDE/Motif", true, true, false, false) {
                        @Override
                        public void apply() throws Exception {
                            UIManager.setLookAndFeel(p.getClassName());
                        }
                    });
                    break;
                }
                case "GTK+": {
                    add(new UIPlaf("GTK+", "GTK+", true, false, true, false) {
                        @Override
                        public void apply() throws Exception {
                            UIManager.setLookAndFeel(p.getClassName());
                        }
                    });
                    break;
                }
                default: {
                    add(new UIPlaf(p.getName(), p.getName(), true, false, true, false) {
                        @Override
                        public void apply() throws Exception {
                            UIManager.setLookAndFeel(p.getClassName());
                        }
                    });
                }
            }
        }

        add(new UIPlaf("FlatLight", "Flat Light", false, false, true, false) {
            @Override
            public void apply() throws Exception {
                UIManager.setLookAndFeel(new FlatLightLaf());
            }
        });
        add(new UIPlaf("FlatDark", "Flat Dark", false, true, false, false) {
            @Override
            public void apply() throws Exception {
                UIManager.setLookAndFeel(new FlatDarkLaf());
            }
        });
        add(new UIPlaf("FlatIntellij", "Flat Intellij", false, false, true, false) {
            @Override
            public void apply() throws Exception {
                UIManager.setLookAndFeel(new FlatIntelliJLaf());
            }
        });
        add(new UIPlaf("FlatDarcula", "Flat Darcula", false, true, false, false) {
            @Override
            public void apply() throws Exception {
                UIManager.setLookAndFeel(new FlatDarculaLaf());
            }
        });

        for (String lightTheme : new String[]{
            "Gray",
            "GitHub_Contrast",
            "GitHub",
            "Arc_Theme_Orange",
            "Arc_Theme",
            "Cyan",
            "Atom_One_Light_Contrast",
            "Atom_One_Light",
            "LightFlatTheme",
            "Light_Owl_Contrast",
            "Light_Owl",
            "Light",
            "Material_Lighter_Contrast",
            "Material_Lighter",
            "Solarized_Light_Contrast",
            "Solarized_Light",
            "Solarized_Light_Theme"
        }) {
            boolean dark = false;
            boolean light = true;
            boolean contrast = lightTheme.toLowerCase().contains("contrast");
            String name = nameFromId(lightTheme);
            add(new UIPlaf("Flat-" + lightTheme, name, false, dark, light, contrast) {
                @Override
                public void apply() throws Exception {
                    IntelliJTheme.install(ClassLoader.getSystemClassLoader().getResourceAsStream(
                            "net/thevpc/swings/plaf/intellijthemes/" + lightTheme + ".theme.json"
                    )
                    );
                }
            });
        }

        for (String theme : new String[]{
            "MaterialTheme",
            "HighContrast",
            "Material_Oceanic_Contrast",
            "Material_Oceanic",
            "Material_Palenight_Contrast",
            "Material_Palenight",
            "Monocai",
            "Monokai_Pro_Contrast",
            "Monokai_Pro",
            "Spacegray",
            "Gruvbox_Theme",
            "Hiberbee",
            "DarkFlatTheme",
            "DarkPurple",
            "Arc_Dark",
            "Arc_Dark_Contrast",
            "Atom_One_Dark_Contrast",
            "Atom_One_Dark",
            "Dracula_Contrast",
            "Dracula",
            "Dracula2",
            "Material_Darker_Contrast",
            "Material_Darker",
            "Material_Deep_Ocean_Contrast",
            "Material_Deep_Ocean",
            "Night_Owl_Contrast",
            "Night_Owl",
            "Nord",
            "One_Dark",
            "Solarized_Dark_Contrast",
            "Solarized_Dark",
            "solarized_Dark_Theme",}) {
            boolean dark = true;
            boolean light = false;
            boolean contrast = theme.toLowerCase().contains("contrast");
            String name = nameFromId(theme);
            add(new UIPlaf("Flat-" + theme, name, false, dark, light, contrast) {
                @Override
                public void apply() throws Exception {
                    IntelliJTheme.install(ClassLoader.getSystemClassLoader().getResourceAsStream(
                            "net/thevpc/swings/plaf/intellijthemes/" + theme + ".theme.json"
                    )
                    );
                }
            });
        }
    }

    protected String nameFromId(String a) {
        StringBuilder sb = new StringBuilder();
        char[] charArray = a.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            char c = charArray[i];
            if (c == '_' || c == '-') {
                sb.append(' ');
            } else if (i > 0 && i < charArray.length - 1 && Character.isLowerCase(charArray[i - 1]) && Character.isUpperCase(charArray[i])) {
                sb.append(' ');
                sb.append(c);
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public final List<UIPlaf> items() {
        ArrayList<UIPlaf> s = new ArrayList<>(installed.values());
        s.sort(new Comparator<UIPlaf>() {
            @Override
            public int compare(UIPlaf o1, UIPlaf o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        return s;
    }

    public final void addListener(UIPlafListener listener) {
        listeners.add(listener);
    }

    public final static UIPlafManager getCurrentManager() {
        UIPlafManager a = (UIPlafManager) UIManager.get(UIPlafManager.class.getName());
        return a != null ? a : INSTANCE;
    }

    public final UIPlaf getCurrent() {
        return current;
    }

    public final void apply(String name) {
        apply(installed.get(name));
    }

    public final void apply(UIPlaf a) {
        if (a != null) {
            UIPlaf old = current;
            current = a;
            try {
                a.apply();
            } catch (Exception ex) {
                throw new IllegalArgumentException(ex);
            }
            UIManager.put(UIPlafManager.class.getName() + ".plaf", a);
            UIManager.put(UIPlafManager.class.getName() + ".dark", a.isDark());
            UIManager.put(UIPlafManager.class.getName() + ".contrast", a.isContrast());
            UIManager.put(UIPlafManager.class.getName() + ".system", a.isSystem());
            UIManager.put(UIPlafManager.class.getName() + ".light", a.isLight());
            UIManager.put(UIPlafManager.class.getName() + ".name", a.getName());
            for (UIPlafListener listener : listeners) {
                listener.plafChanged(current);
            }
        }
    }

    public boolean resizeRelativeFonts(float f) {
        if (f > 0 && f < 4) {
            this.appFontRelative = Float.NaN;
            this.appFontAbsolute = f;
            apply(current);
            return true;
        }
        return false;
    }

    public boolean resizeAbsoluteFonts(float f) {
        if (f > 0 && f <= 128) {
            this.appFontRelative = Float.NaN;
            this.appFontAbsolute = f;
            apply(current);
            return true;
        }
        return false;
    }

    private void onPreparePlaf() {
        if (!Float.isNaN(appFontRelative)) {
            resizeAllUIFontsRelative(appFontRelative);
        } else if (!Float.isNaN(appFontAbsolute)) {
            resizeAllUIFontsAbsolute(appFontAbsolute);
        }
    }

    public final void add(UIPlaf a) {
        installed.put(a.getId(), a);
    }

    private static void resizeAllUIFontsAbsolute(float absolute) {
        UIDefaults defaults = UIManager.getDefaults();
        int i = 0;
        for (Enumeration e = defaults.keys(); e.hasMoreElements(); i++) {
            Object key = e.nextElement();
            Object value = defaults.get(key);
            if (value instanceof Font) {
                Font font = (Font) value;
                if (value instanceof FontUIResource) {
                    defaults.put(key, font.deriveFont(absolute));
                } else {
                    defaults.put(key, font.deriveFont(absolute));
                }
            }
        }
    }

    private static void resizeAllUIFontsRelative(float factor) {
        UIDefaults defaults = UIManager.getDefaults();
        int i = 0;
        for (Enumeration e = defaults.keys(); e.hasMoreElements(); i++) {
            Object key = e.nextElement();
            Object value = defaults.get(key);
            if (value instanceof Font) {
                Font font = (Font) value;
                if (value instanceof FontUIResource) {
                    defaults.put(key, resizeFont(font, factor));
                } else {
                    defaults.put(key, resizeFont(font, factor));
                }
            }
        }
    }

    private static Font resizeFont(Font f, float factor) {
        if (factor <= 0 || factor == 1) {
            if (f instanceof Font2) {
                return f.deriveFont(((Font2) f).initialSize);
            } else {
                return f;
            }
        } else {
            if (f instanceof Font2) {
                int i = ((Font2) f).initialSize;
                int newSize = Math.round(i * factor);
                return new Font2(f.deriveFont((float) newSize), i);
            } else {
                int i = f.getSize();
                int newSize = Math.round(i * factor);
                return new Font2(f.deriveFont((float) newSize), i);
            }
        }
    }

    private static class Font2 extends Font {

        int initialSize;

        public Font2(String string, int i, int i1, int initialSize) {
            super(string, i, i1);
            this.initialSize = initialSize;
        }

        public Font2(Font font, int initialSize) {
            super(font);
            this.initialSize = initialSize;
        }

    }

    public float getAppFontRelative() {
        return appFontRelative;
    }

}
