package net.thevpc.common.lanterna;

import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.ProgressBar;

public class HBarChart extends PanelExt {
    ProgressBar[] all = new ProgressBar[10];
    private int columns;
    public HBarChart(int rows, int columns) {
        super(new LinearLayout(Direction.VERTICAL));
        this.columns=columns;
        setRows(rows);
    }

    public void setRows(int rows){
        removeAllComponents();
        all=new ProgressBar[rows];
        for (int i = 0; i < all.length; i++) {
            all[i] = new ProgressBar(0,100,columns);
            all[i].setLabelFormat("%2.0f%%");
            addComponent(all[i]);
        }
    }

    public void appendValue(int newValue,String format) {
        for (int i = all.length - 1; i > 0; i--) {
            all[i].setValue(all[i - 1].getValue());
            all[i].setLabelFormat(all[i - 1].getLabelFormat());
        }
        all[0].setValue(newValue);
        format=format==null?"%2.0f%%":format;
        all[0].setLabelFormat(format);
    }

    public void setValues(int[] values) {
        for (int i = 0; i < all.length; i++) {
            all[i].setValue(values[i]);
        }
    }

    public void reset() {
        for (int i = 0; i < all.length; i++) {
            all[i].setLabelFormat("%2.0f%%");
            all[i].setValue(0);
        }
    }
}
