/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.commons.md.convert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.thevpc.commons.ljson.LJSON;
import net.thevpc.commons.md.MdElement;
import net.thevpc.commons.md.MdElementPath;
import net.thevpc.commons.md.MdElementTransformBase;
import net.thevpc.commons.md.MdFactory;
import net.thevpc.commons.md.MdLineSeparator;
import net.thevpc.commons.md.MdSequence;
import net.thevpc.commons.md.MdText;
import net.thevpc.commons.md.MdTitle;
import net.thevpc.commons.md.MdXml;
import net.thevpc.commons.md.docusaurus.DocusaurusUtils;

/**
 *
 * @author thevpc
 */
public class DocusaurusTreeTransform extends MdElementTransformBase {

    public DocusaurusTreeTransform() {
    }

    @Override
    protected MdElement transformTitle(MdElementPath<MdTitle> path) {
        MdTitle e=path.getElement();
        if (e.getDepth() < 6) {
            return new MdTitle(e.getCode(), e.getValue(), e.getDepth() + 1);
        }
        return e;
    }

    @Override
    public MdElement transformDocument(MdElement e) {
        if (e instanceof MdSequence) {
            MdSequence s = (MdSequence) e;
            MdElement[] content = s.getElements();
            if (content.length > 0 && content[0] instanceof MdLineSeparator) {
                int x = 0;
                for (int i = 1; i < content.length; i++) {
                    if (content[i] instanceof MdLineSeparator) {
                        x = i + 1;
                        break;
                    }
                }
                List<MdElement> a = new ArrayList<>();
                for (int i = x; i < content.length; i++) {
                    a.add(content[i]);
                }
                for (Iterator<MdElement> it = a.iterator(); it.hasNext();) {
                    MdElement mdElement = it.next();
                    if (mdElement instanceof MdText) {
                        String t = ((MdText) mdElement).getText();
                        if (t.length() == 0) {
                            it.remove();
                        } else if (t.startsWith("import ")) {
                            it.remove();
                        } else {
                            break;
                        }
                    }
                }
                return super.transformDocument(MdFactory.seq(a));
            }
        }
        return super.transformDocument(e);
    }

    @Override
    protected MdElement transformXml(MdElementPath<MdXml> path) {
        MdXml e=path.getElement();
        switch (e.getTag()) {
            case "Tabs": {
                String props = DocusaurusUtils.skipJsonJSXBrackets(e.getProperties().get("values"));
                LJSON[] rows = LJSON.of(props).arrayMembers();
                Map<String,MdElement> sub=new HashMap<>();
                for (MdElement item : MdFactory.asSeq(e.getContent()).getElements()) {
                    if (item.isXml()) {
                        MdXml tabItem = item.asXml();
                        String t = tabItem.getTag();
                        if (t.equals("TabItem")) {
                            String tt = "Unknown";
                            LJSON v = LJSON.of(tabItem.getProperties().get("value"));
                            if (v != null) {
                                tt = v.asString();
                            }
                            MdElement u = transformXml(path.append(tabItem));
                            sub.put(tt, u);
                        }
                    } else if (item.isText()) {
                        if (item.asText().getText().trim().length() > 0) {
                            throw new IllegalArgumentException("Unexpected " + item.getElementType() + ":" + item.asText().getText());
                        }
                    } else {
                        throw new IllegalArgumentException("Unexpected " + item.getElementType() + ":");
                    }
                }
                List<MdElement> res=new ArrayList<>();
                for (LJSON row : rows) {
                    MdElement r = sub.get(row.get("value").asString());
                    if(r!=null){
                        res.add(r);
                    }
                }
                return MdFactory.seq(res.toArray(new MdElement[0]));
            }

            case "TabItem": {
                String tt = "Unknown";
                LJSON v = LJSON.of(e.getProperties().get("value"));
                if (v != null) {
                    tt = v.asString();
                }
                String props = DocusaurusUtils.skipJsonJSXBrackets(path.getParentPath().getElement().asXml().getProperties().get("values"));
                for (LJSON a : LJSON.of(props).arrayMembers()) {
                    if (tt.equals(a.get("value").asString())) {
                        tt = a.get("label").asString();
                        break;
                    }
                }
                if (tt.equals("C#")) {
                    tt = "C Sharp";
                }
                return new MdSequence("", new MdElement[]{new MdTitle("#####", tt, 5), transformElement(path.append(e.getContent()))}, false);
            }

        }
        return e;
    }

}
