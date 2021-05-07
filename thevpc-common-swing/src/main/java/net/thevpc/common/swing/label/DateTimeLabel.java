/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.swing.label;

import net.thevpc.common.swing.label.JTimerLabel;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author vpc
 */
public class DateTimeLabel extends JTimerLabel {

    private DateTimeFormatter dateTimeFormatter;

    public DateTimeLabel() {
        start();
    }

    
    public DateTimeFormatter getDateTimeFormatter() {
        return dateTimeFormatter;
    }

    public DateTimeLabel setDateTimeFormatter(String dateTimeFormatter) {
        this.dateTimeFormatter = dateTimeFormatter == null ? null
                : DateTimeFormatter.ofPattern(dateTimeFormatter)
                        .withZone(ZoneId.systemDefault());
        return this;
    }

    public DateTimeLabel setDateTimeFormatter(DateTimeFormatter dateTimeFormatter) {
        this.dateTimeFormatter = dateTimeFormatter;
        return this;
    }

    @Override
    protected void tic() {
        Instant n = Instant.now();
        if (dateTimeFormatter != null) {
            setText(dateTimeFormatter.format(n));
        } else {
            setText(n.toString());
        }
    }

}
