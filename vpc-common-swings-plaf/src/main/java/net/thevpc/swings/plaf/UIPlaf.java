/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.swings.plaf;

import java.util.Objects;

/**
 *
 * @author vpc
 */
public abstract class UIPlaf {
    
    private final String name;
    private final boolean system;
    private final boolean dark;
    private final boolean light;
    private final boolean contrast;

    public UIPlaf(String name, boolean system, boolean dark, boolean light, boolean contrast) {
        this.name = name;
        this.system = system;
        this.dark = dark;
        this.light = light;
        this.contrast = contrast;
    }

    public boolean isContrast() {
        return contrast;
    }

    public boolean isSystem() {
        return system;
    }

    public boolean isDark() {
        return dark;
    }

    public boolean isLight() {
        return light;
    }

    public String getName() {
        return name;
    }

    public abstract void apply() throws Exception;

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UIPlaf other = (UIPlaf) obj;
        if (this.system != other.system) {
            return false;
        }
        if (this.dark != other.dark) {
            return false;
        }
        if (this.light != other.light) {
            return false;
        }
        if (this.contrast != other.contrast) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }
    
    
}
