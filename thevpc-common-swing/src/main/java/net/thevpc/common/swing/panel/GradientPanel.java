/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.swing.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * https://stackoverflow.com/questions/45603312/smooth-gradient-background-animation-java
 */
public class GradientPanel extends JPanel {

    private static final int WIDE = 640;
    private static final int HIGH = 240;
    private final Timer timer;
    private float hue = 0;
    private Color color1 = Color.white;
    private Color color2 = Color.black;
    private float minValue = 0f;
    private float maxValue = 1f;
    private float step = 0.001f;
    private float saturation = 1f;
    private float brightness = 1f;
    private float contrastFactor = 16f;

    public GradientPanel() {
        ActionListener action = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                if (hue < minValue) {
                    hue = minValue;
                }
                hue += step;
                if (hue > maxValue) {
                    hue = minValue;
                }
                color1 = Color.getHSBColor(hue, saturation, brightness);
                color2 = Color.getHSBColor(hue + contrastFactor * step, saturation, brightness);
                repaint();
            }
        };
        timer = new Timer(500, action);
        timer.start();
    }

    public float getContrastFactor() {
        return contrastFactor;
    }

    public void setContrastFactor(float contrastFactor) {
        if (contrastFactor < 1) {
            contrastFactor = 1;
        }
        this.contrastFactor = contrastFactor;
    }

    public float getHue() {
        return hue;
    }

    public float getSaturation() {
        return saturation;
    }

    public float getMinValue() {
        return minValue;
    }

    public void setMinValue(float minValue) {
        if (minValue <= 0 || minValue >= 1) {
            throw new IllegalArgumentException("minValue should be >0 and <1");
        }
        this.minValue = minValue;
    }

    public float getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(float maxValue) {
        if (maxValue <= 0 || maxValue >= 1) {
            throw new IllegalArgumentException("maxValue should be >0 and <1");
        }
        this.maxValue = maxValue;
    }

    public void setSaturation(float saturation) {
        if (saturation <= 0 || saturation >= 1) {
            throw new IllegalArgumentException("saturation should be >0 and <1");
        }
        this.saturation = saturation;
    }

    public float getBrightness() {
        return brightness;
    }

    public void setBrightness(float brightness) {
        if (brightness <= 0 || brightness >= 1) {
            throw new IllegalArgumentException("brightness should be >0 and <1");
        }
        this.brightness = brightness;
    }

    public void setHue(float hue) {
        if (hue <= 0 || hue >= 1) {
            throw new IllegalArgumentException("hue should be >0 and <1");
        }
        this.hue = hue;
    }

    public float getStep() {
        return step;
    }

    public void setStep(float step) {
        if (step <= 0 || step >= 1) {
            throw new IllegalArgumentException("step should be >0 and <1");
        }
        this.step = step;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        GradientPaint p = new GradientPaint(
                0, 0, color1, getWidth(), 0, color2);
        g2d.setPaint(p);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(WIDE, HIGH);
    }
}
