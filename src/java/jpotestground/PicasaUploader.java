package jpotestground;

import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.media.MediaFileSource;
import com.google.gdata.data.photos.AlbumEntry;
import com.google.gdata.data.photos.PhotoEntry;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;
import jpo.dataModel.GroupInfo;
import jpo.dataModel.PictureInfo;
import jpo.dataModel.SortableDefaultMutableTreeNode;
import jpo.export.PicasaUploadRequest;
/*
 * PicasaUploader.java: class that can interacts with the Google Picasa (TM)
 * service using Google provided code. Copyright (C) 2012-2012 Richard
 * Eigenmann. This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or any later
 * version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. The license is in
 * gpl.txt. See http://www.gnu.org/copyleft/gpl.html for the details.
 */

/**
 * This class has the code to upload a set of pictures to a Picasa Album.
 *
 * @author Richard Eigenmann
 */
public class PicasaUploader {

    private PicasaUploadRequest myRequest;
    /**
     * Defines a logger for this class
     */
    private static final Logger LOGGER = Logger.getLogger ( PicasaUploader.class.getName () );
    private URL AlbumUrl;

    public PicasaUploader ( PicasaUploadRequest myRequest ) {
        this.myRequest = myRequest;

        GroupInfo groupInfo;
        try {
            groupInfo = (GroupInfo) myRequest.getNode ().getUserObject ();
        } catch ( ClassCastException ex ) {
            LOGGER.severe ( ex.getMessage () );
            return;
        }

        if ( !createAlbum ( groupInfo ) ) {
            LOGGER.severe ( "Could not create Album" );
            return;
        }

        PictureInfo pi;
        for ( SortableDefaultMutableTreeNode node : myRequest.getNode ().getChildPictureNodes ( false ) ) {
            pi = (PictureInfo) node.getUserObject ();
            postPicture ( pi );
            if ( myRequest.isInterrupt () ) {
                break;
            }
        }
    }

    public boolean login () {
        LOGGER.info ( "Authenticating on Google servers..." );
        try {
            myRequest.picasaWebService.setUserCredentials ( myRequest.getUsername (), myRequest.getPassword () );
            LOGGER.info ( "Login Successful" );
        } catch ( AuthenticationException ex ) {
            LOGGER.severe ( ex.getMessage () );
            return false;
        }
        return true;

    }

    public boolean createAlbum ( GroupInfo groupInfo ) {
        LOGGER.info ( "Creating Album" );
        AlbumEntry myAlbum = new AlbumEntry ();
        myAlbum.setTitle ( new PlainTextConstruct ( groupInfo.getGroupName () ) );
        myAlbum.setDescription ( new PlainTextConstruct ( groupInfo.getGroupName () ) );
        AlbumEntry insertedEntry;

        URL feedUrl;
        try {
            feedUrl = new URL ( myRequest.getFormattedPicasaUrl () );
        } catch ( MalformedURLException ex ) {
            LOGGER.severe ( ex.getMessage () );
            return false;
        }

        try {
            insertedEntry = myRequest.picasaWebService.insert ( feedUrl, myAlbum );
        } catch ( IOException ex ) {
            LOGGER.severe ( ex.getMessage () );
            return false;
        } catch ( ServiceException ex ) {
            LOGGER.severe ( ex.getMessage () );
            return false;
        }

        String albumIdUrl = insertedEntry.getId ();
        String albumId = albumIdUrl.substring ( albumIdUrl.lastIndexOf ( '/' ) + 1 );
        String albumPostUrl = myRequest.getFormattedPicasaUrl () + "/albumid/" + albumId;
        LOGGER.info ( String.format ( "raw: %s parsed: %s\nrecombined: %s", albumIdUrl, albumId, albumPostUrl ) );

        try {
            AlbumUrl = new URL ( albumPostUrl );
        } catch ( MalformedURLException ex ) {
            LOGGER.severe ( ex.getMessage () );
            return false;
        }
        LOGGER.info ( String.format ( "AlbumId: %s", AlbumUrl.toString () ) );
        return true;
    }

    public boolean postPicture ( PictureInfo pi ) {
        LOGGER.info ( "Posting Picture: " + pi.getDescription () );
        PhotoEntry myPhoto = new PhotoEntry ();
        myPhoto.setTitle ( new PlainTextConstruct ( pi.getDescription () ) );
        myPhoto.setDescription ( new PlainTextConstruct ( pi.getDescription () ) );
        myPhoto.setClient ( "JPO" );

        MediaFileSource myMedia = new MediaFileSource ( pi.getHighresFile (), "image/jpeg" );
        myPhoto.setMediaSource ( myMedia );
        try {
            PhotoEntry returnedPhoto = myRequest.picasaWebService.insert ( AlbumUrl, myPhoto );
        } catch ( IOException ex ) {
            LOGGER.severe ( ex.getMessage () );
            return false;
        } catch ( ServiceException ex ) {
            LOGGER.severe ( ex.getMessage () );
            return false;
        }
        return true;
    }
}