package com.nutri.nutriplan;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nutri.nutriplan.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

// https://sites.google.com/view/nutriplan-privacy-policy

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentSignUp#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentSignUp extends Fragment {
    private Button mButton;
    View view;
    EditText fullName,age,weight,height;
    Spinner gender, exercise;
    TextView privacyPolicyTextView;
    CheckBox privacyPolicyCheckBox;
    RelativeLayout loadingView;
    ScrollView scrollView;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentSignUp() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment signInFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentSignUp newInstance(String param1, String param2) {
        FragmentSignUp fragment = new FragmentSignUp();
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
        checkExistingUser();
        setOnActions();

        return view;
    }

    private void checkExistingUser() {

        String phoneNumber = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("Users");



        usersRef.child(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    moveToMenu();
                }
                else {
                    scrollView.setVisibility(View.VISIBLE);
                    loadingView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
            }
        });







//        if(usersRef.child(phoneNumber).getKey() != null){
//            Fragment newFragment = new FragmentMenu();
//            FragmentTransaction transaction = getFragmentManager().beginTransaction();
//            transaction.replace(R.id.fragment_container, newFragment);
//            transaction.addToBackStack(null);
//            transaction.commit();
//        }


    }

    private void setOnActions() {
        mButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {

                if(!privacyPolicyCheckBox.isChecked()){
                    Toast.makeText(getApplicationContext(), "Please accept the privacy policy to continue", Toast.LENGTH_SHORT).show();

                } else {
                    uploadData();
                    moveToMenu();
                }
            }
        });

        privacyPolicyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://sites.google.com/view/nutriplan-privacy-policy"));
                startActivity(i);
            }
        });
    }

    private void moveToMenu() {
        Fragment newFragment = new FragmentMenu();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void uploadData() {
        String phoneNumber = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();

        Map<String, String> userData = new HashMap<>();
        userData.put("full_name", String.valueOf(fullName.getText()));
        userData.put("age", String.valueOf(age.getText()));
        userData.put("weight", String.valueOf(weight.getText()));
        userData.put("height", String.valueOf(height.getText()));
        userData.put("gender", (String) gender.getSelectedItem());
        userData.put("exercise", (String) exercise.getSelectedItem());


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("Users");


        usersRef.child(phoneNumber).setValue(userData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Firebase", "Data successfully uploaded");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Firebase", "Failed to upload data", e);
                    }
                });

    }

    @SuppressLint("MissingInflatedId")
    private void initViews(LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        mButton = view.findViewById(R.id.sign_up_Button);
        fullName = view.findViewById(R.id.full_name_text_field);
        gender = view.findViewById(R.id.spinner_gender);
        age = view.findViewById(R.id.age_text_field);
        weight = view.findViewById(R.id.weight_text_field);
        height = view.findViewById(R.id.height_text_field);
        exercise = view.findViewById(R.id.spinner_exercise);
        scrollView = view.findViewById(R.id.scroll_view);
        loadingView = view.findViewById(R.id.loading_view);
        privacyPolicyTextView = view.findViewById(R.id.privacy_policy_text_view);
        privacyPolicyCheckBox = view.findViewById(R.id.privacy_policy_check_box);

    }





}