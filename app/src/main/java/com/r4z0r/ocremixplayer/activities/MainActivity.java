package com.r4z0r.ocremixplayer.activities;

import android.content.ComponentName;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.session.MediaController;
import androidx.media3.session.SessionToken;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.r4z0r.ocremixplayer.Global;
import com.r4z0r.ocremixplayer.OCRemixPlayerApplication;
import com.r4z0r.ocremixplayer.R;
import com.r4z0r.ocremixplayer.components.MiniPlayer;
import com.r4z0r.ocremixplayer.databinding.ActivityMainBinding;
import com.r4z0r.ocremixplayer.services.PlaybackService;

import java.util.concurrent.ExecutionException;

@UnstableApi
public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private MiniPlayer miniPlayer;

    private static final String TAG = "MainActivity";
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onStart() {
        super.onStart();
        Global global = OCRemixPlayerApplication.mInstance.getGlobal();

        global.setSessionToken(new SessionToken(this, new ComponentName(getApplicationContext(), PlaybackService.class)));
        ListenableFuture<MediaController> mediacontroleFuture = new MediaController.Builder(getApplicationContext(), global.getSessionToken()).buildAsync();
        mediacontroleFuture.addListener(() -> {
            if (mediacontroleFuture.isDone()) {
                try {
                    global.setMediaController(mediacontroleFuture.get());
                    miniPlayer.setListeners();
                } catch (ExecutionException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, MoreExecutors.directExecutor());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        OCRemixPlayerApplication.mInstance.getGlobal().setNavController(Navigation.findNavController(this, R.id.nav_host_fragment_content_main));
        NavController navController = OCRemixPlayerApplication.mInstance.getGlobal().getNavController();

        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        navController.addOnDestinationChangedListener((navController1, navDestination, bundle) -> {
            if (navDestination.getId() == R.id.SearchFragment) {
                bottomNavigationView.getMenu().findItem(R.id.menu_btn_search).setChecked(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                getSupportActionBar().setHomeButtonEnabled(false);
            } else if (navDestination.getId() == R.id.LastUpdatesFragment) {
                bottomNavigationView.getMenu().findItem(R.id.menu_btn_releases).setChecked(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                getSupportActionBar().setHomeButtonEnabled(false);
            } else if (navDestination.getId() == R.id.PlaylistFragment) {
                bottomNavigationView.getMenu().findItem(R.id.menu_btn_playlist).setChecked(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                getSupportActionBar().setHomeButtonEnabled(false);
            }

        });

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.menu_btn_search) {
                navController.navigate(R.id.SearchFragment);
                return true;
            } else if (item.getItemId() == R.id.menu_btn_releases) {
                navController.navigate(R.id.LastUpdatesFragment);
                return true;
            } else if (item.getItemId() == R.id.menu_btn_playlist) {
                navController.navigate(R.id.PlaylistFragment);
                return true;
            }
            return false;
        });

        miniPlayer = binding.miniPlayer;

        if (OCRemixPlayerApplication.mInstance.getGlobal().getMediaController()!= null
                && OCRemixPlayerApplication.mInstance.getGlobal().getMediaController().isPlaying()) {
            miniPlayer.setVisibility(View.VISIBLE);
        } else {
            miniPlayer.setVisibility(View.GONE);
        }
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return (NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp());
    }
}