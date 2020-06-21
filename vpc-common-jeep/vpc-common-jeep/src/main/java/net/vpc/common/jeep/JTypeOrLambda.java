package net.vpc.common.jeep;

import java.util.Arrays;
import java.util.Objects;

public class JTypeOrLambda {
    private JType type;
    private JType[] lambdaArgTypes;

    public static JTypeOrLambda of(JType[] args) {
        return new JTypeOrLambda(args);
    }

    public static JTypeOrLambda ofTypeOrNull(JType type) {
        return type==null?null:new JTypeOrLambda(type);
    }

    public static JTypeOrLambda of(JType type) {
        return new JTypeOrLambda(type);
    }

    public JTypeOrLambda(JType type) {
        this.type = type;
        if(type==null){
            throw new NullPointerException();
        }
    }

    public JTypeOrLambda(JType[] lambdaArgTypes) {
        this.lambdaArgTypes = lambdaArgTypes;
        if(lambdaArgTypes==null){
            throw new NullPointerException();
        }
    }


    public static String signatureStringNoPars(JTypeOrLambda... all){
        StringBuilder sb=new StringBuilder();
        for (int i = 0; i < all.length; i++) {
            if(i>0){
                sb.append(",");
            }
            sb.append(all[i]);
        }
        return sb.toString();
    }
    public static String signatureString(JTypeOrLambda... all){
        StringBuilder sb=new StringBuilder();
        sb.append("(");
        try {
            for (int i = 0; i < all.length; i++) {
                if (i > 0) {
                    sb.append(",");
                }
                sb.append(all[i].toString()); //force non null
            }
            sb.append(")");
        }catch (NullPointerException ex){
            throw new JFixMeLaterException("Invalid null args for signatureString");
        }
        return sb.toString();
    }
//    public static JType[] jTypes(JTypeOrLambda... all){
//        JType[] a=new JType[all.length];
//        for (int i = 0; i < all.length; i++) {
//            a[i]=all[i].getType();
//        }
//        return a;
//    }
//
//    public static JTypeOrLambda jTypeOrLambda(JNode node){
//        if(node instanceof HNLiteral && ((HNLiteral) node).getValue()==null){
//            return new JTypeOrLambda((JType) null);
//        }else if(node instanceof HNLambdaExpression){
//            HNLambdaExpression lx=(HNLambdaExpression) node;
//            List<HNDeclareIdentifier> arguments = lx.getArguments();
//            JType[] lax=new JType[arguments.size()];
//            for (int j = 0; j < lax.length; j++) {
//                JTypeName tn = arguments.get(j).getIdentifierTypeName();
//                JType t = arguments.get(j).getIdentifierType();
//                if(t==null && tn!=null){
//                    return null;
//                }
//                lax[j]= t;
//            }
//            return new JTypeOrLambda(lax);
//        }else {
//            JType t=node==null?null:node.getType();
//            if(t==null){
//                return null;
//            }
//            return new JTypeOrLambda(t);
//        }
//    }
//
//    public static JTypeOrLambda[] jTypeOrLambdas(JNode... all){
//        JTypeOrLambda[] aa=new JTypeOrLambda[all.length];
//        for (int i = 0; i < all.length; i++) {
//            aa[i]= jTypeOrLambda(all[i]);
//            if(aa[i]==null){
//                return null;
//            }
//        }
//        return aa;
//    }

    public boolean isType(){
        return lambdaArgTypes==null;
    }

    public boolean isLambda(){
        return lambdaArgTypes!=null;
    }

    public JType getType() {
        if(!isType()){
            throw new IllegalArgumentException("Not a Type");
        }
        return type;
    }

    public JType[] getLambdaArgTypes() {
        if(!isLambda()){
            throw new IllegalArgumentException("Not a Lambda");
        }
        return lambdaArgTypes;
    }

    @Override
    public String toString() {
        if(isType()){
            return type==null?"null":type.getName();
        }
        StringBuilder sb=new StringBuilder("(");
        for (int i = 0; i < lambdaArgTypes.length; i++) {
            if(i>0){
                sb.append(",");
            }
            sb.append(lambdaArgTypes[i]==null?"?":lambdaArgTypes[i].getName());
        }
        sb.append(")->...");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JTypeOrLambda that = (JTypeOrLambda) o;
        return Objects.equals(type, that.type) &&
                Arrays.equals(lambdaArgTypes, that.lambdaArgTypes);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(type);
        result = 31 * result + Arrays.hashCode(lambdaArgTypes);
        return result;
    }
}
