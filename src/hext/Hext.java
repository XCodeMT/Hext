/*
 * Copyright (C) 2013 Silas Schwarz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package hext;

import com.XCodeMT.chromeTabs.ITab;
import com.XCodeMT.chromeTabs.ITabbedPaneWindow;
import com.XCodeMT.chromeTabs.ITabbedPaneWindowFactory;
import com.XCodeMT.chromeTabs.tabsX.XTabProgram;
import java.awt.Window;
import java.lang.reflect.Method;
import javax.swing.SwingUtilities;

/**
 *
 * @author XCodeMT
 */
public class Hext implements XTabProgram {
    
    public static void main(String args[]) {
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        SwingUtilities.invokeLater(new Hext());
    }

    @Override
    public void run() {
        ITabbedPaneWindowFactory windowFactory = new HTabbedPaneWindowFactory();
        ITabbedPaneWindow window = windowFactory.createWindow();
        ITab tab = window.getTabbedPane().getTabFactory().createTab("New...");
        tab.setContent(new HexView(tab));
        window.getTabbedPane().addTab(tab);
        window.getTabbedPane().setSelectedTab(tab);
        window.getWindow().setSize(500, 500);
        window.getWindow().setVisible(true);
    }
}
