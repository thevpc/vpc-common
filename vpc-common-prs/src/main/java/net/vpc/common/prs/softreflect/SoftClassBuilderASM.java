/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.prs.softreflect;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

/**
 *
 * @author vpc
 */
public class SoftClassBuilderASM implements SoftClassBuilder {

//    public static void main(String[] args) {
//        String cls = "/home/vpc/xprojects/apps/dbclient/plugins/tool/tool-jstsql/build/classes/net/vpc/app/dbclient/plugin/sql/jstsql/actions/JSTSqlSessionActionsFactory.class";
//        try {
//            FileInputStream i = new FileInputStream(cls);
//            SoftClassBuilderASM z = new SoftClassBuilderASM();
//            SoftClass cc = z.buildClass(i);
//            System.out.println(cc);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }

    public SoftClass buildClass(InputStream stream) throws IOException {
        ClassReader cr = new ClassReader(stream);
        MyClassVisitor m = new MyClassVisitor();
        cr.accept(m, 0);
        return m.clazz;
    }

    class MyAnnotationVisitor implements AnnotationVisitor {

        SoftAnnotationImpl annotation = new SoftAnnotationImpl();
        List<SoftAnnotationAttribute> list = new ArrayList<SoftAnnotationAttribute>();

        public MyAnnotationVisitor(String desc) {
            annotation.setName(mapType2(desc));
        }
        
        public void visit(String name, Object value) {
            SoftAnnotationAttributeImpl a = new SoftAnnotationAttributeImpl();
            a.setName(name);
            a.setValue(value);
            list.add(a);
        }

        public void visitEnum(String name, String desc, String value) {
            //ignore
        }

        public AnnotationVisitor visitAnnotation(String name, String desc) {
            //ignore
            return null;
        }

        public AnnotationVisitor visitArray(String name) {
            //ignore
            return null;
        }

        public void visitEnd() {
            annotation.setAtributes(list.toArray(new SoftAnnotationAttribute[list.size()]));
        }

        public SoftAnnotationImpl getAnnotation() {
            return annotation;
        }
    }

    class MyFieldVisitor implements FieldVisitor {

        SoftFieldImpl field = new SoftFieldImpl();
        List<SoftAnnotation> annotations = new ArrayList<SoftAnnotation>();

        public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
            return new MyAnnotationVisitor(desc) {

                @Override
                public void visitEnd() {
                    super.visitEnd();
                    annotations.add(annotation);
                }
            };
        }

        public void visitAttribute(Attribute attr) {
        }

        public void visitEnd() {
            field.setAnnotations(annotations.toArray(new SoftAnnotation[annotations.size()]));
        }
    }

    class MyMethodVisitor implements MethodVisitor {

        SoftMethodImpl method = new SoftMethodImpl();
        List<SoftAnnotation> annotations = new ArrayList<SoftAnnotation>();

        public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
            return new MyAnnotationVisitor(desc) {

                @Override
                public void visitEnd() {
                    super.visitEnd();
                    annotations.add(annotation);
                }
            };
        }

        public void visitAttribute(Attribute attr) {
        }

        public void visitEnd() {
            method.setAnnotations(annotations.toArray(new SoftAnnotation[annotations.size()]));
        }

        public AnnotationVisitor visitAnnotationDefault() {
            return null;
        }

        public AnnotationVisitor visitParameterAnnotation(int parameter, String desc, boolean visible) {
            return null;
        }

        public void visitCode() {
            //
        }

        public void visitFieldInsn(int opcode, String owner, String name, String desc) {
            //
        }

        public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
            //
        }

        public void visitIincInsn(int var, int increment) {
            //
        }

        public void visitInsn(int opcode) {
            //
        }

        public void visitIntInsn(int opcode, int operand) {
            //
        }

//        public void visitInvokeDynamicInsn(String name, String desc, MethodHandle bsm, Object... bsmArgs) {
//            //
//        }

        public void visitJumpInsn(int opcode, Label label) {
            //
        }

        public void visitLabel(Label label) {
            //
        }

        public void visitLdcInsn(Object cst) {
            //
        }

        public void visitLineNumber(int line, Label start) {
            //
        }

        public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
            //
        }

        public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
            //
        }

        public void visitMaxs(int maxStack, int maxLocals) {
            //
        }

        public void visitMethodInsn(int opcode, String owner, String name, String desc) {
            //
        }

        public void visitMultiANewArrayInsn(String desc, int dims) {
            //
        }

        public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
            //
        }

        public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
            //
        }

        public void visitTypeInsn(int opcode, String type) {
            //
        }

        public void visitVarInsn(int opcode, int var) {
            //
        }
    }

    class MyClassVisitor implements ClassVisitor {

        SoftClassImpl clazz = new SoftClassImpl();
        List<SoftField> fields = new ArrayList<SoftField>();
        List<SoftMethod> methods = new ArrayList<SoftMethod>();
        List<SoftAnnotation> annotations = new ArrayList<SoftAnnotation>();

        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
            clazz.setName(mapType2(name));
            clazz.setSuperclass(mapType2(superName));
            String[] interfaces2 = new String[interfaces.length];
            for (int i = 0; i < interfaces2.length; i++) {
                interfaces2[i] = mapType2(interfaces[i]);
            }
            clazz.setInterfaces(interfaces2);
            clazz.setModifiers(access);
            clazz.setInterface(Modifier.isInterface(access));
        }

        public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
            MyAnnotationVisitor a=new MyAnnotationVisitor(desc) {

                @Override
                public void visitEnd() {
                    super.visitEnd();
                    annotations.add(annotation);
                }
            };
            return a;
        }

        public void visitAttribute(Attribute attr) {
        }

        public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
            MyFieldVisitor fieldVisitor = new MyFieldVisitor() {

                public void visitEnd() {
                    super.visitEnd();
                    fields.add(field);
                }
            };
            final SoftFieldImpl f = fieldVisitor.field;
            f.setModifiers(access);
            f.setName(name);
            f.setDeclaringClassName(clazz.getName());
            return fieldVisitor;
        }

        public void visitInnerClass(String name, String outerName, String innerName, int access) {
        }

        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            MyMethodVisitor methodVisitor = new MyMethodVisitor() {

                @Override
                public void visitEnd() {
                    super.visitEnd();
                    if (method.getName().equals("<init>")) {
                        if (method.getParameterTypes().length == 0) {
                            clazz.setBeanConstructor(true);
                        }
                    } else if (method.getName().equals("<cinit>")) {
                    } else {
                        methods.add(method);
                    }
                }
            };
            SoftMethodImpl method = methodVisitor.method;
            method.setDeclaringClass(clazz);
            method.setDeclaringClassName(clazz.getName());
            method.setModifiers(access);
            method.setName(name);
            int indexOfEndPar = desc.indexOf(')');
            String returnType = mapType2(desc.substring(indexOfEndPar + 1));
//        if(!returnType.equals("V")){
            //returnType = JavaByteCodeUtils.mapType2(returnType);
//        }
            //method.setType(returnType);
            String[] types = mapMethodTypes(desc.substring(1, indexOfEndPar));
//            if(Modifier.isNative(access) || Modifier.isInterface(clazz.getModifiers())){
            int max = 0;
            int types_length = types.length;
            String[] methodParams = new String[types_length];
            for (int i = 0; i < types_length; i++) {
                String type = types[i];
                String s = /*JavaByteCodeUtils.mapType*/ (type);
                methodParams[i] = s;
            }
            method.setParameterTypes(methodParams);
            method.setReturnType(returnType);
            return methodVisitor;
        }

        public void visitOuterClass(String owner, String name, String desc) {
        }

        public void visitSource(String source, String debug) {
        }

        public void visitEnd() {
            clazz.setDeclaredFields(fields.toArray(new SoftField[fields.size()]));
            clazz.setDeclaredMethods(methods.toArray(new SoftMethod[methods.size()]));
            clazz.setAnnotations(annotations.toArray(new SoftAnnotation[annotations.size()]));
        }
    }

    public String[] mapMethodTypes(String desc) {
        ArrayList<String> all = new ArrayList<String>();
        int i = 0;
        StringBuilder sb2 = new StringBuilder();
        char[] chars = desc.toCharArray();
        int desc_length = chars.length;
        while (i < desc_length) {
            switch (chars[i]) {
                case 'L': {
                    WHILE2:
                    while (i < desc_length) {
                        switch (chars[i]) {
                            case ';': {
                                sb2.append(";");
                                String x = sb2.toString();
//                                x = x.replace(STRING_JAVA_CLASS_NAME_ENCODED, "G");
//                                x = x.replace(OBJECT_JAVA_CLASS_NAME_ENCODED, "O");
                                all.add(mapType2(x));
                                sb2.delete(0, sb2.length());
                                break WHILE2;
                            }
                            default: {
                                sb2.append(chars[i]);
                                break;
                            }
                        }
                        i++;
                    }
                    break;
                }
                case '[': {
                    sb2.append('[');
                    break;
                }
                default: {
                    sb2.append(chars[i]);
                    all.add(mapType2(sb2.toString()));
                    sb2.delete(0, sb2.length());
                }
            }
            i++;
        }
        return all.toArray(new String[all.size()]);
    }

    public String mapType2(String type) {
        char[] all = type.toCharArray();
        int length = all.length;
        int array = 0;
        while (all[array] == '[') {
            array++;
        }
        if (array > 0) {
            return type;
//            if (all[length - 1] == ';') {
//                int count = length - 1;
//                for (int i = array + 1; i < count; i++) {
//                    if (all[i] == '/') {
//                        all[i] = '.';
//                    }
//                }
//                return new String(all, array + 1, count - array - 1);
//            } else {
//                //int typeLength = length - array;
//                //String name2 = new String(all, array, typeLength);
//                //return KObjectType.resolveByCode(name2).getName();
//                return type;
//            }
        } else if (all[length - 1] == ';') {
            int count = length - 1;
            for (int i = 1; i < count; i++) {
                if (all[i] == '/') {
                    all[i] = '.';
                }
            }
            return new String(all, 1, count - 1);
        } else if (type.length() == 1) {
            switch (type.charAt(0)) {
                case 'I':
                    return "int";
                case 'J':
                    return "long";
                case 'F':
                    return "float";
                case 'D':
                    return "double";
                case 'C':
                    return "char";
                case 'B':
                    return "byte";
                case 'Z':
                    return "boolean";
                case 'V':
                    return "void";
                case 'S':
                    return "short";
            }
            throw new IllegalArgumentException("???");
        } else {
            int count = length;
            for (int i = 0; i < count; i++) {
                if (all[i] == '/') {
                    all[i] = '.';
                }
            }
            return new String(all, 0, count);
        }
    }
}
