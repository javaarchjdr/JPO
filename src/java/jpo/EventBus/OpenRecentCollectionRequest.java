package jpo.EventBus;

/**
 * This request indicates that the user wants open a recent collection
 * ToDo: consider whether this request should be integrated with the FileLoadRequest
 * 
 * It will not check for
 * unsaved updates. To check for those wrap this in a UnsavedUpdatesDialogRequest:
 * 
 * JpoEventBus.getInstance().post( new UnsavedUpdatesDialogRequest( new OpenRecentCollectionRequest())  );
 
 *
 * @author Richard eigenmann
 */
public class OpenRecentCollectionRequest implements Request {

    private final int i;

    /**
     * A request to load a file
     * @param i the index in the {@link jpo.dataModel.Settings#recentCollections} array
     *			indicating the file to load.
     */
    public OpenRecentCollectionRequest( int i ) {
        this.i = i;
    }
    
    public int getI() {
        return i;
    }

}
