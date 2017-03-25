package fr.mogmi.syncsonic.model.directory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mogmi on 25/03/17.
 */

public class Directory {

    @SerializedName("child")
    @Expose
    public List<Child> childs;

}
