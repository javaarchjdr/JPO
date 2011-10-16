package jpo.gui.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import jpo.dataModel.PictureInfo;
import jpo.dataModel.Settings;
import jpo.dataModel.Tools;
import jpo.gui.PictureViewerActions;
import net.miginfocom.swing.MigLayout;

/*
PictureFrame.java:  Class that manages the frame and display of the Picutre

Copyright (C) 2002-2011  Richard Eigenmann.
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
 * Class that manages the frame and display of the Picture.
 * @author Richard Eigenmann
 */
public class PictureFrame {

    /**
     * Constructor. Initialises the GUI widgets.
     */
    public PictureFrame( final PictureViewerActions pictureViewerController ) {
        this.pictureViewerController = pictureViewerController;
        initGui();
        pictureJPanel.addKeyListener( myViewerKeyAdapter );
    }
    /**
     * A handle back to the Controller the keystrokes can request actions
     */
    private final PictureViewerActions pictureViewerController;
    /**
     * Defines a logger for this class
     */
    private static final Logger LOGGER = Logger.getLogger( PictureFrame.class.getName() );
    /**
     *   The pane that handles the image drawing aspects.
     **/
    private PicturePane pictureJPanel = new PicturePane();

    /**
     * Provides direct access to the panel that shows the picture.
     * @return the Picture Panel
     */
    public PicturePane getPictureJPanel() {
        return pictureJPanel;
    }
    /**
     *  Navigation Panel
     */
    private final PictureViewerNavBar navButtonPanel = new PictureViewerNavBar();

    /**
     * Provides direct access to the navButtonPanel
     * @return the navButtonPanel
     */
    public PictureViewerNavBar getPictureViewerNavBar() {
        return navButtonPanel;
    }
    /**
     *  The root JPanel
     */
    private final JPanel viewerPanel = new JPanel();
    /**
     *  The Window in which the viewer will place it's components.
     **/
    public ResizableJFrame myJFrame = new ResizableJFrame( viewerPanel );
    /**
     *   progress bar to track the pictures loaded so far
     */
    public final JProgressBar loadJProgressBar = new JProgressBar();
    /**
     *   This textarea shows the description of the picture being shown
     **/
    public JTextArea descriptionJTextField = new JTextArea();

    /**
     *  This method creates all the GUI widgets and connects them for the
     *  PictureViewer.
     */
    private void initGui() {
        Tools.checkEDT();

        viewerPanel.setBackground( Settings.PICTUREVIEWER_BACKGROUND_COLOR );
        viewerPanel.setOpaque( true );
        viewerPanel.setFocusable( false );

        viewerPanel.setLayout( new MigLayout( "insets 0", "[grow, fill]", "[grow, fill][]" ) );

        // Picture Painter Pane
        pictureJPanel.setBackground( Settings.PICTUREVIEWER_BACKGROUND_COLOR );
        pictureJPanel.setVisible( true );
        pictureJPanel.setOpaque( true );
        pictureJPanel.setFocusable( true );
        viewerPanel.add( pictureJPanel, "span, grow" );

        final JPanel lowerBar = new JPanel( new MigLayout( "insets 0, wrap 3", "[left][grow, fill][right]", "[]" ) );
        lowerBar.setBackground( Settings.PICTUREVIEWER_BACKGROUND_COLOR );
        lowerBar.setOpaque( true );
        lowerBar.setFocusable( false );

        loadJProgressBar.setPreferredSize( new Dimension( 120, 20 ) );
        loadJProgressBar.setMaximumSize( new Dimension( 140, 20 ) );
        loadJProgressBar.setMinimumSize( new Dimension( 80, 20 ) );
        loadJProgressBar.setBackground( Settings.PICTUREVIEWER_BACKGROUND_COLOR );
        loadJProgressBar.setBorderPainted( true );
        loadJProgressBar.setBorder( BorderFactory.createLineBorder( Color.gray, 1 ) );

        loadJProgressBar.setMinimum( 0 );
        loadJProgressBar.setMaximum( 100 );
        loadJProgressBar.setStringPainted( true );
        loadJProgressBar.setVisible( false );

        lowerBar.add( loadJProgressBar, "hidemode 2" );

        // The Description_Panel
        descriptionJTextField.setFont( Font.decode( Settings.jpoResources.getString( "PictureViewerDescriptionFont" ) ) );
        descriptionJTextField.setWrapStyleWord( true );
        descriptionJTextField.setLineWrap( true );
        descriptionJTextField.setEditable( true );
        descriptionJTextField.setForeground( Settings.PICTUREVIEWER_TEXT_COLOR );
        descriptionJTextField.setBackground( Settings.PICTUREVIEWER_BACKGROUND_COLOR );
        descriptionJTextField.setCaretColor( Settings.PICTUREVIEWER_TEXT_COLOR );
        descriptionJTextField.setOpaque( true );
        descriptionJTextField.setBorder( new EmptyBorder( 2, 12, 0, 0 ) );
        descriptionJTextField.setMinimumSize( new Dimension( 80, 26 ) );
        descriptionJTextField.addFocusListener( new FocusAdapter() {

            @Override
            public void focusLost( FocusEvent e ) {
                super.focusLost( e );
                updateDescription();
            }
        } );

        JScrollPane descriptionJScrollPane = new JScrollPane( JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER );
        descriptionJScrollPane.setViewportView( descriptionJTextField );
        descriptionJScrollPane.setBorder( new EmptyBorder( 0, 0, 0, 0 ) );
        descriptionJScrollPane.setBackground( Settings.PICTUREVIEWER_BACKGROUND_COLOR );
        descriptionJScrollPane.setOpaque( true );
        lowerBar.add( descriptionJScrollPane );

        navButtonPanel.setPictureViewer( pictureViewerController );
        lowerBar.add( navButtonPanel );
        viewerPanel.add( lowerBar );
    }


    /**
     * This method sends the text of the textbox to the pictureinfo.
     * This updates the description if it has changed.
     */
    private void updateDescription() {
        Object userObject = pictureViewerController.getCurrentNode().getUserObject();
        if ( userObject != null ) {
            if ( userObject instanceof PictureInfo ) {
                LOGGER.fine( "Sending description update to " + descriptionJTextField.getText() );
                ( (PictureInfo) userObject ).setDescription(
                        descriptionJTextField.getText() );
            }

        }
    }
    private ViewerKeyAdapter myViewerKeyAdapter = new ViewerKeyAdapter();

    private class ViewerKeyAdapter
            extends KeyAdapter {

        /**
         *  method that analysed the key that was pressed
         */
        @Override
        public void keyPressed( KeyEvent e ) {
            int k = e.getKeyCode();
            if ( ( k == KeyEvent.VK_I ) ) {
                pictureJPanel.cylceInfoDisplay();
            } else if ( ( k == KeyEvent.VK_N ) ) {
                pictureViewerController.requestNextPicture();
            } else if ( ( k == KeyEvent.VK_M ) ) {
                pictureViewerController.requestPopupMenu();
            } else if ( ( k == KeyEvent.VK_P ) ) {
                pictureViewerController.requestPriorPicture();
            } else if ( ( k == KeyEvent.VK_F ) ) {
                pictureViewerController.requestScreenSizeMenu();
            } else if ( ( k == KeyEvent.VK_SPACE ) || ( k == KeyEvent.VK_HOME ) ) {
                pictureViewerController.resetPicture();
            } else if ( ( k == KeyEvent.VK_PAGE_UP ) ) {
                pictureViewerController.zoomIn();
            } else if ( ( k == KeyEvent.VK_PAGE_DOWN ) ) {
                pictureViewerController.zoomOut();
            } else if ( ( k == KeyEvent.VK_1 ) ) {
                pictureJPanel.zoomFull();
            } else if ( ( k == KeyEvent.VK_UP ) || ( k == KeyEvent.VK_KP_UP ) ) {
                pictureJPanel.scrollDown();
            } else if ( ( k == KeyEvent.VK_DOWN ) || ( k == KeyEvent.VK_KP_DOWN ) ) {
                pictureJPanel.scrollUp();
            } else if ( ( k == KeyEvent.VK_LEFT ) || ( k == KeyEvent.VK_KP_LEFT ) ) {
                pictureJPanel.scrollRight();
            } else if ( ( k == KeyEvent.VK_RIGHT ) || ( k == KeyEvent.VK_KP_RIGHT ) ) {
                pictureJPanel.scrollLeft();
            } else {
                JOptionPane.showMessageDialog( myJFrame,
                        Settings.jpoResources.getString( "PictureViewerKeycodes" ),
                        Settings.jpoResources.getString( "PictureViewerKeycodesTitle" ),
                        JOptionPane.INFORMATION_MESSAGE );
            }
        }
    }
}