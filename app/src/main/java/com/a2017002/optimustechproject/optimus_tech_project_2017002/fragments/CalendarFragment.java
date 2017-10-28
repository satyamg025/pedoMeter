package com.a2017002.optimustechproject.optimus_tech_project_2017002.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.a2017002.optimustechproject.optimus_tech_project_2017002.models.ActivityDataumPOJO;
import com.a2017002.optimustechproject.optimus_tech_project_2017002.util.ColoredSnackbar;
import com.a2017002.optimustechproject.optimus_tech_project_2017002.util.DbHandler;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.a2017002.optimustechproject.optimus_tech_project_2017002.R;

public class CalendarFragment extends android.support.v4.app.Fragment {
    final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");

    ActivityDataumPOJO data;
    TextView cal_event;
    SimpleDateFormat sdf;
    Gson gson = new Gson();
    Date last;
    private View parentView;
    private ColoredSnackbar coloredSnackBar;
    public CalendarFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            parentView = inflater.inflate(R.layout.fragment_calendar, container, false);
            sdf = new SimpleDateFormat("yyyy-MM-dd");
            CaldroidFragment caldroidFragment = new CaldroidFragment();
            Date min = null, max = null;

            data= gson.fromJson(DbHandler.getString(getActivity(),"activity_detail","{}"),ActivityDataumPOJO.class);

            Bundle args = new Bundle();
            cal_event = (TextView) parentView.findViewById(R.id.cal_event);
            Calendar cal = Calendar.getInstance();
            args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
            args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
            caldroidFragment.setArguments(args);
            //caldroidFragment.setCaldroidListener(listener(caldroidFragment));
            FragmentTransaction t = getChildFragmentManager().beginTransaction();
            t.replace(R.id.flContent, caldroidFragment);
            t.commit();
        try {
            setCalendar(caldroidFragment);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return parentView;
    }

//    private CaldroidListener listener(final CaldroidFragment caldroidFragment) {
//        final CaldroidListener listener = new CaldroidListener() {
//
//            @Override
//            public void onSelectDate(Date date, View view) {
//                if (last != null)
//                    caldroidFragment.clearSelectedDate(last);
//                caldroidFragment.setSelectedDate(date);
//                last = date;
//                cal_event.setText(formatter.format(date));
//                caldroidFragment.refreshView();
//                if (dateList != null)
//                    for (ACPOJO res : dateList) {
//                        try {
//                            if ((date.after(sdf.parse(res.start)) && date.before(sdf.parse(res.end))) || date.compareTo(sdf.parse(res.start)) == 0 || date.compareTo(sdf.parse(res.end)) == 0)
//                                cal_event.setText(cal_event.getText().toString() + "," + res.getTitle());
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//            }
//        };
//        // Setup Caldroid
//        caldroidFragment.setCaldroidListener(listener);
//        return listener;
//    }

//    void SetDates(final CaldroidFragment caldroidFragment) {
//        String data = DbHandler.getString(getActivity(), "cal", "{}");
//        if (!data.equals("{}")) {
//            dateList = gson.fromJson(data, new TypeToken<List<ACPOJO>>() {
//            }.getType());
//            setCalendar(caldroidFragment);
//        }
//        ACRequest ACRequest = ServiceGenerator.createService(ACRequest.class);
//        Call<List<ACPOJO>> call = ACRequest.requestResponse();
//
//        call.enqueue(new Callback<List<ACPOJO>>() {
//            @Override
//            public void onResponse(Call<List<ACPOJO>> call, Response<List<ACPOJO>> response) {
//                dateList = response.body();
//                if (response.code() == 200) {
//                    DbHandler.putString(getActivity(), "cal", gson.toJson(dateList));
//                    setCalendar(caldroidFragment);
//
//                }
//                else if (response.code() == 502)
//                {
//                    String tmp = ServiceGenerator.API_BASE_URL_ALTERNATE;
//                    ServiceGenerator.API_BASE_URL_ALTERNATE=ServiceGenerator.API_BASE_URL;
//                    ServiceGenerator.API_BASE_URL=tmp;
//                }
//                else
//                {
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<ACPOJO>> call, Throwable t) {
//            }
//
//        });
//
//    }

    void setCalendar(CaldroidFragment caldroidFragment) throws ParseException {

        for(int i=0;i<data.getDate().size();i++){
            String dateString =data.getDate().get(i);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy");
            Date convertedDate = new Date();
            convertedDate=dateFormat.parse(dateString);
            caldroidFragment.setBackgroundDrawableForDate(new ColorDrawable(getResources().getColor(R.color.colorAccent)),convertedDate);

        }
    }

}
