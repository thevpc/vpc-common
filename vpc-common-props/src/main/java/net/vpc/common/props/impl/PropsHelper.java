package net.vpc.common.props.impl;

import net.vpc.common.props.PropertyEvent;
import net.vpc.common.props.*;

public class PropsHelper {

    public static String buildPath(String ... all){
        StringBuilder sb=new StringBuilder();
        for (String s : all) {
            if(s!=null) {
                if (s.startsWith("/")) {
                    s = s.substring(1);
                }
                if (s.endsWith("/")) {
                    s = s.substring(0, s.length() - 1);
                }
                if (s.length() > 0) {
                    sb.append("/");
                    sb.append(s);
                }
            }
        }
        if(sb.length()==0){
            return "/";
        }
        return sb.toString();
    }

    public static PropertyEvent prefixPath(PropertyEvent event,String pathPrefix){
        String p = buildPath(pathPrefix,event.getPath());
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        PropertyEvent e2 = new PropertyEvent(
                event.getProperty(),
                event.getIndex(),
                oldValue,
                newValue,
                p,
                event.getAction()
        );
        return e2;
    }
}
