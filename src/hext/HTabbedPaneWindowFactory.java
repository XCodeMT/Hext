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
import com.XCodeMT.chromeTabs.TabbedPane;
import com.XCodeMT.chromeTabs.tabsX.XTabFactory;
import com.XCodeMT.chromeTabs.tabsX.XTabbedPaneWindow;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.lang.reflect.Method;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

/**
 *
 * @author XCodeMT
 */
public class HTabbedPaneWindowFactory implements ITabbedPaneWindowFactory {

    @Override
    public ITabbedPaneWindow createWindow() {
        XTabbedPaneWindow tabbedPaneWindow = new XTabbedPaneWindow();
        tabbedPaneWindow.getTabbedPane().setTabFactory(new XTabFactory());
        tabbedPaneWindow.getTabbedPane().setWindowFactory(this);
        tabbedPaneWindow.getTabbedPane().setNewTabActionListener(new NewTabListener(tabbedPaneWindow.getTabbedPane()));
        tabbedPaneWindow.setJMenuBar(createMenuBar(tabbedPaneWindow.getTabbedPane()));
        enableOSXFullscreen(tabbedPaneWindow.getWindow());
        return tabbedPaneWindow;
    }

    private JMenuBar createMenuBar(final TabbedPane pane) {
        JMenuBar bar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem open = new JMenuItem("Open");
        JMenuItem save = new JMenuItem("Save");
        JMenuItem newTab = new JMenuItem("New Tab");
        JMenuItem newWindow = new JMenuItem("New Window");

        open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, 
                         Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, 
                         Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        newTab.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, 
                         Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        newWindow.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, 
                         Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((HexView) pane.getSelectedTab().getContent()).open();
            }
        });
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((HexView) pane.getSelectedTab().getContent()).save();
            }
        });
        newTab.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (pane.getTabFactory() == null) {
                    return;
                }
                ITab newTab = pane.getTabFactory().createTab("New...");
                newTab.setContent(new HexView(newTab));
                pane.addTab(pane.getTabCount(), newTab);
                pane.setSelectedTab(newTab);
            }
        });
        newWindow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Hext());
            }
        });

        fileMenu.add(open);
        fileMenu.add(save);
        fileMenu.add(newTab);
        fileMenu.add(newWindow);

        bar.add(fileMenu);

        return bar;

    }

    class NewTabListener implements ActionListener {

        TabbedPane tabbedPane;

        NewTabListener(TabbedPane tabbedPane) {
            this.tabbedPane = tabbedPane;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (tabbedPane.getTabFactory() == null) {
                return;
            }
            ITab newTab = tabbedPane.getTabFactory().createTab("New...");
            newTab.setContent(new HexView(newTab));
            tabbedPane.addTab(tabbedPane.getTabCount(), newTab);
            tabbedPane.setSelectedTab(newTab);
        }
    }
    
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void enableOSXFullscreen(Window window) {
        try {
            Class util = Class.forName("com.apple.eawt.FullScreenUtilities");
            Class params[] = new Class[]{Window.class, Boolean.TYPE};
            Method method = util.getMethod("setWindowCanFullScreen", params);
            method.invoke(util, window, true);
        } catch (ClassNotFoundException e1) {
        } catch (Exception e) {
        }
    }
}
