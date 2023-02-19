package com.nutri.nutriplan;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentProfile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentProfile extends Fragment {
    View view;
    RelativeLayout loadingView;
    ScrollView scrollView;
    RelativeLayout scrollViewBackground;
    ImageButton editButton;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentProfile(Context context) {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentProfile.
     */
    // TODO: Rename and change types and number of parameters
    public FragmentProfile newInstance(String param1, String param2) {
        FragmentProfile fragment = new FragmentProfile(getContext());
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
        initViews(inflater, container);
        setOnActions();
        return view;
    }

    private void setOnActions() {
        editButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {

                moveToMenu();

            }
        });
    }

    private void moveToMenu() {
        Fragment newFragment = new FragmentEdit();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void initViews(LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        TextView fullName = view.findViewById(R.id.full_name_text_field);
        TextView age = view.findViewById(R.id.age_text_field);
        TextView weight = view.findViewById(R.id.weight_text_field);
        TextView height = view.findViewById(R.id.height_text_field);
        TextView exercise = view.findViewById(R.id.exercise_text_field);
        scrollView = view.findViewById(R.id.scroll_view);
        scrollViewBackground = view.findViewById(R.id.scroll_view_background);
        loadingView = view.findViewById(R.id.loading_view);
        editButton = view.findViewById(R.id.button_edit);



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
                exercise.setText(dataSnapshot.child("exercise").getValue(String.class));
                scrollView.setVisibility(View.VISIBLE);
                loadingView.setVisibility(View.GONE);
                scrollViewBackground.setVisibility(View.VISIBLE);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }
}