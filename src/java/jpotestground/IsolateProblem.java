/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jpotestground;

import jpo.dataModel.DescriptionWordMap;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.Scrollable;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Richard Eigenmann
 */
public class IsolateProblem extends JFrame {

     /**
     * Defines a logger for this class
     */
    private final static Logger LOGGER = Logger.getLogger( IsolateProblem.class.getName() );

    public static void main( String[] args ) {
        Runnable r = new Runnable() {

            @Override
            public void run() {
                new IsolateProblem();
            }
        };
        SwingUtilities.invokeLater( r );


    }


    public IsolateProblem() {
        setPreferredSize( new Dimension( 400, 400 ) );
        this.getContentPane().add( new BorderLayoutPanel() );
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        pack();
        setVisible( true );
    }

    class BorderLayoutPanel extends JPanel {

        //JPanel labelPanel = new JPanel();
        JPanel labelPanel = new VerticalScrollPanel();
        //JPanel labelPanel = new ScrollSavvyJPanel();

        JScrollPane jsp = new JScrollPane( labelPanel );
        //TagCloudJPanel jsp = new TagCloudJPanel( null );

        //JPanel labelPanel = jsp.getMyPanel();
        JSlider slider = new JSlider( 1, 300 );


        public BorderLayoutPanel() {
            setLayout( new BorderLayout() );
            add( slider, BorderLayout.PAGE_START );
            slider.setValue( 1 );
            slider.addChangeListener( new ChangeListener() {

                @Override
                public void stateChanged( ChangeEvent e ) {
                    populateWithLabels();
                }
            } );

            //add( labelPanel, BorderLayout.CENTER );
            add( jsp, BorderLayout.CENTER );


            //dwm = new DescriptionWordMap( new PlayWithTagCloud.Cities(); );
            //availableWords = dwm.getMap().size();

            populateWithLabels();
        }


        public void populateWithLabels() {
            labelPanel.removeAll();
            int numberOfLabels = slider.getValue();
            for ( int i = 1; i <= numberOfLabels; i++ ) {
                labelPanel.add( new JLabel( "Label-" + Integer.toString( i ) ) );
            }
            //jsp.validate();
            labelPanel.validate();
            jsp.validate();
            labelPanel.repaint();
        }
    }

    class VerticalScrollPanel extends JPanel implements Scrollable {

        @Override
        public Dimension getPreferredScrollableViewportSize() {
            return getPreferredSize();

        }


        @Override
        public Dimension getPreferredSize() {
            Dimension d = new Dimension( 200, getPreferredHeight() );
            return d;
        }


        private int getPreferredHeight() {
            int rv = 0;
            for ( int k = 0, count = getComponentCount(); k < count; k++ ) {
                Component comp = getComponent( k );
                Rectangle r = comp.getBounds();
                int height = r.y + r.height;
                if ( height > rv ) {
                    rv = height;
                }
            }
            rv += ( (FlowLayout) getLayout() ).getVgap();
            return rv;
        }


        @Override
        public int getScrollableBlockIncrement( Rectangle visibleRect, int orientation, int direction ) {
            return 1;
        }


        @Override
        public boolean getScrollableTracksViewportHeight() {
            return false;
        }


        @Override
        public boolean getScrollableTracksViewportWidth() {
            return true;
        }


        @Override
        public int getScrollableUnitIncrement( Rectangle visibleRect, int orientation, int direction ) {
            return 1;
        }
    }

}
