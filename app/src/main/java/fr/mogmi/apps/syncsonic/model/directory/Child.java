package fr.mogmi.apps.syncsonic.model.directory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mogmi on 25/03/17.
 */

public class Child {

    @Expose
    public String id;

    @SerializedName("isDir")
    @Expose
    public boolean isDirectory;

    // Only for files (= songs)

    @Expose
    public String title;

    @Expose
    public String artist;

    @Expose
    public String path;

    @Expose
    public String coverArt;
}
