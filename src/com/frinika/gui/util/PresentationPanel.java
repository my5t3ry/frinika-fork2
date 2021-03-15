/*
 * Created on Jul 5, 2007
 *
 * Copyright (c) 2006-2007 Jens Gulden
 * 
 * http://www.frinika.com
 * 
 * This file is part of Frinika.
 * 
 * Frinika is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.

 * Frinika is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with Frinika; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package com.frinika.gui.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.ListCellRenderer;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * An alternative to a JTabbedPane for displaying complex forms.
 *
 * @author Jens Gulden
 */
public class PresentationPanel extends JPanel {
    
    private static final long serialVersionUID = 1L;
    
    protected final static Dimension LABEL_SIZE = new Dimension(100, 50);
    protected final static Color LABEL_COLOR_SELECTED = new Color(220, 220, 235);
    protected final static Color LABEL_BORDER_COLOR_SELECTED = Color.DARK_GRAY;
	
    protected ArrayList<String> titles = new ArrayList<String>();
    protected ArrayList<Icon> icons = new ArrayList<Icon>();
    protected ArrayList<Component> components = new ArrayList<Component>();
    
    /** Creates new form PresentationPanel */
    public PresentationPanel() {
        initComponents();
        naviList.setCellRenderer(new ListCellRenderer() {
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                if ((index >= 0) && (index < titles.size())) {
                    String title = titles.get(index);
                    Icon icon = icons.get(index); // may be null
                    JLabel label = new JLabel(title, icon, JLabel.CENTER);
                    label.setMinimumSize(LABEL_SIZE);
                    label.setPreferredSize(LABEL_SIZE);
                    if (isSelected || cellHasFocus) {
                    	label.setOpaque(true);
                        label.setBackground(LABEL_COLOR_SELECTED);
                        label.setBorder(new LineBorder(LABEL_BORDER_COLOR_SELECTED, 2));
                    } else {
                        //label.setBackground(Color.WHITE);
                    }
                    return label;
                } else {
                    return null;
                }
            }
        });
        naviList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
            	int index = naviList.getSelectedIndex();//e.getFirstIndex();
                setSelected(index);
            }
        });
    }
    
    /** Creates new form PresentationPanel */
    public PresentationPanel(JTabbedPane tabbedPane) {
        this();
        initFromTabbedPane(tabbedPane);
        setSelected(0);
    }
    
    public void initFromTabbedPane(JTabbedPane tabbedPane) {
        for (int index = 0; index < tabbedPane.getComponentCount(); index++) {
            String title = tabbedPane.getTitleAt(index);
            Icon icon = tabbedPane.getIconAt(index);
            Component component = tabbedPane.getComponent(index);
            this.titles.add(title);
            this.icons.add(icon);
            this.components.add(component);
        }
        naviList.setListData(new Object[this.titles.size()]);
    }
    
    public void setSelected(int index) {
        if (titles.isEmpty()) return;
        if ((index < 0) || (index >= titles.size())) {
            return;
        }
        Component component = components.get(index);
        contentPanel.removeAll();
        String title = titles.get(index);
        contentPanel.setBorder(new TitledBorder(title));
        contentPanel.add(component, BorderLayout.CENTER);
        contentPanel.validate();
        contentPanel.repaint();
        naviList.setSelectedIndex(index);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        splitPane = new javax.swing.JSplitPane();
        naviPanel = new javax.swing.JPanel();
        naviScrollPane = new javax.swing.JScrollPane();
        naviList = new javax.swing.JList();
        fillerPanel = new javax.swing.JPanel();
        contentPanel = new javax.swing.JPanel();
        initializingLabel = new javax.swing.JLabel();

        setLayout(new java.awt.BorderLayout());

        splitPane.setDividerLocation(150);
        splitPane.setDividerSize(4);
        naviPanel.setLayout(new java.awt.GridBagLayout());

        naviScrollPane.setViewportView(naviList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.5;
        naviPanel.add(naviScrollPane, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        naviPanel.add(fillerPanel, gridBagConstraints);

        splitPane.setLeftComponent(naviPanel);

        contentPanel.setLayout(new java.awt.BorderLayout());

        initializingLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        initializingLabel.setText("initializing...");
        contentPanel.add(initializingLabel, java.awt.BorderLayout.CENTER);

        splitPane.setRightComponent(contentPanel);

        add(splitPane, java.awt.BorderLayout.CENTER);

    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel contentPanel;
    private javax.swing.JPanel fillerPanel;
    private javax.swing.JLabel initializingLabel;
    private javax.swing.JList naviList;
    private javax.swing.JPanel naviPanel;
    private javax.swing.JScrollPane naviScrollPane;
    private javax.swing.JSplitPane splitPane;
    // End of variables declaration//GEN-END:variables
    
}
