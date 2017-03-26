package fr.mogmi.apps.syncsonic;

/**
 * Created by mogmi on 25/03/17.
 */

public class SyncedItem {
    public boolean isNew;
    public String artist;
    public String title;

    public SyncedItem(boolean isNew, String artist, String title) {
        this.isNew = isNew;
        this.artist = artist;
        this.title = title;
    }
}
