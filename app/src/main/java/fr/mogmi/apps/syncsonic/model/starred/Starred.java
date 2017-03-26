package fr.mogmi.apps.syncsonic.model.starred;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains all types of starred items
 */
public class Starred  {

    @SerializedName("artist")
    @Expose
    public List<StarredArtist> artists = new ArrayList<>();

    @SerializedName("album")
    @Expose
    public List<StarredAlbum> albums = new ArrayList<>();

    @SerializedName("song")
    @Expose
    public List<StarredSong> songs = new ArrayList<>();
}
