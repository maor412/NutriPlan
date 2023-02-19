package com.nutri.nutriplan;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentNutritionMenu#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentNutritionMenu extends Fragment {
    View view;
    RelativeLayout loadingView;
    ScrollView scrollView;
    TextView BreakfastTextView, lunchTextView, snackTextView, dinnerTextView, noteTextView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentNutritionMenu(Context context) {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentNutritionMenu.
     */
    // TODO: Rename and change types and number of parameters
    public FragmentNutritionMenu newInstance(String param1, String param2) {
        FragmentNutritionMenu fragment = new FragmentNutritionMenu(getContext());
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
        getMenu();
        return view;
    }

    private void getMenu() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Menus").child("1");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                BreakfastTextView.setText(dataSnapshot.child("Breakfast").getValue(String.class).replace("\\n", System.getProperty("line.separator")));
                lunchTextView.setText(dataSnapshot.child("Lunch").getValue(String.class).replace("\\n", System.getProperty("line.separator")));
                snackTextView.setText(dataSnapshot.child("Snack").getValue(String.class).replace("\\n", System.getProperty("line.separator")));
                dinnerTextView.setText(dataSnapshot.child("Dinner").getValue(String.class).replace("\\n", System.getProperty("line.separator")));
                noteTextView.setText(dataSnapshot.child("Note").getValue(String.class).replace("\\n", System.getProperty("line.separator")));
                scrollView.setVisibility(View.VISIBLE);
                loadingView.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


    }

    private void initViews(LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.fragment_nutrition_menu, container, false);
        BreakfastTextView = view.findViewById(R.id.Breakfast_text_view);
        lunchTextView = view.findViewById(R.id.lunch_text_view);
        snackTextView = view.findViewById(R.id.snack_text_view);
        dinnerTextView = view.findViewById(R.id.dinner_text_view);
        noteTextView = view.findViewById(R.id.note_text_view);
        scrollView = view.findViewById(R.id.scroll_view);
        loadingView = view.findViewById(R.id.loading_view);

    }
}