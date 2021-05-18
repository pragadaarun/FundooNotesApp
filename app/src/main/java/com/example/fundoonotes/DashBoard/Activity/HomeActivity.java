package com.example.fundoonotes.DashBoard.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import com.example.fundoonotes.DashBoard.Fragments.Notes.AddNoteFragment;
import com.example.fundoonotes.DashBoard.Fragments.ArchiveFragment;
import com.example.fundoonotes.DashBoard.Fragments.Notes.NotesFragment;
import com.example.fundoonotes.DashBoard.Fragments.TrashFragment;
import com.example.fundoonotes.HelperClasses.CallBack;
import com.example.fundoonotes.Firebase.DataManager.FirebaseUserManager;
import com.example.fundoonotes.Firebase.Model.FirebaseUserModel;
import com.example.fundoonotes.R;
import com.example.fundoonotes.DashBoard.Fragments.ReminderFragment;
import com.example.fundoonotes.UI.Activity.LoginRegisterActivity;
import com.example.fundoonotes.UI.Activity.SharedPreferenceHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity {

    private static final int ACTIVITY_READ_EXTERNAL_IMAGE_REQUEST_CODE = 1000;
    private static final int PERMISSION_READ_EXTERNAL_STORAGE_REQUEST_CODE = 201;
    public static FloatingActionButton addNote;
    FirebaseAuth firebaseAuth;
    private DrawerLayout drawer;
    SharedPreferenceHelper sharedPreferenceHelper;
    private final FirebaseUserManager firebaseUserManager = new FirebaseUserManager();
    private NotesFragment notesFragment;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();

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
        notesFragment = new NotesFragment();

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
        ImageView userDp = headerView.findViewById(R.id.user_profile);
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

        userDp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    Intent galleryIntent = new
                            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent,
                            ACTIVITY_READ_EXTERNAL_IMAGE_REQUEST_CODE);
                } else{
                    requestPermissions(new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    }, PERMISSION_READ_EXTERNAL_STORAGE_REQUEST_CODE);
                }
            }
        });

        StorageReference profileRef = storageReference.child("users/"+firebaseAuth.getCurrentUser().getUid()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(uri -> Picasso.get().load(uri).into(userDp));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_READ_EXTERNAL_STORAGE_REQUEST_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                Intent galleryIntent = new
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent,
                        ACTIVITY_READ_EXTERNAL_IMAGE_REQUEST_CODE);
            } else {
                // User refused to grant permission.
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ACTIVITY_READ_EXTERNAL_IMAGE_REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
                Uri imageUri = data.getData();
                uploadImageToFirebase(imageUri);
            }
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        firebaseAuth = FirebaseAuth.getInstance();
        final StorageReference fileRef = storageReference.child("users/"+firebaseAuth.getCurrentUser().getUid()+"/profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        ImageView userDp= findViewById(R.id.user_profile);
                        Picasso.get().load(uri).into(userDp);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Failed.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.note) {
            getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container,
                    notesFragment).commit();
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