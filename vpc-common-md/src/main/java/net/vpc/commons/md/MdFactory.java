/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.commons.md;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author vpc
 */
public class MdFactory {

    private MdFactory() {

    }

    public static MdElement append(Collection<MdElement> arr) {
        return append(false, arr);
    }
    
    public static MdElement appendInline(Collection<MdElement> arr) {
        return append(true, arr);
    }
    
    public static MdElement append(MdElement... arr) {
        return append(false, arr);
    }
    
    public static MdElement appendInline(MdElement... arr) {
        return append(true, arr);
    }
    
    public static MdElement append(boolean inline, Collection<MdElement> arr) {
        if (arr == null) {
            return append(inline, new MdElement[0]);
        }
        return append(inline, arr.toArray(new MdElement[0]));
    }

    public static MdElement append(boolean inline, MdElement... arr) {
        List<MdElement> all = new ArrayList<MdElement>();
        if (arr != null) {
            for (MdElement mdElement : arr) {
                if (mdElement != null) {
                    all.add(mdElement);
                }
            }
        }
        if (all.isEmpty()) {
            return new MdText("");
        }
        if (all.size() == 1) {
            return all.get(0);
        }
        return new MdSequence("", all.toArray(new MdElement[0]), inline);
    }
}
