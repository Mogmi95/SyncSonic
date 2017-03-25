package fr.mogmi.syncsonic.network;

import fr.mogmi.syncsonic.model.Ping;
import fr.mogmi.syncsonic.model.SubsonicResponse;
import fr.mogmi.syncsonic.model.starred.StarredContainer;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by mogmi on 25/03/17.
 */

public interface SubsonicService {

    /**
     * A Ping request
     *
     * <p>
     *     <a href="http://www.subsonic.org/pages/api.jsp#ping">http://www.subsonic.org/pages/api.jsp#ping</a>
     * </p>
     *
     * @param format The response format (json, xml...)
     * @param app The name of the app
     * @param version The version
     * @param user The username
     * @param token The token for authentication
     * @param salt The salt used to generate the token
     * @return A {@link Call} for a ping request.
     */
    @GET("rest/ping.view")
    Call<SubsonicResponse<Ping>> ping(
            @Query("f") String format,
            @Query("c") String app,
            @Query("v") String version,
            @Query("u") String user,
            @Query("t") String token,
            @Query("s") String salt
    );

    /**
     * A GetStarred request
     *
     * <p>
     *     <a href="http://www.subsonic.org/pages/api.jsp#getStarred">http://www.subsonic.org/pages/api.jsp#getStarred</a>
     * </p>
     *
     * @param format The response format (json, xml...)
     * @param app The name of the app
     * @param version The version
     * @param user The username
     * @param token The token for authentication
     * @param salt The salt used to generate the token
     * @return A {@link Call} for a getStarred request.
     */
    @GET("rest/getStarred.view")
    Call<SubsonicResponse<StarredContainer>> getStarred(
            @Query("f") String format,
            @Query("c") String app,
            @Query("v") String version,
            @Query("u") String user,
            @Query("t") String token,
            @Query("s") String salt
    );

    @GET("rest/download.view")
    Call<Object> getDownload(
            @Query("f") String format,
            @Query("c") String app,
            @Query("v") String version,
            @Query("u") String user,
            @Query("t") String token,
            @Query("s") String salt,
            @Query("id") String id
    );
}
