/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.lanterna.test;

import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Borders;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import java.io.IOException;
import net.thevpc.common.lanterna.BarChart;
import net.thevpc.common.lanterna.GridBagLayout;
import net.thevpc.common.lanterna.GridCell;
import net.thevpc.common.lanterna.PanelExt;

/**
 *
 * @author vpc
 */
public class NewClassTest {
    public static void main(String[] args) {
        try {
            DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
//            terminalFactory.setInitialTerminalSize(new TerminalSize(200, 80));
            TerminalScreen screen = terminalFactory.createScreen();
            screen.startScreen();
            MultiWindowTextGUI textGUI = new MultiWindowTextGUI(screen);
            Window window = new BasicWindow("Login");


            PanelExt p = new PanelExt(new GridBagLayout());
            p.addComponent(createVBarChart().setLayoutData(new GridCell("<+*>")).withBorder(Borders.singleLine()));
            p.addComponent(createVBarChart().setLayoutData(new GridCell("<+*>")).withBorder(Borders.singleLine()));
            p.addComponent(createVBarChart().setLayoutData(new GridCell("<+*c>.")).withBorder(Borders.singleLine()));
            p.addComponent(createVBarChart().setLayoutData(new GridCell("<+*>")).withBorder(Borders.singleLine()));
            p.addComponent(createVBarChart().setLayoutData(new GridCell("<+*>")).withBorder(Borders.singleLine()));
            p.addComponent(createVBarChart().setLayoutData(new GridCell("<+*>")).withBorder(Borders.singleLine()));
            p.addComponent(createVBarChart().setLayoutData(new GridCell("<+*>")).withBorder(Borders.singleLine()));
            p.addComponent(createVBarChart().setLayoutData(new GridCell("<+*>")).withBorder(Borders.singleLine()));
            p.addComponent(createVBarChart().setLayoutData(new GridCell("<+*>")).withBorder(Borders.singleLine()));
            p.addComponent(createVBarChart().setLayoutData(new GridCell("<+*>")).withBorder(Borders.singleLine()));
            p.addComponent(createVBarChart().setLayoutData(new GridCell("<+*>")).withBorder(Borders.singleLine()));
//
//            p.setPreferredSize(screen.getTerminalSize());
//            Panel p = new Panel();
//            p.addComponent(component);

            p.setPreferredSize(screen.getTerminal().getTerminalSize().withRelative(-4, -4));
            window.setComponent(p);
            textGUI.addWindowAndWait(window);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    
    private static BarChart createVBarChart() {
        BarChart component = new BarChart(2);
        component.setRange(0, 100);
        component.setLegendSize(0);
        component.setLabelSize(0);
//            component.addValue(50);
        component.addValue(new double[]{30,50});
        component.addValue(new double[]{5,10});
        component.addValue(new double[]{10,20});
        component.addValue(new double[]{20,30});
        component.addValue(new double[]{40,50});
        component.addValue(new double[]{80,90});
        component.addValue(new double[]{20,30});
        component.addValue(new double[]{40,30});
        component.addValue(new double[]{80,90});
        component.addLegendValue(0, "0%");
        component.addLegendValue(50, "50%");
        component.addLegendValue(100, "100%");
//            component.setPreferredSize(new TerminalSize(40,20));

//            ProgressBar component = new ProgressBar();
//            component.setValue(65);
        return component;
    }
}
