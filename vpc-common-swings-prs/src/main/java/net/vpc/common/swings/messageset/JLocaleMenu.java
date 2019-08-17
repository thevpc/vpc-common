/**
 * ====================================================================
 *                        vpc-prs library
 *
 * Pluggable Resources Set is a small library for simplifying
 * plugin based applications
 * 
 * Copyright (C) 2006-2008 Taha BEN SALAH
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * ====================================================================
 */

package net.vpc.common.swings.messageset;

import net.vpc.common.swings.SwingLocaleManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Locale;
import net.vpc.common.prs.locale.LocaleManager;

public class JLocaleMenu extends JMenu {
	public static final String PROPERTY_LOCALE_SELECTED="PROPERTY_LOCALE_SELECTED";
	private Locale defaultLocale;

	private Locale[] availableLangs;

	private ButtonGroup bg = new ButtonGroup();
	private JMenuItem[] langMenuItems;

	private ActionListener trap = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			Locale current = (Locale) ((JComponent) e.getSource())
					.getClientProperty("locale");
			fireLocaleSelected(current);
		}
	};

	public JLocaleMenu(Locale defaultOne) {
		this(LocaleManager.getInstance().getAvailableLocales(),defaultOne);
	}
	
	public JLocaleMenu(Locale[] available,Locale defaultOne) {
		this.availableLangs=available;
		this.defaultLocale=defaultOne;
        ArrayList<JMenuItem> all=new ArrayList<JMenuItem>();
        this.addSeparator();
		for (int langId = 0; langId < available.length; langId++) {
            Locale current=available[langId];
            JCheckBoxMenuItem i=new JCheckBoxMenuItem("");
            bg.add(i);
            if(current!=null && current.equals(defaultOne)){
                insert(i,0);
            }else{
                add(i);
            }
            all.add(i);
            i.setName("langMenuItem[" + langId + "]");
			i.setSelected(current!=null && current.equals(Locale.getDefault()));
			i.putClientProperty("locale", current);
			i.addActionListener(trap);
			i.setText(current == null ? "-": current.getDisplayLanguage(current));
		}
        langMenuItems=all.toArray(new JMenuItem[all.size()]);
    }

	private void fireLocaleSelected(Locale locale) {
		firePropertyChange(PROPERTY_LOCALE_SELECTED, null, locale);
	}
	
	public void addLocaleSelectedListener(PropertyChangeListener listener){
		addPropertyChangeListener(PROPERTY_LOCALE_SELECTED,listener);
	}

	public void removeLocaleSelectedListener(PropertyChangeListener listener){
		removePropertyChangeListener(PROPERTY_LOCALE_SELECTED,listener);
	}
    
    public void setSelectedLocale(Locale locale){
        for (JMenuItem langMenuItem : langMenuItems) {
            Locale localeValue=(Locale) langMenuItem.getClientProperty("locale");
            if(localeValue!=null && localeValue.equals(locale)){
                langMenuItem.setSelected(true);
            }
        }
    }
}
