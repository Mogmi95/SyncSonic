package fr.mogmi.syncsonic.network;

import android.support.annotation.NonNull;

import fr.mogmi.syncsonic.model.Ping;
import fr.mogmi.syncsonic.model.SubsonicResponse;
import fr.mogmi.syncsonic.model.starred.StarredContainer;
import fr.mogmi.syncsonic.tools.SecurityHelper;
import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * A Singleton that is able to generate Retrofit Call for Subsonic API
 */
public class SubsonicHelper {

    public final static String DEFAULT_FORMAT = "json";
    public final static String DEFAULT_APP_VERSION = "1.13.0";
    public final static String DEFAULT_APP_NAME = "syncsonic";

    @NonNull
    private SubsonicService subsonicService;

    @NonNull
    private String format;

    @NonNull
    private String app;

    @NonNull
    private String version;

    @NonNull
    private String username;

    @NonNull
    private String password;

    public SubsonicHelper(@NonNull Retrofit retrofit,
                          @NonNull String format,
                          @NonNull String app,
                          @NonNull String version,
                          @NonNull String username,
                          @NonNull String password) {
        subsonicService = retrofit.create(SubsonicService.class);

        this.format = format;
        this.app = app;
        this.version = version;
        this.username = username;
        this.password = password;
    }


    /**
     * Ping the server.
     *
     * @return A retrofit {@link Call}
     */
    public Call<SubsonicResponse<Ping>> getPing() {
        String salt = SecurityHelper.randomString(20);
        return subsonicService.ping(
                format,
                app,
                version,
                username,
                SecurityHelper.md5(password + salt),
                salt
        );
    }

    /**
     * Retrieve the list of starred items for the user
     *
     * @return A retrofit {@link Call}
     */
    public Call<SubsonicResponse<StarredContainer>> getStarred() {
        String salt = SecurityHelper.randomString(20);
        return subsonicService.getStarred(
                format,
                app,
                version,
                username,
                SecurityHelper.md5(password + salt),
                salt
        );
    }

    /**
     * Retrieve an URL to download a file from the server
     *
     * @param id The id of the element to download
     * @return A {@link String} representing an url linking to the file
     */
    public String getDownloadLink(@NonNull String id) {
        String salt = SecurityHelper.randomString(20);
        Call call = subsonicService.getDownload(
                format,
                app,
                version,
                username,
                SecurityHelper.md5(password + salt),
                salt,
                id
        );

        return call.request().url().toString();
    }
}
