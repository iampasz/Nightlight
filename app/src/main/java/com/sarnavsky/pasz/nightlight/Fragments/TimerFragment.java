package com.sarnavsky.pasz.nightlight.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.sarnavsky.pasz.nightlight.MainActivity;
import com.sarnavsky.pasz.nightlight.R;


public class TimerFragment extends Fragment {

    ConstraintLayout timer_constrain;

    TextView timer_text;

    public static TimerFragment init(){
        return new TimerFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.timer_fragment, container,  false);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Spinner spinner_hours = (Spinner) view.findViewById(R.id.spinner_hours);
        Spinner spinner_minutes = (Spinner) view.findViewById(R.id.spinner_minutes);
        Integer[] items_hours = new Integer[]{0, 1, 2, 3, 4, 6};
        ArrayAdapter<Integer> adapter_hours = new ArrayAdapter<Integer>(getContext(),R.layout.simple_spinner_item, items_hours);
        adapter_hours.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spinner_hours.setAdapter(adapter_hours);
        spinner_hours.setSelection(0);

        Integer[] items_minutes = new Integer[]{0, 1, 5, 10, 20, 30, 40, 50};
        ArrayAdapter<Integer> adapter_minutes = new ArrayAdapter<Integer>(getContext(),R.layout.simple_spinner_item, items_minutes);
        adapter_minutes.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);

        spinner_minutes.setAdapter(adapter_minutes);
        spinner_minutes.setSelection(0);


        timer_constrain =  view.findViewById(R.id.timer_constrain);

        timer_constrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // MainFragment mainFragment = (MainFragment) getParentFragmentManager().findFragmentByTag("main_fragment");
                ((MainActivity)getActivity()).startTimer((int)spinner_hours.getSelectedItem(), (int) spinner_minutes.getSelectedItem());
                removeThisFragment();
            }
        });

    }

    private void removeThisFragment(){
        FragmentManager fm = getParentFragmentManager();
        fm.beginTransaction().remove(TimerFragment.this).commit();
        ((MainActivity)getActivity()).clearAlpha();
    }
}
