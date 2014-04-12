package map;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.event.MouseInputListener;
import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.OSMTileFactoryInfo;
import org.jdesktop.swingx.input.CenterMapListener;
import org.jdesktop.swingx.input.PanKeyListener;
import org.jdesktop.swingx.input.PanMouseInputListener;
import org.jdesktop.swingx.input.ZoomMouseWheelListenerCursor;
import org.jdesktop.swingx.mapviewer.DefaultTileFactory;
import org.jdesktop.swingx.mapviewer.DefaultWaypoint;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdesktop.swingx.mapviewer.LocalResponseCache;
import org.jdesktop.swingx.mapviewer.TileFactoryInfo;
import org.jdesktop.swingx.mapviewer.Waypoint;
import org.jdesktop.swingx.mapviewer.WaypointPainter;
import org.jdesktop.swingx.painter.CompoundPainter;
import org.jdesktop.swingx.painter.Painter;

/**
 *
 * @author richi
 */
public class MapViewer {
    public JXMapViewer getMapViewer() {
        JXMapViewer mapViewer = new JXMapViewer();

        // Create a TileFactoryInfo for OpenStreetMap
        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory( info );
        mapViewer.setTileFactory( tileFactory );

        // Setup local file cache
        File cacheDir = new File( System.getProperty( "java.io.tmpdir" ) + File.separator + ".jxmapviewer2" );
        LocalResponseCache.installResponseCache( info.getBaseURL(), cacheDir, false );

        // Use 8 threads in parallel to load the tiles
        tileFactory.setThreadPoolSize( 8 );

        // Set the focus
        GeoPosition frankfurt = new GeoPosition( 50.11, 8.68 );

        mapViewer.setZoom( 7 );
        mapViewer.setAddressLocation( frankfurt );

        // Add interactions
        MouseInputListener mia = new PanMouseInputListener( mapViewer );
        mapViewer.addMouseListener( mia );
        mapViewer.addMouseMotionListener( mia );

        mapViewer.addMouseListener( new CenterMapListener( mapViewer ) );

        mapViewer.addMouseWheelListener( new ZoomMouseWheelListenerCursor( mapViewer ) );

        mapViewer.addKeyListener( new PanKeyListener( mapViewer ) );

        // Add a selection painter
        SelectionAdapter sa = new SelectionAdapter( mapViewer );
        SelectionPainter sp = new SelectionPainter( sa );
        mapViewer.addMouseListener( sa );
        mapViewer.addMouseMotionListener( sa );
        mapViewer.setOverlayPainter( sp );

        GeoPosition frankfurt2 = new GeoPosition( 50, 7, 0, 8, 41, 0 );
        GeoPosition wiesbaden = new GeoPosition( 50, 5, 0, 8, 14, 0 );
        GeoPosition mainz = new GeoPosition( 50, 0, 0, 8, 16, 0 );
        GeoPosition darmstadt = new GeoPosition( 49, 52, 0, 8, 39, 0 );
        GeoPosition offenbach = new GeoPosition( 50, 6, 0, 8, 46, 0 );

        // Create a track from the geo-positions
        List<GeoPosition> track = Arrays.asList( frankfurt2, wiesbaden, mainz, darmstadt, offenbach );
        RoutePainter routePainter = new RoutePainter( track );

        // Set the focus
        //mapViewer.zoomToBestFit new HashSet<GeoPosition>( track ), 0.7 );

        // Create waypoints from the geo-positions
        Set<Waypoint> waypoints = new HashSet<Waypoint>( Arrays.asList(
                new DefaultWaypoint( frankfurt2 ),
                new DefaultWaypoint( wiesbaden ),
                new DefaultWaypoint( mainz ),
                new DefaultWaypoint( darmstadt ),
                new DefaultWaypoint( offenbach ) ) );

        // Create a waypoint painter that takes all the waypoints
        WaypointPainter<Waypoint> waypointPainter = new WaypointPainter<Waypoint>();
        waypointPainter.setWaypoints( waypoints );

        // Create a compound painter that uses both the route-painter and the waypoint-painter
        List<Painter<JXMapViewer>> painters = new ArrayList<Painter<JXMapViewer>>();
        painters.add( routePainter );
        painters.add( waypointPainter );

        CompoundPainter<JXMapViewer> painter = new CompoundPainter<JXMapViewer>( painters );
        mapViewer.setOverlayPainter( painter );
        
        return mapViewer;
    }
}
