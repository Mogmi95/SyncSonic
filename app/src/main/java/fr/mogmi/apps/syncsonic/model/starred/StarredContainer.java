package fr.mogmi.apps.syncsonic.model.starred;

import com.google.gson.annotations.Expose;

import fr.mogmi.apps.syncsonic.model.BaseResponse;

/**
 * Created by mogmi on 25/03/17.
 */

public class StarredContainer extends BaseResponse {

    @Expose
    public Starred starred;

}
