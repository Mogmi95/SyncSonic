package fr.mogmi.syncsonic.model.starred;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Contains all types of starred items
 */
public class Starred  {

    @SerializedName("artist")
    @Expose
    public List<StarredArtist> artists;

    @SerializedName("album")
    @Expose
    public List<StarredAlbum> albums;

    @SerializedName("song")
    @Expose
    public List<StarredSong> songs;
}
