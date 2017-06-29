package com.a2017002.optimustechproject.optimus_tech_project_2017002.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.a2017002.optimustechproject.optimus_tech_project_2017002.Interface.VideoRequest;
import com.a2017002.optimustechproject.optimus_tech_project_2017002.R;
import com.a2017002.optimustechproject.optimus_tech_project_2017002.adapter.adapter_videos;
import com.a2017002.optimustechproject.optimus_tech_project_2017002.models.VideoDataPOJO;
import com.a2017002.optimustechproject.optimus_tech_project_2017002.networking.ServiceGenerator;
import com.a2017002.optimustechproject.optimus_tech_project_2017002.util.ColoredSnackbar;
import com.a2017002.optimustechproject.optimus_tech_project_2017002.util.DbHandler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by satyam on 28/6/17.
 */

public class VideoFragment extends Fragment {

    View parentView;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    private ColoredSnackbar coloredSnackbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_video, container, false);

        progressDialog=new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading");
        progressDialog.show();

        VideoRequest videoRequest= ServiceGenerator.createService(VideoRequest.class, DbHandler.getString(getContext(),"bearer",""));
        Call<VideoDataPOJO> call=videoRequest.requestResponse();
        call.enqueue(new Callback<VideoDataPOJO>() {
            @Override
            public void onResponse(Call<VideoDataPOJO> call, Response<VideoDataPOJO> response) {
                progressDialog.dismiss();
                Log .e("videror",String.valueOf(response.body().getData()));
                if(response.code()==200){

                    if(!response.body().getError()) {
                        recyclerView = (RecyclerView) parentView.findViewById(R.id.recycler);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        adapter_videos adapter_videos = new adapter_videos(getContext(), response.body().getData().getName(), response.body().getData().getVideoBy(), response.body().getData().getBackground(), response.body().getData().getFetchUrl());
                        recyclerView.setAdapter(adapter_videos);
                    }
                    else{
                        DbHandler.unsetSession(getContext(),"isForcedLoggedOut");
                    }

                }
                else{
                    Snackbar snackbar=Snackbar.make(getActivity().findViewById(android.R.id.content),"Error connecting to server",Snackbar.LENGTH_LONG);
                    coloredSnackbar.warning(snackbar).show();
                }
            }

            @Override
            public void onFailure(Call<VideoDataPOJO> call, Throwable t) {
                    progressDialog.dismiss();
                    Log.e("videoErr",String.valueOf(t));
                    new AlertDialog.Builder(getContext()).setTitle("Error").setMessage("Connection Failed").setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getActivity().onBackPressed();
                        }
                    }).show();
            }
        });

        return parentView;
    }
}
