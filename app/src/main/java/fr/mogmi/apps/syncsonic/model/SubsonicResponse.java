package fr.mogmi.apps.syncsonic.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Data representing a StarredArtist item
 */
public class SubsonicResponse<T extends BaseResponse> {

    @Expose()
    @SerializedName("subsonic-response")
    public T subsonicResponse;
}
