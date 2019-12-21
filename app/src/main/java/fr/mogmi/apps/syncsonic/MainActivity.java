package fr.mogmi.apps.syncsonic;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;

import fr.mogmi.apps.syncsonic.model.Ping;
import fr.mogmi.apps.syncsonic.model.SubsonicResponse;
import fr.mogmi.apps.syncsonic.model.directory.Child;
import fr.mogmi.apps.syncsonic.model.directory.DirectoryContainer;
import fr.mogmi.apps.syncsonic.model.starred.StarredAlbum;
import fr.mogmi.apps.syncsonic.model.starred.StarredArtist;
import fr.mogmi.apps.syncsonic.model.starred.StarredContainer;
import fr.mogmi.apps.syncsonic.model.starred.StarredSong;
import fr.mogmi.apps.syncsonic.network.SubsonicHelper;
import fr.mogmi.apps.syncsonic.settings.SettingsActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static fr.mogmi.apps.syncsonic.R.id.fab;

public class MainActivity extends AppCompatActivity {

    private final static int RC_PERMISSION_WRITE_EXTERNAL_STORAGE = 42;

    private RecyclerView recyclerView;
    private TextView mainTextView;
    private SubsonicHelper subsonicHelper;
    private DownloadManager mgr;
    private FloatingActionButton actionButton;

    private ArrayList<SyncedItem> syncedItems = new ArrayList<>();
    private DownloadsAdapter downloadsAdapter;
    private Retrofit retrofit;

    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mainTextView = (TextView) findViewById(R.id.main_text);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        downloadsAdapter = new DownloadsAdapter(syncedItems);
        recyclerView.setAdapter(downloadsAdapter);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        mgr = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

        actionButton = (FloatingActionButton) findViewById(fab);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sync();
            }
        });

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, RC_PERMISSION_WRITE_EXTERNAL_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == RC_PERMISSION_WRITE_EXTERNAL_STORAGE) {
            // TODO Handle permission
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkServerConnection();
    }

    private void checkServerConnection() {
        if (prefs.getString("server_login", null) == null
                || prefs.getString("server_password", null) == null
                || prefs.getString("server_url", null) == null) {
            recyclerView.setVisibility(View.GONE);
            actionButton.setVisibility(View.GONE);
            mainTextView.setText("Edit your server settings");
            return;
        }

        if (retrofit != null && subsonicHelper != null) {
            ping();
        } else {
            try {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(prefs.getString("server_url", ""))
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                subsonicHelper = new SubsonicHelper(
                        retrofit,
                        SubsonicHelper.DEFAULT_FORMAT,
                        SubsonicHelper.DEFAULT_APP_NAME,
                        SubsonicHelper.DEFAULT_APP_VERSION,
                        prefs.getString("server_login", ""),
                        prefs.getString("server_password", "")
                );

                ping();
            } catch (IllegalArgumentException ie) {
                mainTextView.setText("Check your server URL");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void updateView(boolean noProblem, String message) {
        if (noProblem) {
            recyclerView.setVisibility(View.VISIBLE);
            actionButton.setVisibility(View.VISIBLE);
            mainTextView.setText("Press sync");
        } else {
            recyclerView.setVisibility(View.GONE);
            actionButton.setVisibility(View.GONE);
            mainTextView.setText(message);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent openSettings = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(openSettings);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void sync() {
        updateView(true, null);
        mainTextView.setVisibility(View.GONE);

        Animation rotation = AnimationUtils.loadAnimation(this, R.anim.spin);
        rotation.setRepeatCount(Animation.INFINITE);
        actionButton.startAnimation(rotation);

        syncedItems.clear();
        downloadsAdapter.notifyDataSetChanged();
        Call<SubsonicResponse<StarredContainer>> starred = subsonicHelper.getStarred();
        Log.i("STARRED", String.valueOf(starred.request().url()));
        starred.enqueue(new Callback<SubsonicResponse<StarredContainer>>() {
            @Override
            public void onResponse(Call<SubsonicResponse<StarredContainer>> call, Response<SubsonicResponse<StarredContainer>> response) {
                for (StarredArtist artist : response.body().subsonicResponse.starred.artists) {
                    downloadDirectory(artist.id);
                }
                for (StarredAlbum album : response.body().subsonicResponse.starred.albums) {
                    downloadDirectory(album.id);
                }
                for (StarredSong song : response.body().subsonicResponse.starred.songs) {
                    startDownload(song.path, song.id, song.artist, song.title);
                }
                actionButton.clearAnimation();
            }

            @Override
            public void onFailure(Call<SubsonicResponse<StarredContainer>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void downloadDirectory(@NonNull String id) {
        Call<SubsonicResponse<DirectoryContainer>> directory = subsonicHelper.getDirectory(id);
        directory.enqueue(new Callback<SubsonicResponse<DirectoryContainer>>() {
            @Override
            public void onResponse(Call<SubsonicResponse<DirectoryContainer>> call, Response<SubsonicResponse<DirectoryContainer>> response) {
                for (Child child : response.body().subsonicResponse.directory.childs) {
                    if (child.isDirectory) {
                        downloadDirectory(child.id);
                    } else {
                        startDownload(child.path, child.id, child.artist, child.title);
                    }
                }
            }

            @Override
            public void onFailure(Call<SubsonicResponse<DirectoryContainer>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void ping() {
        final Call<SubsonicResponse<Ping>> ping = subsonicHelper.getPing();

        Log.i("PING", String.valueOf(ping.request().url()));

        ping.enqueue(new Callback<SubsonicResponse<Ping>>() {
            @Override
            public void onResponse(Call<SubsonicResponse<Ping>> call, Response<SubsonicResponse<Ping>> response) {
                Log.i("PING", response.body().subsonicResponse.status);
                if (response.body().subsonicResponse.status.equals("ok")) {
                    updateView(true, null);
                } else {
                    updateView(false, "Check your credentials");
                }
            }

            @Override
            public void onFailure(Call<SubsonicResponse<Ping>> call, Throwable t) {
                updateView(false, "Check your credentials");
                t.printStackTrace();
            }
        });
    }

    private void startDownload(@NonNull String path,
                               @NonNull String id,
                               @NonNull String artist,
                               @NonNull String title) {
        String dirPath = path.substring(0, path.lastIndexOf("/"));
        String filename = path.substring(path.lastIndexOf("/"), path.length());

        // Creating the directory
        File musicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
        String directoryPath = "Syncsonic/" + dirPath;
        File directory = new File(musicDirectory, directoryPath);
        directory.mkdirs();
        // Creating the file
        File file = new File(directory, filename);
        if (!file.exists()) {
            String url = subsonicHelper.getDownloadLink(id);
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            request.setDestinationUri(Uri.fromFile(file));
            request.setTitle(title);
            if (prefs.getBoolean("sync_wifi_only", true)) {
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
            }
            mgr.enqueue(request);
            syncedItems.add(new SyncedItem(true, artist, title));
        } else {
            syncedItems.add(new SyncedItem(false, artist, title));
        }
        downloadsAdapter.notifyDataSetChanged();
    }

}
