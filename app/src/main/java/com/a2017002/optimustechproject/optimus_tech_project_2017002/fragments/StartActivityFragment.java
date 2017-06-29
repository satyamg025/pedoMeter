package com.a2017002.optimustechproject.optimus_tech_project_2017002.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.a2017002.optimustechproject.optimus_tech_project_2017002.Activity.NavigationActivity;
import com.a2017002.optimustechproject.optimus_tech_project_2017002.Activity.PedoActivity;
import com.a2017002.optimustechproject.optimus_tech_project_2017002.R;

/**
 * Created by satyam on 28/6/17.
 */

public class StartActivityFragment extends Fragment {

    View parentView;
    Button activity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_start_activity, container, false);

        activity=(Button)parentView.findViewById(R.id.activity);
        activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),PedoActivity.class));
            }
        });

        return parentView;
    }
}
