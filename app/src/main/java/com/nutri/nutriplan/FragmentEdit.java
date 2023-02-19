package com.nutri.nutriplan;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;

import com.nutri.nutriplan.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentEdit#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentEdit extends Fragment {
    private Button saveButton;
    View view;
    EditText fullName,age,weight,height;
    Spinner exercise;
    RelativeLayout loadingView;
    ScrollView scrollView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentEdit() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentEdit.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentEdit newInstance(String param1, String param2) {
        FragmentEdit fragment = new FragmentEdit();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        initViews(inflater,container);
        setProperties();
        setOnClickListeners();
        return view;
    }

    private void initViews(LayoutInflater inflater, ViewGroup container) {

        view = inflater.inflate(R.layout.fragment_edit, container, false);
        saveButton = view.findViewById(R.id.save_Button);
        fullName = view.findViewById(R.id.full_name_text_field);
        age = view.findViewById(R.id.age_text_field);
        weight = view.findViewById(R.id.weight_text_field);
        height = view.findViewById(R.id.height_text_field);
        exercise = view.findViewById(R.id.spinner_exercise);
        scrollView = view.findViewById(R.id.scroll_view);
        loadingView = view.findViewById(R.id.loading_view);
    }

    private void setProperties() {
        String phoneNumber = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("Users").child(phoneNumber);


        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fullName.setText(dataSnapshot.child("full_name").getValue(String.class));
                age.setText(dataSnapshot.child("age").getValue(String.class));
                weight.setText(dataSnapshot.child("weight").getValue(String.class));
                height.setText(dataSnapshot.child("height").getValue(String.class));
                scrollView.setVisibility(View.VISIBLE);
                loadingView.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void setOnClickListeners() {

        saveButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                saveAndExit();
            }
        });
    }

    private void saveAndExit() {
        String phoneNumber = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("Users").child(phoneNumber);
        usersRef.child("full_name").setValue(String.valueOf(fullName.getText()));
        usersRef.child("age").setValue(String.valueOf(age.getText()));
        usersRef.child("weight").setValue(String.valueOf(weight.getText()));
        usersRef.child("height").setValue(String.valueOf(height.getText()));
        if(!exercise.getSelectedItem().equals("Excerscise?"))
            usersRef.child("exercise").setValue((String) exercise.getSelectedItem());
        closeFragment();
    }

    private void closeFragment() {
        FragmentManager fragmentManager = getFragmentManager();

        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            getActivity().finish();
        }
    }
}