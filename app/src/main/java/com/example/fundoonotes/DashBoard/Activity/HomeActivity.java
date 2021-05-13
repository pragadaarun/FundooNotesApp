package com.example.fundoonotes.DashBoard.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import com.example.fundoonotes.DashBoard.Fragments.AddNoteFragment;
import com.example.fundoonotes.DashBoard.Fragments.ArchiveFragment;
import com.example.fundoonotes.DashBoard.Fragments.NotesFragment;
import com.example.fundoonotes.DashBoard.Fragments.TrashFragment;
import com.example.fundoonotes.Firebase.CallBack;
import com.example.fundoonotes.Firebase.FirebaseNoteModel;
import com.example.fundoonotes.Firebase.FirebaseUserManager;
import com.example.fundoonotes.Firebase.FirebaseUserModel;
import com.example.fundoonotes.Firebase.NoteAdapter;
import com.example.fundoonotes.R;
import com.example.fundoonotes.DashBoard.Fragments.ReminderFragment;
import com.example.fundoonotes.UI.Activity.LoginRegisterActivity;
import com.example.fundoonotes.UI.Activity.SharedPreferenceHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    public static FloatingActionButton addNote;
    FirebaseAuth firebaseAuth;
    private DrawerLayout drawer;
    SharedPreferenceHelper sharedPreferenceHelper;
    private final FirebaseUserManager firebaseUserManager = new FirebaseUserManager();

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
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.home_fragment_container, new AddNoteFragment())
                    .addToBackStack(null).commit();
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
        View headerView = navigationView.getHeaderView(0);
        TextView userName = headerView.findViewById(R.id.user_name_display);
        TextView userEmail = headerView.findViewById(R.id.user_email_display);
        firebaseUserManager.getUserDetails(new CallBack<FirebaseUserModel>() {
            @Override
            public void onSuccess(FirebaseUserModel data) {
                userName.setText(data.getUserName());
                userEmail.setText(data.getUserEmail());
            }

            @Override
            public void onFailure(Exception exception) {
                Toast.makeText(HomeActivity.this,
                        "Something went Wrong",
                        Toast.LENGTH_SHORT).show();
            }
        });
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
        }
        else {
            super.onBackPressed();
        }
    }
}