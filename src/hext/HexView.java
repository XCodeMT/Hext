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

import awtX.XFileChooser;
import com.XCodeMT.chromeTabs.ITab;
import java.awt.GridLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author XCodeMT
 */
public class HexView extends javax.swing.JPanel {
    private XFileChooser fileChooser = new XFileChooser();
    private File content = null;
    ITab tab;

    /**
     * Creates new form HexView
     */
    public HexView(ITab tab) {
        super(new GridLayout(1, 1));
        initComponents();
        addComponentListener(new HexResizeListener());
        splitPane.setDividerLocation(0.66);
        this.tab = tab;
    }
    
    public void resizeHexView() {
        splitPane.setDividerLocation(0.66);
    }
    
    public void open() {
        fileChooser.showOpenDialog();
        content = fileChooser.getFile();
        updateFromFile();
    }
    
    public void save() {
        fileChooser.showSaveDialog();
    }

    private void updateFromFile() {
        try {
            byte[] byteContent = FileUtils.readFileToByteArray(content);
            String hexContent = "";
            int j = 0;
            int l = 0;
            for (int i = 0; i < byteContent.length; i++) {
                if (l > 31) {
                    hexContent += "\n";
                    l = 0;
                    j = 0;
                }
                if (j > 3) {
                    hexContent += " ";
                    j = 0;
                }
                hexContent += hexString(byteContent[i]);
                j++;
                l++;
            }
            String stringContent = FileUtils.readFileToString(content);
            stringContent = convert(stringContent);
            hexArea.setText(hexContent);
            textArea.setText(stringContent);
        } catch (IOException ex) {
            Logger.getLogger(HexView.class.getName()).log(Level.SEVERE, null, ex);
        }
        tab.setTitle(content.getName());
    }
    
    private String convert(String in) {
        char[] charIN = in.toCharArray();
        String out = "";
        int l = 0;
        for (int i = 0; i < charIN.length; i++) {
            if (charIN[i] == '\n') {
                charIN[i] = ' ';
            }
            l++;
            if (l > 15) {
                out += "\n";
                l = 0;
            }
            out += charIN[i];
        }
        return out;
    }
    
    public String hexString(byte in) {
        String out = "";
        String temp = Integer.toHexString(in);
        String byteString = "";
        char[] tempBytes = temp.toCharArray();
        char[] bytes = {'0', 0};
        switch(tempBytes.length) {
            case 1:
                bytes[1] = tempBytes[0];
                break;
            case 2:
                bytes = tempBytes;
                break;
            case 8:
                bytes[0] = tempBytes[6];
                bytes[1] = tempBytes[7];
                break;
            default:
                bytes[0] = tempBytes[0];
                bytes[1] = tempBytes[1];
                break;
        }
        
        for (int i = 0; i < bytes.length; i++) {
            byteString += bytes[i];
        }
        System.out.println(byteString);
        out = temp;
//        char[] temp = Integer.toHexString(in).toCharArray();
//        if(temp.length > 1) {
//            out += temp[temp.length - 2] + temp[temp.length - 1];
//        } else {
//            out += "0" + temp[temp.length - 1];
//        }
        return byteString.toUpperCase();
    }
    
    class HexResizeListener implements ComponentListener {

        @Override
        public void componentResized(ComponentEvent e) {
            resizeHexView();
        }

        @Override
        public void componentMoved(ComponentEvent e) {
        }

        @Override
        public void componentShown(ComponentEvent e) {
        }

        @Override
        public void componentHidden(ComponentEvent e) {
        }
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        splitPane = new javax.swing.JSplitPane();
        textArea = new javax.swing.JTextArea();
        hexArea = new javax.swing.JTextArea();

        splitPane.setDividerLocation(50);

        textArea.setFont(new java.awt.Font("Monaco", 0, 13)); // NOI18N

        splitPane.setRightComponent(textArea);

        hexArea.setFont(new java.awt.Font("Monaco", 0, 13)); // NOI18N

        splitPane.setLeftComponent(hexArea);
        
        add(splitPane);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea hexArea;
    private javax.swing.JSplitPane splitPane;
    private javax.swing.JTextArea textArea;
    // End of variables declaration//GEN-END:variables
}
