package fr.mogmi.syncsonic.model;

import com.google.gson.annotations.Expose;

/**
 * The basic information of a Subsonic Response
 */
public class BaseResponse {

    @Expose()
    public String status;

    @Expose()
    public String version;
}
