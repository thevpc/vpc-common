package net.thevpc.common.lanterna;

import com.googlecode.lanterna.Symbols;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.ThemeDefinition;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import net.thevpc.common.collections.IntArrayList;

import java.io.IOException;
import java.util.Arrays;

public class BarChart extends AbstractComponent<BarChart> implements ComponentExt {
    private int preferredHeight;
    private VBarSingleModel[] models;
    private double[] legendValues = new double[0];
    private String[] legendLabels = new String[0];
    private double rangeMin = Double.NaN;
    private double rangeMax = Double.NaN;
    //    private ProgressConverter converter;
    private BarValueFormatter formatter;
    private boolean showLabelsOnBars = true;
    private boolean showLegend = true;
    private boolean showLabelsInLegend = true;
    private int labelSize = -1;
    private int legendSize = -1;
    private int minValuesCount = -1;
    private boolean visible = true;


    public BarChart() {
        this(1);
    }

    public BarChart(int size) {
        if (size <= 0) {
            size = 1;
        }
        models = new VBarSingleModel[size];
        for (int i = 0; i < size; i++) {
            models[i]=new VBarSingleModel();
        }
    }

    public int getModelCount(){
        return models.length;
    }

    

    

    @Override
    public TerminalSize getMinimumSize() {
        return new TerminalSize(3, 3);
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public int getMinValuesCount() {
        return minValuesCount;
    }

    public BarChart setMinValuesCount(int minValuesCount) {
        for (VBarSingleModel model : models) {
            model.setMinValuesCount(minValuesCount);
        }
        invalidate();
        return this;
    }

    public void rollValue(double value, String label) {
        rollValue(new double[]{value}, new String[]{label});
    }

    public void rollValue(double[] value, String[] label) {
        checkModelSize(value.length);
        checkModelSize(label.length);
        for (int i = 0; i < models.length; i++) {
            models[i].rollValue(value[i], label[i]);
        }
        invalidate();
    }

    public void rollValue(double value, String label, int maxValues) {
        rollValue(new double[]{value}, new String[]{label}, maxValues);
    }

    public void rollValue(double[] value, String[] label, int maxValues) {
        checkModelSize(value.length);
        checkModelSize(label.length);
        for (int i = 0; i < models.length; i++) {
            models[i].rollValue(value[i], label[i], maxValues);
        }
        invalidate();
    }


    public boolean isShowLabelsOnBars() {
        return showLabelsOnBars;
    }

    public BarChart setShowLabelsOnBars(boolean showLabelsOnBars) {
        this.showLabelsOnBars = showLabelsOnBars;
        return this;
    }

    public boolean isShowLabelsInLegend() {
        return showLabelsInLegend;
    }

    public BarChart setShowLabelsInLegend(boolean showLabelsInLegend) {
        this.showLabelsInLegend = showLabelsInLegend;
        return this;
    }

    public boolean isShowLegend() {
        return showLegend;
    }

    public BarChart setShowLegend(boolean showLegend) {
        this.showLegend = showLegend;
        return this;
    }

    public int getLabelSize() {
        return labelSize;
    }

    public BarChart setLabelSize(int labelSize) {
        this.labelSize = labelSize;
        return this;
    }

    public double getRangeMin() {
        return rangeMin;
    }

    public BarChart setRangeMin(double rangeMin) {
        this.rangeMin = rangeMin;
        reset0();
        return this;
    }

    public int getLegendSize() {
        return legendSize;
    }

    public BarChart setLegendSize(int legendSize) {
        this.legendSize = legendSize;
        return this;
    }

    public BarChart setRange(double min, double max) {
        setRangeMin(min);
        setRangeMax(max);
        return this;
    }

    public double getRangeMax() {
        return rangeMax;
    }

    public BarChart setRangeMax(double rangeMax) {
        this.rangeMax = rangeMax;
        reset0();
        return this;
    }

    public void addValue(double value) {
        addValue(value, null);
    }

    public void addValue(double[] value) {
        addValue(value, new String[value.length]);
    }


    public void addValue(double value, String label) {
        addValue(new double[]{value}, new String[]{label});
    }

    public void addValue(double[] value, String[] label) {
        checkModelSize(value.length);
        checkModelSize(label.length);
        for (int i = 0; i < models.length; i++) {
            models[i].addValue(value[i], label[i]);
        }
        invalidate();
    }

    public void addLegendValue(double value, String label) {
        legendValues = Arrays.copyOf(legendValues, legendValues.length + 1);
        legendLabels = Arrays.copyOf(legendLabels, legendLabels.length + 1);
        legendValues[legendValues.length - 1] = value;
        legendLabels[legendValues.length - 1] = label;
        invalidate();
    }

    public void setValue(int index, double value, String label) {
        setValue(index, new double[]{value}, new String[]{label});
    }

    private void checkModelSize(int size) {
        if (size != models.length) {
            throw new IllegalArgumentException("Illegal Size " + size + " <> " + models.length);
        }
    }

    public void setValue(int index, double[] value, String[] label) {
        checkModelSize(value.length);
        checkModelSize(label.length);
        for (int i = 0; i < models.length; i++) {
            models[i].setValue(index, value[i], label[i]);
        }
        reset0();
        invalidate();
    }

    public void reset() {

    }

    private void reset0() {
//        converter = null;
        invalidate();
    }

    public int size() {
        return models[0].size();
    }

    public BarValueFormatter getFormatter() {
        return formatter;
    }

    public void setFormatter(BarValueFormatter formatter) {
        this.formatter = formatter;
    }

//    public int[] getIntValues() {
//        getConverter();
//        return converter.intValues(values);
//    }

    private ProgressConverter getConverter() {
        ProgressConverter progressConverter = new ProgressConverter(new double[0], rangeMin, rangeMax);
        for (VBarSingleModel model : models) {
            progressConverter.visit(model.values);
        }
        return progressConverter;
    }

    public int getPreferredHeight() {
        return preferredHeight;
    }

    public void setPreferredHeight(int preferredHeight) {
        this.preferredHeight = preferredHeight;
    }

    @Override
    protected ComponentRenderer<BarChart> createDefaultRenderer() {
        return new VBarRenderer();
    }

    private double[] getValues(int m) {
        return models[m].values;
    }

    private String[] getLabels(int m) {
        return models[m].labels;
    }

    private static class ProgressConverter {
        double min = Double.NaN;
        double max = Double.NaN;
        private double rangeMin;
        private double rangeMax;

        public ProgressConverter(double[] values, double rangeMin, double rangeMax) {
            this.rangeMin = rangeMin;
            this.rangeMax = rangeMax;
            visit(values);
        }

        public void visit(double[] values) {
            for (double value : values) {
                if (Double.isNaN(value)) {
                    value = 0;
                }
                if (Double.isNaN(min) || value < min) {
                    min = value;
                }
                if (Double.isNaN(max) || value > max) {
                    max = value;
                }
            }
            if (!Double.isNaN(rangeMin)) {
                min = rangeMin;
            }
            if (!Double.isNaN(rangeMax)) {
                max = rangeMax;
            }
        }

        public int[] intValues(double[] values) {
            int[] intValues = new int[values.length];
            for (int i = 0; i < intValues.length; i++) {
                intValues[i] = intValue(values[i]);
            }
            return intValues;
        }

        public int intValue(double value) {
            if (min == max) {
                return 100;
            }
            if (!Double.isNaN(rangeMin) && value < rangeMin) {
                value = rangeMin;
            }
            if (!Double.isNaN(rangeMax) && value > rangeMax) {
                value = rangeMax;
            }
            return (int) (value / (max - min) * 100);
        }
    }

    public static class VBarRenderer implements ComponentRenderer<BarChart> {
        @Override
        public TerminalSize getPreferredSize(BarChart component) {
            int labelSize = computeLegendLabelSize(component);
            if (labelSize > 0) {
                labelSize++;
            }
            int preferredHeight = component.getPreferredHeight();
            if (preferredHeight <= 0) {
                preferredHeight = 20;
            }
            int vsize = computeValueLabelSize(component);
            if (vsize <= 0) {
                vsize = 1;
            }
            int columns = component.size() * vsize + (labelSize);
            if (columns <= 0) {
                columns = 1;
            }
            if (preferredHeight <= 0) {
                preferredHeight = 1;
            }
            return new TerminalSize(columns, preferredHeight);
        }

        private double computeBarSize(BarChart component, TerminalSize gsize) {
            int legendLabelSize = computeLegendLabelSize(component);
            if (legendLabelSize > 0) {
                legendLabelSize++;
            }
            return (1.0 * gsize.getColumns() - legendLabelSize) / component.size();
        }

        private int computeValueLabelSize(BarChart component) {
            if (!component.showLabelsOnBars) {
                return 0;
            }
            int labelSize = 1;
            SafeBarValueFormatter sf = new SafeBarValueFormatter(component.getFormatter(), -1);

            ProgressConverter converter = component.getConverter();
            for (int m = component.getModelCount() - 1; m >= 0; m--) {
                double[] values = component.getValues(m);
                String[] labels = component.getLabels(m);
                int[] ivalues = converter.intValues(values);
                for (int i = 0; i < values.length; i++) {
                    String s = sf.format(values[i], ivalues[i], labels[i]);
                    if (s != null && s.length() > labelSize) {
                        labelSize = s.length();
                    }
                }
            }

            int barLabelSize = component.getLabelSize();
            if (barLabelSize >= 0) {
                labelSize = barLabelSize;
            }
            return labelSize;
        }

        private int computeLegendLabelSize(BarChart component) {
            return computeLegendLabelSize(component, component.showLegend, component.showLabelsInLegend);
        }

        private int computeLegendLabelSize(BarChart component, boolean showLegend, boolean showLabelsInLegend) {
            if (!showLegend) {
                return 0;
            }
            SafeBarValueFormatter sf = new SafeBarValueFormatter(component.getFormatter(), -1);
            int labelSize = 1;
            ProgressConverter converter = component.getConverter();
            for (int m = component.getModelCount() - 1; m >= 0; m--) {
                int[] ilegendValues = converter.intValues(component.legendValues);
                for (int i = 0; i < component.legendValues.length; i++) {
                    String s = sf.format(component.legendValues[i], ilegendValues[i], component.legendLabels[i]);
                    if (s != null && s.length() > labelSize) {
                        labelSize = s.length();
                    }
                }
            }
            if (showLabelsInLegend) {
                for (int m = component.getModelCount() - 1; m >= 0; m--) {
                    double[] doubles = component.getValues(m);
                    String[] labels = component.getLabels(m);
                    int[] ivalues = converter.intValues(doubles);
                    for (int i = 0; i < doubles.length; i++) {
                        String s = sf.format(doubles[i], ivalues[i], labels[i]);
                        if (s != null && s.length() > labelSize) {
                            labelSize = s.length();
                        }
                    }
                }
            }
            int barLabelSize = component.getLegendSize();
            if (barLabelSize >= 0) {
                labelSize = barLabelSize;
            }
            return labelSize;
        }

//        private int computeLabelSize(BarChart component) {
//            if (!component.showLegend) {
//                return 0;
//            }
//            int labelSize = component.getLabelSize();
//            SafeBarValueFormatter sf = new SafeBarValueFormatter(component.getFormatter(), -1);
//
//            int[] ivalues = component.getConverter().intValues(component.values);
//            int[] ilegendValues = component.getConverter().intValues(component.legendValues);
//            int labelSize1 = 1;
//            for (int i = 0; i < component.values.length; i++) {
//                String s = sf.format(component.values[i], ivalues[i], component.labels[i]);
//                if (s != null && s.length() > labelSize1) {
//                    labelSize1 = s.length();
//                }
//            }
//            for (int i = 0; i < component.legendValues.length; i++) {
//                String s = sf.format(component.legendValues[i], ilegendValues[i], component.legendLabels[i]);
//                if (s != null && s.length() > labelSize1) {
//                    labelSize1 = s.length();
//                }
//            }
//            if(labelSize>1 && labelSize1> labelSize){
//                labelSize1= labelSize;
//            }
//            return (labelSize1 + 1);
//
//        }

        private char getChar(int modelIndex){
            char[] chars = (""+' '+Symbols.BLOCK_SPARSE+Symbols.BLOCK_MIDDLE+Symbols.BLOCK_MIDDLE+Symbols.BLOCK_DENSE).toCharArray();
            return chars[modelIndex%chars.length];
        }

        @Override
        public void drawComponent(TextGUIGraphics graphics, BarChart component) {
            TerminalSize gsize = graphics.getSize();
            boolean showLegend = component.showLegend;
            if (showLegend && component.size() >= gsize.getColumns()) {
                showLegend = false;
            }
            //int valueLabelSize = computeValueLabelSize(component);
            int legendLabelSize = computeLegendLabelSize(component, showLegend, component.showLabelsInLegend);
            if (legendLabelSize > 0) {
                legendLabelSize++;
            }
            int computedLabelSize = computeValueLabelSize(component);
            double dbarSize = computeBarSize(component, gsize);
            TextColor fc = graphics.getForegroundColor();
            TextColor bc = graphics.getBackgroundColor();
            TerminalSize size = component.getSize();
            int max = Math.max(1, size.getRows() - 1);
            SafeBarValueFormatter legendSf = new SafeBarValueFormatter(component.getFormatter(), legendLabelSize == 0 ? 0 : (legendLabelSize - 1));
            SafeBarValueFormatter valueSf = new SafeBarValueFormatter(component.getFormatter(), computedLabelSize);
            ProgressConverter converter = component.getConverter();
            ThemeDefinition themeDefinition = component.getThemeDefinition();
            graphics.applyThemeStyle(themeDefinition.getInsensitive());
            for (int m = component.getModelCount() - 1; m >= 0; m--) {
                char schar = getChar(m);
                double[] values = component.getValues(m);
                String[] labels = component.getLabels(m);
                int[] values1 = converter.intValues(values);
                IntArrayList newValues = new IntArrayList();
                double val = 0;
                int oldInt = -1;
                for (int i = 0; i < values1.length; i++) {
                    int newInt = (int) (val + i * dbarSize);
                    if (newInt != oldInt) {
                        newValues.add(i);
                        oldInt = newInt;
                    }
                }
                int barSize = (int) dbarSize;
                if (barSize <= 0) {
                    barSize = 1;
                }
                if (computedLabelSize > barSize) {
                    computedLabelSize = barSize;
                }
                for (int vi = 0; vi < newValues.size(); vi++) {
                    int value = values1[newValues.get(vi)];
                    int ri = max - (int) (value / (100.0) * max);

                    for (int ri2 = ri; ri2 <= max; ri2++) {
                        for (int j = 0; j < barSize; j++) {
                            graphics.setCharacter(legendLabelSize + vi * barSize + j, ri2, schar);
                        }
                    }
                    if (component.showLabelsOnBars || (showLegend && component.showLabelsInLegend)) {
                        if (component.showLabelsOnBars) {
                            String stringValue = valueSf.format(values[vi], value, labels[vi]);
                            if (stringValue.length() > barSize) {
                                stringValue = stringValue.substring(0, barSize);
                            }
                            graphics.putString(legendLabelSize + vi * barSize, ri, stringValue);
                        }
                    }
                }


            }

            if (showLegend) {
                graphics.setForegroundColor(bc);
                graphics.setBackgroundColor(fc);
                for (int m = component.getModelCount() - 1; m >= 0; m--) {

                    double[] values = component.getValues(m);
                    String[] labels = component.getLabels(m);
                    int[] values1 = converter.intValues(values);
                    IntArrayList newValues = new IntArrayList();
                    double val = 0;
                    int oldInt = -1;
                    for (int i = 0; i < values1.length; i++) {
                        int newInt = (int) (val + i * dbarSize);
                        if (newInt != oldInt) {
                            newValues.add(i);
                            oldInt = newInt;
                        }
                    }

                    for (int vi = 0; vi < newValues.size(); vi++) {
                        int value = values1[newValues.get(vi)];
                        int ri = max - (int) (value / (100.0) * max);
                        if (component.showLabelsInLegend) {
                            String stringValue = legendSf.format(values[vi], value, labels[vi]);
                            graphics.putString(0, ri, stringValue);
                        }
                    }
                }
                for (int i = 0; i <= max; i++) {
                    graphics.putString(legendLabelSize - 1, i, String.valueOf(Symbols.SINGLE_LINE_VERTICAL));
                }
                if (component.legendValues != null) {
                    int[] values2 = converter.intValues(component.legendValues);
                    for (int vi = 0; vi < component.legendValues.length; vi++) {
                        int value = values2[vi];
                        int ri = max - (int) (value / (100.0) * max);
                        String stringValue = legendSf.format(component.legendValues[vi], value, component.legendLabels[vi]);
                        graphics.putString(0, ri, stringValue);
                    }
                }
                graphics.setForegroundColor(fc);
                graphics.setBackgroundColor(bc);
            }

        }
    }

    private static class SafeBarValueFormatter implements BarValueFormatter {
        private int minSize;
        private BarValueFormatter formatter;

        public SafeBarValueFormatter(BarValueFormatter formatter, int csize) {
            this.formatter = formatter;
            this.minSize = csize;
        }

        @Override
        public String format(double value, int percentValue, String stringValue) {
            BarValueFormatter f = formatter;
            String stringValue2 = null;
            if (f != null) {
                stringValue2 = f.format(value, percentValue, stringValue);
            } else {
                stringValue2 = stringValue;
            }
            if (stringValue2 == null) {
                stringValue2 = String.valueOf(value);
            }
            StringBuilder sb = new StringBuilder();
            sb.append(stringValue2);
            if (minSize >= 0) {
                while (sb.length() < minSize) {
                    sb.insert(0, ' ');
                }
                while (sb.length() > minSize) {
                    sb.delete(sb.length() - 1, sb.length());
                }
            }
            return sb.toString();
        }
    }
}
