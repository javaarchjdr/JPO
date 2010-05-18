package jpo.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;

/*
YearlyAnalysisGui.java:  Swing based Gui elements to show the Yearly Analysis

Copyright (C) 2009-2010  Richard Eigenmann.
This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or any later version. This program is distributed
in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS
FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
more details. You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
The license is in gpl.txt.
See http://www.gnu.org/copyleft/gpl.html for the details.
 */
/**
 * Swing based Gui elements to show the Yearly Analysis
 *
 * @author Richard Eigenmann
 */
public class YearsAnalysisGui extends JFrame {

    /** Creates new form YearsAnalysisGui */
    public YearsAnalysisGui() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        DisplayPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        DisplayPanel.setLayout(new java.awt.GridBagLayout());
        jScrollPane1.setViewportView(DisplayPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

  
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel DisplayPanel;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables


    /**
     * returns the display panel
     * @return the display panel
     */
    public JPanel getDisplayPanel() {
        return DisplayPanel;
    }
}
