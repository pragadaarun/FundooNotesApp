package com.example.fundoonotes.DashBoard.Fragments.Notes;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.fundoonotes.R;
import com.example.fundoonotes.UI.Activity.SharedPreferenceHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class UpdateNoteFragment extends Fragment {

    private static final String TAG = "UpdateNoteFragment";
    EditText updateNoteTitle, updateNoteDescription;
    Button updateButton, datePickerAction, timePickerAction;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;
    public Calendar dateTimeSchedule;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_note, container, false);
        String title = getArguments().getString("title");
        String description = getArguments().getString("description");
        String noteID = getArguments().getString("noteID");
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        updateNoteTitle = (EditText) view.findViewById(R.id.updateNoteTitle);
        updateNoteDescription = (EditText) view.findViewById(R.id.updateNoteDescription);
        updateButton =  view.findViewById(R.id.updateNoteButton);
        datePickerAction = view.findViewById(R.id.date_picker_action);
        timePickerAction = view.findViewById(R.id.time_picker_action);
        SharedPreferenceHelper sharedPreferenceHelper = new SharedPreferenceHelper(getContext());
        dateTimeSchedule = Calendar.getInstance();

        Log.e(TAG, "onCreateView: " + title);

        updateNoteTitle.setText(title);
        updateNoteDescription.setText(description);

        datePickerAction.setOnClickListener(v -> {
            DialogFragment datePicker = new DatePickerFragment();
            datePicker.show(getFragmentManager(), "date picker");
        } );

        timePickerAction.setOnClickListener(v -> {
            DialogFragment timePicker =new TimePickerFragment();
            timePicker.show(getFragmentManager(),"time picker");
        });

        updateButton.setOnClickListener(v -> {
            String newNoteTitle = updateNoteTitle.getText().toString();
            String newNoteDescription = updateNoteDescription.getText().toString();


            if (!newNoteTitle.isEmpty() && !newNoteDescription.isEmpty()) {
                firebaseFirestore=FirebaseFirestore.getInstance();
                DocumentReference documentReference = firebaseFirestore
                        .collection("users")
                        .document(firebaseUser.getUid())
                        .collection("notes").document(noteID);
                Map<String,Object> note=new HashMap<>();
                note.put("title", newNoteTitle);
                note.put("description", newNoteDescription);
                note.put("creationDate", System.currentTimeMillis());
                documentReference.set(note).addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(),"Note is updated",
                            Toast.LENGTH_SHORT).show();
                    sharedPreferenceHelper.setNoteTitle(newNoteTitle);
                    sharedPreferenceHelper.setNoteDescription(newNoteDescription);
                    startAlarm(dateTimeSchedule);
                    getFragmentManager().popBackStack();
                }).addOnFailureListener(e ->
                        Toast.makeText(getContext(),
                                "Failed To update",Toast.LENGTH_SHORT).show());
            } else {
                Toast.makeText(getContext(),"Both Fields are Required",Toast.LENGTH_SHORT).show();
            }

        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void timeView(Calendar timeScheduled) {
        String timeText = "Scheduled Time : ";

        timeText += DateFormat.getTimeInstance(DateFormat.SHORT).format(timeScheduled.getTime());

        Log.e(TAG, "timeView: " + timeText );
        timePickerAction.setText(timeText);
    }

    public void dateView(Calendar dateScheduled) {
        String dateText = "Scheduled Date : ";

        dateText += DateFormat.getDateInstance(DateFormat.SHORT).format(dateScheduled.getTime());

        Log.e(TAG, "dateView: " + dateText );
        datePickerAction.setText(dateText);
    }

    private void startAlarm(Calendar c) {
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 1, intent, 0);

        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

}