package com.yoursh.dfgden.yorsh.activities;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.yoursh.dfgden.yorsh.R;
import com.yoursh.dfgden.yorsh.fragments.GameFragment;
import com.yoursh.dfgden.yorsh.fragments.PlayersFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, android.support.v4.app.FragmentManager.OnBackStackChangedListener {

    private   ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setFirstFragment();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        getSupportFragmentManager().addOnBackStackChangedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount()==1){
                moveTaskToBack(true);
            } else super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case (R.id.nav_game):
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragmentContainer, GameFragment.getInstance()).
                        commit();
                break;
            case (R.id.nav_players):
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragmentContainer, PlayersFragment.getInstance()).
                        commit();
                break;
            case (R.id.nav_rules):

                break;
            case (R.id.nav_stats):
                break;
            case (R.id.nav_about):
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackStackChanged() {
        if (getSupportFragmentManager().getBackStackEntryCount() ==0) {
            finish();
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            toggle.setDrawerIndicatorEnabled(false);
            toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getSupportFragmentManager().popBackStackImmediate();
                }
            });
        } else {
            toggle.setDrawerIndicatorEnabled(true);
        }
    }

    private void setFirstFragment() {
        if (getSupportFragmentManager().findFragmentByTag("start") == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,
                    GameFragment.getInstance(), "start").addToBackStack(null).commit();
        }
    }


}
