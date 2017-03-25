package fr.mogmi.syncsonic.model.starred;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Data representing a starred album
 */
public class StarredAlbum {

    @Expose
    public String id;

    @Expose
    public String parent;

    @SerializedName("album")
    @Expose
    public String title;

    @Expose
    public int year;

    @Expose
    public String coverArt;
}
