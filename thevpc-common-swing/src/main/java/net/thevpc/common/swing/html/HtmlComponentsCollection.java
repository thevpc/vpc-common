/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.swing.html;

import java.awt.ItemSelectable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.*;
import javax.swing.ButtonModel;
import javax.swing.JEditorPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.Element;
import javax.swing.text.ElementIterator;
import javax.swing.text.StyleConstants;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;

/**
 *
 * @author thevpc
 */
public class HtmlComponentsCollection implements Iterable<HtmlComponent> {

//    public static void main(String[] args) {
//        try {
//            JEditorPane p = new JEditorPane();
//            p.setContentType("text/html");
//            p.setText("<html><body>Hello<form> <input id='jj' type=\"checkbox\" name=\"toto\"><a href='http://bye.com'>hihi</a></form></body></html>");
//            p.setEditable(false);
//            JScrollPane pp = new JScrollPane(p);
//            pp.setPreferredSize(new Dimension(400, 400));
//            p.addHyperlinkListener(new HyperlinkListener() {
//
//                @Override
//                public void hyperlinkUpdate(HyperlinkEvent e) {
//                    if (EventType.ACTIVATED.equals(e.getEventType())) {
//                        System.out.println("click " + e.getURL());
//                    }
//                }
//            });
//            HtmlComponentsCollection cc = parse(p);
//            cc.addItemListener(new ItemListener() {
//
//                @Override
//                public void itemStateChanged(ItemEvent e) {
//                    HtmlComponent c = (HtmlComponent) e.getSource();
//                    System.out.println(c.getId() + " : " + c.isSelected());
//                }
//            });
//            JOptionPane.showMessageDialog(null, pp);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
    private List<HtmlComponent> components = new ArrayList<HtmlComponent>();
    private Map<String, HtmlComponent> map = new HashMap<String, HtmlComponent>();

    public static HtmlComponentsCollection parse(JEditorPane editor) {
        HTMLDocument doc = (HTMLDocument) editor.getDocument();
        ElementIterator it = new ElementIterator(doc);
        Element element;
        int index = 0;
        HtmlComponentsCollection cc = new HtmlComponentsCollection();
        while ((element = it.next()) != null) {
            System.out.println("start " + element);
            AttributeSet as = element.getAttributes();
            Enumeration enumm = as.getAttributeNames();
            boolean input = element.getName().equals("input");
            if (input) {
                String type = null;
                String id = null;
                Object model = null;
                HashMap<Object, Object> properties = new HashMap<Object, Object>();
                while (enumm.hasMoreElements()) {
                    Object name = enumm.nextElement();
                    Object value = as.getAttribute(name);
                    System.out.println("\t\t****** " + name + " ==> " + value);
                    if (isHtmlAttribute(name)) {
                        properties.put(name.toString(), value);
                    }
                    if (isHtmlAttribute(name, "id") && value instanceof String) {
                        id = (String) value;
                    } else if (isHtmlAttribute(name, "type")) {
                        type = (String) value;
                    } else if (isStyleConstants(name, "model")) {
                        model = value;
                    }
                }
                if (input) {
                    cc.add(new HtmlComponent(++index, id, model, type, properties));
                }
            }
        }
        return cc;
    }

    public HtmlComponent getComponent(String id) {
        return map.get(id);
    }

    private static boolean isHtmlAttribute(Object nameObject, String name) {
        if (nameObject instanceof HTML.Attribute) {
            return ((HTML.Attribute) nameObject).toString().equals(name);
        }
        return false;
    }

    private static boolean isHtmlAttribute(Object nameObject) {
        return (nameObject instanceof HTML.Attribute);
    }

    private static boolean isStyleConstants(Object nameObject, String name) {
        if (nameObject instanceof StyleConstants) {
            return ((StyleConstants) nameObject).toString().equals(name);
        }
        return false;
    }

    @Override
    public Iterator<HtmlComponent> iterator() {
        return components.iterator();
    }

    public int size() {
        return components.size();
    }

    public void add(HtmlComponent c) {
        components.add(c);
        String id = c.getId();
        if (id == null) {
            System.err.println("Component without id");
        } else if (map.containsKey(id)) {
            System.err.println("Duplcate Component id " + id);
        } else {
            map.put(id, c);
        }
    }

    public void addActionListener(ActionListener a) {
        for (HtmlComponent htmlComponent : components) {
            if (htmlComponent.getModel() instanceof ButtonModel) {
                ((ButtonModel) htmlComponent.getModel()).addActionListener(new HtmlComponentActionListenerAdapter(a, htmlComponent));
            }
            //if(htmlComponent instanceof )
        }
    }

    public void addItemListener(ItemListener a) {
        for (HtmlComponent htmlComponent : components) {
            if (htmlComponent.getModel() instanceof ButtonModel) {
                ((ButtonModel) htmlComponent.getModel()).addItemListener(new HtmlComponentItemListenerAdapter(a, htmlComponent));
            }
            //if(htmlComponent instanceof )
        }
    }

    public void addChangeListener(ChangeListener a) {
        for (HtmlComponent htmlComponent : components) {
            if (htmlComponent.getModel() instanceof ButtonModel) {
                ((ButtonModel) htmlComponent.getModel()).addChangeListener(new HtmlComponentChangeListenerAdapter(a, htmlComponent));
            }
            //if(htmlComponent instanceof )
        }
    }

    private class HtmlComponentActionListenerAdapter implements ActionListener {

        private ActionListener a;
        private HtmlComponent c;

        public HtmlComponentActionListenerAdapter(ActionListener a, HtmlComponent c) {
            this.a = a;
            this.c = c;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            ActionEvent x = new ActionEvent(c, e.getID(), e.getActionCommand(), e.getWhen(), e.getModifiers());
            a.actionPerformed(x);
        }
    }

    private class HtmlComponentChangeListenerAdapter implements ChangeListener {

        private ChangeListener a;
        private HtmlComponent c;

        public HtmlComponentChangeListenerAdapter(ChangeListener a, HtmlComponent c) {
            this.a = a;
            this.c = c;
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            ChangeEvent x = new ChangeEvent(c);
            a.stateChanged(x);

        }
    }

    private static class HtmlComponentItemSelectableAdapter extends HtmlComponent implements ItemSelectable {

        private HtmlComponent c;

        public HtmlComponentItemSelectableAdapter(HtmlComponent c) {
            super(c.getIndex(), c.getId(), c.getModel(), c.getType(), c.getProperties());
            this.c = c;
        }

        @Override
        public void addItemListener(ItemListener l) {
            ((ItemSelectable) c.getModel()).addItemListener(l);
        }

        @Override
        public Object[] getSelectedObjects() {
            return ((ItemSelectable) c.getModel()).getSelectedObjects();
        }

        @Override
        public void removeItemListener(ItemListener l) {
            ((ItemSelectable) c.getModel()).removeItemListener(l);
        }
    }

    private static class HtmlComponentItemListenerAdapter implements ItemListener {

        private ItemListener a;
        private HtmlComponent c;
        private HtmlComponentItemSelectableAdapter s;

        public HtmlComponentItemListenerAdapter(ItemListener a, HtmlComponent c) {
            this.a = a;
            this.c = c;
            s = new HtmlComponentItemSelectableAdapter(c);
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
            ItemEvent x = new ItemEvent(s, e.getID(), e.getItem(), e.getStateChange());
            a.itemStateChanged(x);
        }
    }
}
