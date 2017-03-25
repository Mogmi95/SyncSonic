package fr.mogmi.syncsonic.model.starred;

import com.google.gson.annotations.Expose;

/**
 * Data representing a starred song
 */
public class StarredSong {

    @Expose
    public String id;

    @Expose
    public String parent;

    @Expose
    public String album;

    @Expose
    public String title;

    @Expose
    public String artist;

    @Expose
    public String artistId;

    @Expose
    public String albumId;

    @Expose
    public int year;

    @Expose
    public int track;

    @Expose
    public String coverArt;

    @Expose
    public String path;

    @Expose
    public String suffix;
}
