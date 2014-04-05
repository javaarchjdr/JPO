package jpo.gui;

import jpo.dataModel.YearlyAnalysis;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;
import jpo.EventBus.JpoEventBus;
import jpo.EventBus.ShowQueryRequest;
import jpo.dataModel.Settings;
import jpo.dataModel.YearQuery;

/*
 YearlyAnalysisGuiController.java:  The controller that makes the GUI work

 Copyright (C) 2009-2014  Richard Eigenmann.
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
 * The controller that makes the GUI work
 *
 * @author Richard Eigenmann
 */
public class YearlyAnalysisGuiController {

    /**
     * Defines a logger for this class
     */
    private static final Logger LOGGER = Logger.getLogger( YearlyAnalysisGuiController.class.getName() );

    /**
     * Constructor to call to create a new YearlyAnalysisGui
     *
     * @param startNode
     */
    public YearlyAnalysisGuiController( DefaultMutableTreeNode startNode ) {
        YearlyAnalysis ya = new YearlyAnalysis( startNode );
        YearsAnalysisGui yag = new YearsAnalysisGui();
        yag.pack();
        yag.setLocationRelativeTo( Settings.anchorFrame );
        yag.setVisible( true );

        TreeMap<Integer, TreeMap<Integer, HashSet<DefaultMutableTreeNode>>> yearsMap = ya.getYearMap();

        JPanel panel = yag.getDisplayPanel();
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridy = 0;
        gc.anchor = GridBagConstraints.LINE_START;

        for ( Integer year : yearsMap.keySet() ) {
            gc.gridx = 0;
            panel.add( new JLabel( String.format( "Year: %4d", year ) ), gc );

            TreeMap<Integer, HashSet<DefaultMutableTreeNode>> monthMap = yearsMap.get( year );
            for ( Integer month : monthMap.keySet() ) {
                HashSet<DefaultMutableTreeNode> nodes = monthMap.get( month );
                gc.gridx = 1;
                panel.add( new JLabel( String.format( "%s", YearlyAnalysis.getMonthName( month ) ) ), gc );
                gc.gridx = 2;
                YearsAnalysisButton b = new YearsAnalysisButton( ya, year, month );
                panel.add( b, gc );
                gc.gridy++;
            }
        }
        panel.validate();

    }

    /**
     *
     * @author Richard Eigenmann
     */
    class YearsAnalysisButton
            extends JButton {

        /**
         * The maximum dynamic width we want to give this button in addition to
         * the minimum width
         */
        int maxWidth = 400;

        /**
         * The minimum width a button will always have;
         */
        int minimumWidth = 80;

        /**
         * The width this button will use.
         */
        int width;

        /**
         * The height for the button
         */
        int height = 16;

        public YearsAnalysisButton( YearlyAnalysis ya, final Integer year,
                Integer month ) {
            super( "Button" );
            int count = ya.getYearMap().get( year ).get( month ).size();
            setText( String.format( "Nodes: %d", count ) );
            width = (int) ( (double) count / ya.maxNodesPerMonthInAllYears() * maxWidth + minimumWidth );
            //setMinimumSize( buttonSize );
            //setMaximumSize( buttonSize );
            //setPreferredSize( buttonSize );
            addActionListener( new ActionListener() {

                @Override
                public void actionPerformed( ActionEvent e ) {
                    JpoEventBus.getInstance().post( new ShowQueryRequest( new YearQuery( year.toString() ) ) );
                }
            } );
            setBackground( GradientColor.getColor( GradientColor.BLACK_WHITE_COLORS, (double) count / ya.maxNodesPerMonthInAllYears() ) );
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension( width, height );
        }
    }
}
