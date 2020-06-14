package net.vpc.common.jeep;

import net.vpc.common.jeep.impl.functions.JSignature;

public interface JRawType extends JType {
    /**
     * name with generic variables
     * @return name with generic variables
     */
    String gname();

    JType parametrize(JType... parameters);

    //<editor-fold desc="Add/Remove">

    JConstructor addConstructor(JSignature signature, String[] argNames, JInvoke handler, int modifiers, boolean redefine);

    JField addField(String name, JType type, int modifiers, boolean redefine);

    JMethod addMethod(JSignature signature, String[] argNames, JType returnType, JInvoke handler, int modifiers, boolean redefine);
    //</editor-fold>

}