/**
 * ====================================================================
 *                        vpc-prs library
 *
 * Pluggable Resources Set is a small library for simplifying
 * plugin based applications
 * 
 * <br>
 *
 * Copyright [2020] [thevpc]
 * Licensed under the Apache License, Version 2.0 (the "License"); you may 
 * not use this file except in compliance with the License. You may obtain a 
 * copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an 
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied. See the License for the specific language 
 * governing permissions and limitations under the License.
 * <br>
 * ====================================================================
*/

package net.thevpc.common.swings.messageset;

import net.thevpc.common.prs.locale.LocaleManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Locale;

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
