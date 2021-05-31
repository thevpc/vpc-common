package net.thevpc.common.props.impl;

import net.thevpc.common.props.PropertyEvent;
import net.thevpc.common.props.*;

import java.util.function.Predicate;

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

    public static PropertyEvent prefixPath(PropertyEvent event, Path pathPrefix){
        Path p = pathPrefix.append(event.eventPath());
        Object oldValue = event.oldValue();
        Object newValue = event.newValue();

        PropertyEvent e2 = new PropertyEvent(
                event.property(),
                event.index(),
                oldValue,
                newValue,
                p,
                event.eventType(),false
        );
        return e2;
    }

    public static <T> int bestSelectableIndex(ObservableList<T> list, Predicate<T> selectable, int currIndex, int lastIndex) {
        int bestIndex = -1;
        if (lastIndex <= currIndex) {
            bestIndex = nextSelectableIndex(list, selectable, currIndex);
            if (bestIndex < 0) {
                bestIndex = previousSelectableIndex(list, selectable, currIndex);
            }
        } else {
            bestIndex = previousSelectableIndex(list, selectable, currIndex);
            if (bestIndex < 0) {
                bestIndex = nextSelectableIndex(list, selectable, currIndex);
            }
        }
        return bestIndex;
    }

    //    public static AppChoiceItemRenderer<SimpleItem> prepareSimpleItemList(AppChoiceList<SimpleItem> c, Function<String, AppImage> functionResolver) {
//        LastIndexTracker lastIndexTracker = new LastIndexTracker();
//        c.selection().onChange(lastIndexTracker);
//        c.selection().adjusters().add(new PropertyAdjuster() {
//            @Override
//            public void adjust(PropertyAdjusterContext context) {
//                SimpleItem v = (SimpleItem) context.getNewValue();
//                if (v.isGroup()) {
//                    WritableList<SimpleItem> values = c.values();
//                    int newIndex = bestSelectableIndex(values, values.findFirstIndexOf(v), lastIndexTracker.getLastIndex());
//                    context.doInstead(() -> {
//                        c.selection().indices().add(newIndex);
//                    });
//                }
//            }
//        });
//        c.selection().onChange(new PropertyListener() {
//            private int lastIndex = -1;
//
//            @Override
//            public void propertyUpdated(PropertyEvent event) {
//                SimpleItem v = c.selection().get();
//                if (v != null) {
//                    WritableList<SimpleItem> vals = c.values();
//                    int i = vals.findFirstIndex(a -> a.equals(v));
//                    if (v.isGroup()) {
//                        int bestIndex = -1;
//                        if (lastIndex <= i) {
//                            bestIndex = next(i);
//                            if (bestIndex < 0) {
//                                bestIndex = previous(i);
//                            }
//                        } else {
//                            bestIndex = previous(i);
//                            if (bestIndex < 0) {
//                                bestIndex = next(i);
//                            }
//                        }
//                        if (bestIndex < 0) {
//                            c.selection().clear();
//                        } else {
//                            c.selection().setAll(c.values().get(bestIndex));
//                        }
//                    }
//                    lastIndex = i;
//                }
//            }
//
//            private int next(int i) {
//                int bestIndex = -1;
//                for (int j = i + 1; j < c.values().size(); j++) {
//                    SimpleItem o2 = c.values().get(j);
//                    if (!o2.isGroup()) {
//                        bestIndex = j;
//                        break;
//                    }
//                }
//                return bestIndex;
//            }
//
//            private int previous(int i) {
//                int bestIndex = -1;
//                for (int j = i - 1; j >= 0; j--) {
//                    SimpleItem o2 = c.values().get(j);
//                    if (!o2.isGroup()) {
//                        bestIndex = j;
//                        break;
//                    }
//                }
//                return bestIndex;
//            }
//        });
//        c.itemRenderer().set(
//                context -> {
//                    SimpleItem value = context.getValue();
//                    if (value == null) {
//                        context.setIcon(null);
//                    } else {
//                        SimpleItem nv = value;
//                        if (nv.isGroup()) {
//                            AppFont f = context.getFont();
//                            AppColor fc = context.getColor();
//                            AppColor bc = context.getBackgroundColor();
//                            context.setOpaque(true);
//                            context.setBackgroundColor(fc);
//                            context.setTextColor(bc);
//                            context.setTextFont(f.derive(FontWeight.BOLD).derive(FontPosture.ITALIC));
//                        } else {
//                            String icon = (value).getIcon();
//                            AppImage iconObj = null;
//                            if (functionResolver != null) {
//                                iconObj = functionResolver.apply(icon);
//                            }
//                            context.setIcon(iconObj);
//                        }
//                    }
//                }
//        );
//    }
//
    private static <T> int nextSelectableIndex(ObservableList<T> list, Predicate<T> selectable, int i) {
        int bestIndex = -1;
        for (int j = i + 1; j < list.size(); j++) {
            T o2 = list.get(j);
            if (!selectable.test(o2)) {
                bestIndex = j;
                break;
            }
        }
        return bestIndex;
    }

    private static <T> int previousSelectableIndex(ObservableList<T> list, Predicate<T> selectable, int i) {
        int bestIndex = -1;
        for (int j = i - 1; j >= 0; j--) {
            T o2 = list.get(j);
            if (!selectable.test(o2)) {
                bestIndex = j;
                break;
            }
        }
        return bestIndex;
    }
}
