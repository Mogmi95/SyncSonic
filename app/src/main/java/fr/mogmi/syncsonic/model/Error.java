package fr.mogmi.syncsonic.model;

import com.google.gson.annotations.Expose;

/**
 * Created by mogmi on 25/03/17.
 */

public class Error {
    @Expose()
    public int code;
    @Expose()
    public String message;
}
