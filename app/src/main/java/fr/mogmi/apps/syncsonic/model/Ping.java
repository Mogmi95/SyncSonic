package fr.mogmi.apps.syncsonic.model;

import com.google.gson.annotations.Expose;

/**
 * Data representing a StarredArtist item
 */
public class Ping extends BaseResponse {

    @Expose()
    public Error error;
}
