package com.example.fundoonotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import static com.example.fundoonotes.LoginFragment.IS_LOGGED_IN;
import static com.example.fundoonotes.LoginFragment.SHARED_PREFERENCES;

public class NotesActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_header_container);
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new NotesFragment()).commit();
            navigationView.setCheckedItem(R.id.note);
        }
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.note:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new NotesFragment()).commit();
                break;
            case R.id.remainder:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ReminderFragment()).commit();
                break;
            case R.id.archive:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ArchiveFragment()).commit();
                break;
            case R.id.logout:
                mAuth.signOut();
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(IS_LOGGED_IN, false);
                editor.apply();
                Intent backToMain = new Intent(NotesActivity.this, MainActivity.class);
                startActivity(backToMain);
                finish();

                break;
            case R.id.delete:
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}