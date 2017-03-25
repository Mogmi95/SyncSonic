package fr.mogmi.syncsonic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import fr.mogmi.syncsonic.model.Ping;
import fr.mogmi.syncsonic.model.SubsonicResponse;
import fr.mogmi.syncsonic.model.starred.StarredAlbum;
import fr.mogmi.syncsonic.model.starred.StarredArtist;
import fr.mogmi.syncsonic.model.starred.StarredContainer;
import fr.mogmi.syncsonic.model.starred.StarredSong;
import fr.mogmi.syncsonic.network.SubsonicHelper;
import fr.mogmi.syncsonic.settings.SettingsActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private TextView mainText;

    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mainText = (TextView) findViewById(R.id.main_text);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sync();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(prefs.getString("server_url", ""))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Log.i("TAG", "LOGIN : " + prefs.getString("server_login", ""));
        Log.i("TAG", "MDP : " + prefs.getString("server_password", ""));

        SubsonicHelper subsonicHelper = new SubsonicHelper(
                retrofit,
                SubsonicHelper.DEFAULT_FORMAT,
                SubsonicHelper.DEFAULT_APP_NAME,
                SubsonicHelper.DEFAULT_APP_VERSION,
                prefs.getString("server_login", ""),
                prefs.getString("server_password", "")
        );

        Call<SubsonicResponse<Ping>> ping = subsonicHelper.getPing();

        Log.i("PING", String.valueOf(ping.request().url()));

        ping.enqueue(new Callback<SubsonicResponse<Ping>>() {
            @Override
            public void onResponse(Call<SubsonicResponse<Ping>> call, Response<SubsonicResponse<Ping>> response) {
                Log.i("PING", response.body().subsonicResponse.status);
            }

            @Override
            public void onFailure(Call<SubsonicResponse<Ping>> call, Throwable t) {
                t.printStackTrace();
            }
        });

        Call<SubsonicResponse<StarredContainer>> starred = subsonicHelper.getStarred();
        Log.i("STARRED", String.valueOf(starred.request().url()));
        starred.enqueue(new Callback<SubsonicResponse<StarredContainer>>() {
            @Override
            public void onResponse(Call<SubsonicResponse<StarredContainer>> call, Response<SubsonicResponse<StarredContainer>> response) {
                appendText("ARTISTS :");
                for (StarredArtist artist : response.body().subsonicResponse.starred.artists) {
                    appendText(artist.name);
                }
                appendText("ALBUMS :");
                for (StarredAlbum album : response.body().subsonicResponse.starred.albums) {
                    appendText("[" + album.year + "] " + album.title);
                }
                appendText("SONG :");
                for (StarredSong song : response.body().subsonicResponse.starred.songs) {
                    appendText(song.artist + " - " + song.title);
                }
            }

            @Override
            public void onFailure(Call<SubsonicResponse<StarredContainer>> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    private void appendText(String text) {
        mainText.append(text + "\n");
    }




}
