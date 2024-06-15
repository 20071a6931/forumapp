package com.example.forumapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.forumapp.Fragment.ChangePassFrag;
import com.example.forumapp.Fragment.FavouriteFrag;
import com.example.forumapp.Fragment.Home;
import com.example.forumapp.Fragment.ProfileFrag;
import com.example.forumapp.Util.Util;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        toolbar = (Toolbar) findViewById(R.id.toolbaar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);

        // Enable the Up button in the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Home home = new Home();
        replace(home);
        // Handle item clicks in the navigation drawer
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Handle item clicks here
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    toolbar.setTitle("Home");
                    Home home = new Home();
                    replace(home);
                } else if (itemId == R.id.nav_favourite) {
                    toolbar.setTitle("Favourites");
                    FavouriteFrag cp = new FavouriteFrag();
                    replace(cp);
                } else if (itemId == R.id.nav_cp) {
                    toolbar.setTitle("Change Password");
                    ChangePassFrag cp = new ChangePassFrag();
                    replace(cp);
                }  else if (itemId == R.id.nav_profile) {
                    toolbar.setTitle("Profile");
                    ProfileFrag frag = new ProfileFrag();
                    replace(frag);

                } else if (itemId == R.id.nav_logout) {

                    Util.setSP(getApplicationContext(), "");
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                } else {
                    Home home = new Home();
                    replace(home);
                }

                // Close the drawer
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle the ActionBarDrawerToggle
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

    void replace(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_content, fragment); // replace a Fragment with Frame Layout
        transaction.commit();

    }
}