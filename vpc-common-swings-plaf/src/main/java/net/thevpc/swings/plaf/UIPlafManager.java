/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.swings.plaf;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.IntelliJTheme;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import javax.swing.UIManager;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;

/**
 *
 * @author vpc
 */
public class UIPlafManager {
    public static UIPlafManager INSTANCE = new UIPlafManager();
    private LinkedHashMap<String, UIPlaf> installed = new LinkedHashMap<>();
    private UIPlaf current;

    public UIPlafManager() {
        for (UIManager.LookAndFeelInfo p : UIManager.getInstalledLookAndFeels()) {
            switch (p.getName()) {
                case "Metal": {
                    add(new UIPlaf("Metal", true, false, true, false) {
                        @Override
                        public void apply() throws Exception {
                            MetalLookAndFeel.setCurrentTheme(new OceanTheme());
                            UIManager.setLookAndFeel(new MetalLookAndFeel());
                        }
                    });
                    break;
                }
                case "Nimbus": {
                    add(new UIPlaf("Nimbus", true, false, false, false) {
                        @Override
                        public void apply() throws Exception {
                            UIManager.setLookAndFeel(p.getClassName());
                        }
                    });
                    break;
                }
                case "CDE/Motif": {
                    add(new UIPlaf("CDE_Motif", true, true, false, false) {
                        @Override
                        public void apply() throws Exception {
                            UIManager.setLookAndFeel(p.getClassName());
                        }
                    });
                    break;
                }
                case "GTK+": {
                    add(new UIPlaf("GTK+", true, false, true, false) {
                        @Override
                        public void apply() throws Exception {
                            UIManager.setLookAndFeel(p.getClassName());
                        }
                    });
                    break;
                }
                default: {
                    add(new UIPlaf(p.getName(), true, false, true, false) {
                        @Override
                        public void apply() throws Exception {
                            UIManager.setLookAndFeel(p.getClassName());
                        }
                    });
                }
            }
        }

        add(new UIPlaf("FlatLight", false, false, true, false) {
            @Override
            public void apply() throws Exception {
                UIManager.setLookAndFeel(new FlatLightLaf());
            }
        });
        add(new UIPlaf("FlatDark", false, true, false, false) {
            @Override
            public void apply() throws Exception {
                UIManager.setLookAndFeel(new FlatDarkLaf());
            }
        });
        add(new UIPlaf("FlatIntellij", false, false, true, false) {
            @Override
            public void apply() throws Exception {
                UIManager.setLookAndFeel(new FlatIntelliJLaf());
            }
        });
        add(new UIPlaf("FlatDarcula", false, true, false, false) {
            @Override
            public void apply() throws Exception {
                UIManager.setLookAndFeel(new FlatDarculaLaf());
            }
        });

        
        for (String theme : new String[]{
            "HighContrast",
            "Material_Oceanic_Contrast",
            "Material_Oceanic",
            "Material_Palenight_Contrast",
            "Material_Palenight",
            "MaterialTheme",
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
            boolean contrast = theme.toLowerCase().contains("contrast");
            add(new UIPlaf("Flat-" + theme, false, dark, light, contrast) {
                @Override
                public void apply() throws Exception {
                    IntelliJTheme.install(ClassLoader.getSystemClassLoader().getResourceAsStream(
                            "net/thevpc/swings/plaf/intellijthemes/" + theme + ".theme.json"
                    )
                    );
                }
            });
        }

        for (String theme : new String[]{
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
            add(new UIPlaf("Flat-" + theme, false, dark, light, contrast) {
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

    public final static UIPlafManager getCurrentManager() {
        return (UIPlafManager) UIManager.get(UIPlafManager.class.getName());
    }

    public final UIPlaf getCurrent() {
        return current;
    }

    public final void apply(String name) {
        UIPlaf a = installed.get(name);
        if (a != null) {
            current=a;
            try {
                a.apply();
            } catch (Exception ex) {
                throw new IllegalArgumentException(ex);
            }
            UIManager.put(UIPlafManager.class.getName(), a);
            UIManager.put(UIPlafManager.class.getName() + ".dark", a.isDark());
            UIManager.put(UIPlafManager.class.getName() + ".contrast", a.isContrast());
            UIManager.put(UIPlafManager.class.getName() + ".system", a.isSystem());
            UIManager.put(UIPlafManager.class.getName() + ".light", a.isLight());
            UIManager.put(UIPlafManager.class.getName() + ".name", a.getName());
        }
    }


    public final void add(UIPlaf a) {
        installed.put(a.getName(), a);
    }

}
