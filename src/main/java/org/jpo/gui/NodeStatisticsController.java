package org.jpo.gui;

import org.jpo.cache.ThumbnailCreationQueue;
import org.jpo.dataModel.NodeStatistics;
import org.jpo.dataModel.NodeStatisticsBean;
import org.jpo.dataModel.Settings;
import org.jpo.dataModel.Tools;
import org.jpo.gui.swing.NodeStatisticsPanel;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.logging.Logger;

/*
NodeStatisticsController.java: a controller that makes the NodeStatisticsPanel show interesting stats

Copyright (C) 2002 - 2015  Richard Eigenmann.
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
 *  a panel that shows some counts about the collection
 */
public class NodeStatisticsController {

    /**
     * Defines a logger for this class
     */
    private static final Logger LOGGER = Logger.getLogger( NodeStatisticsController.class.getName() );
    
    /**
     * The NodeStatistics object that gives the results
     */
    private final NodeStatistics nodeStatistics = new NodeStatistics( null );

    
    private NodeStatisticsPanel nodeStatisticsPanel;

    
    /**
     * Returns the JComponent representing the statistics
     * @return the JComponent
     */
    public JComponent getJComponent(){
        // lazy instantiation (is it worth it?)
        if ( nodeStatisticsPanel == null ) {
            nodeStatisticsPanel = new NodeStatisticsPanel();
        }
        return nodeStatisticsPanel;
    }
        
    /**
     *  This method will update the statistics based on the supplied input node.
     *  @param  statisticsNode   The node that is being analysed.
     */
    public void updateStats( DefaultMutableTreeNode statisticsNode ) {
        if ( Settings.getPictureCollection().fileLoading ) {
            LOGGER.info( "Still busy loading the file. Aborting" );
            return;
        }

        if ( nodeStatistics.getNode() != statisticsNode ) {
            nodeStatistics.setNode( statisticsNode );
        }
        updateStats();

    }

    /**
     *  This method will update the statistics based on the current Node Statistics object.
     *  It has an embedded SwingWorker class to do the stats work on a background thread and
     *  then offload the updates into the EDT.
     */
    public void updateStats() {
        class updateStatsWorker extends SwingWorker<Object, Object> {
            private final NodeStatisticsBean nodeStatisticsBean = new NodeStatisticsBean();

            @Override
            public String doInBackground() {
                nodeStatisticsBean.setNumberOfNodes( nodeStatistics.getNumberOfNodesString() );
                nodeStatisticsBean.setNumberOfGroups( nodeStatistics.getNumberOfGroupsString() );
                nodeStatisticsBean.setNumberOfPictures( nodeStatistics.getNumberOfPicturesString() );
                nodeStatisticsBean.setSizeOfPictures( nodeStatistics.getSizeOfPicturesString() );

                if ( Settings.writeLog ) {
                    nodeStatisticsBean.setFreeMemory( Tools.freeMemory() );
                    nodeStatisticsBean.setQueueCount( Settings.jpoResources.getString( "queCountJLabel" ) + ThumbnailCreationQueue.size() );
                    nodeStatisticsBean.setSelectedCount( String.format( "Selected: %d", Settings.getPictureCollection().getSelection().size() ) );
                }
                return "done";
            }

            @Override
            protected void done() {
                try {
                    nodeStatisticsPanel.updateStats( nodeStatisticsBean );
                } catch ( Exception ignore ) {
                }
            }
        }

        ( new updateStatsWorker() ).execute();
    }
}
