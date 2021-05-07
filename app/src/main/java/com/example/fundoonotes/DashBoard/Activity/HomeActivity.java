package com.example.fundoonotes.DashBoard.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import com.example.fundoonotes.DashBoard.Fragments.AddNoteFragment;
import com.example.fundoonotes.DashBoard.Fragments.ArchiveFragment;
import com.example.fundoonotes.DashBoard.Fragments.NotesFragment;
import com.example.fundoonotes.DashBoard.Fragments.TrashFragment;
import com.example.fundoonotes.R;
import com.example.fundoonotes.DashBoard.Fragments.ReminderFragment;
import com.example.fundoonotes.UI.Activity.LoginRegisterActivity;
import com.example.fundoonotes.UI.Activity.SharedPreferenceHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    private DrawerLayout drawer;
    SharedPreferenceHelper sharedPreferenceHelper;
    FloatingActionButton addNote;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        firebaseAuth = FirebaseAuth.getInstance();
        sharedPreferenceHelper = new SharedPreferenceHelper(this);
        addNote = findViewById(R.id.add_note);

        addNote.setOnClickListener(v -> {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();

            fragmentTransaction.replace(R.id.home_fragment_container, new AddNoteFragment())
                    .addToBackStack(null).commit();
            addNote.hide();
        });
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
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.home_fragment_container,
                    new NotesFragment()).commit();
            navigationView.setCheckedItem(R.id.note);
        }
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.note) {
            getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container,
                    new NotesFragment()).commit();
        } else if (item.getItemId() == R.id.reminder) {
            getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container,
                    new ReminderFragment()).commit();
        } else if (item.getItemId() == R.id.archive) {
            getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container,
                    new ArchiveFragment()).commit();
        } else if (item.getItemId() == R.id.delete) {
            getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container,
                    new TrashFragment()).commit();
        } else if (item.getItemId() == R.id.logOut) {
            firebaseAuth.signOut();
            sharedPreferenceHelper.setIsLoggedIn(false);
            finish();
            Intent backToMain = new Intent(HomeActivity.this, LoginRegisterActivity.class);
            startActivity(backToMain);
            finish();
        }  else {
                //do nothing
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
//        } else if (getSupportFragmentManager().getBackStackEntryCount() == 0 ){
//            addNote.show();
//            getSupportFragmentManager().popBackStack();
//            fragmentTransaction.commit();
        }
        else {
            super.onBackPressed();
        }
    }
}