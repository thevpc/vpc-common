package net.vpc.common.jeep.impl.types;

import net.vpc.common.jeep.*;

public class DefaultJTypeVariable extends AbstractJTypeVariable {
    private String name;
    private JType[] lowerBounds;
    private JType[] upperBounds;
    private JDeclaration declaration;

    public DefaultJTypeVariable(String name, JType[] lowerBounds, JType[] upperBounds, JDeclaration declaration, JTypes types) {
        super(types);
        this.name = name;
        this.lowerBounds = lowerBounds;
        this.upperBounds = upperBounds;
        this.declaration = declaration;
    }

    @Override
    public JDeclaration getDeclaration() {
        return declaration;
    }

    @Override
    public JType[] upperBounds() {
        return upperBounds;
    }

    public JType[] lowerBounds() {
        return lowerBounds;
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public JType replaceParameter(String name, JType param) {
        if(name.equals(this.name)){
            return param;
        }
        return this;
    }

    @Override
    public boolean isInterface() {
        return false;
    }
}
