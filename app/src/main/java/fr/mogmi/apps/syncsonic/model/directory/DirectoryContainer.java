package fr.mogmi.apps.syncsonic.model.directory;

import com.google.gson.annotations.Expose;

import fr.mogmi.apps.syncsonic.model.BaseResponse;

/**
 * Created by mogmi on 25/03/17.
 */

public class DirectoryContainer extends BaseResponse {

    @Expose
    public Directory directory;
}
