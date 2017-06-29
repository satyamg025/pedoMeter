package com.a2017002.optimustechproject.optimus_tech_project_2017002.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.a2017002.optimustechproject.optimus_tech_project_2017002.R;
import com.imanoweb.calendarview.CalendarListener;
import com.imanoweb.calendarview.CustomCalendarView;
import com.imanoweb.calendarview.DayDecorator;
import com.imanoweb.calendarview.DayView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by satyam on 29/6/17.
 */

public class CalendarFragment extends Fragment {

    View parentView;
    CustomCalendarView calendarView;
    private TextView selectedDateTv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_calendar, container, false);

        calendarView = (CustomCalendarView) parentView.findViewById(R.id.calendar_view);
        selectedDateTv=(TextView)parentView.findViewById(R.id.selected_date);

        Calendar currentCalendar = Calendar.getInstance(Locale.getDefault());

        calendarView.setFirstDayOfWeek(Calendar.MONDAY);

        calendarView.setShowOverflowDate(false);

        calendarView.refreshCalendar(currentCalendar);
        calendarView.setHorizontalScrollBarEnabled(true);
        calendarView.setCalendarListener(new CalendarListener() {
            @Override
            public void onDateSelected(Date date) {
              //  if (!CalendarUtils.isPastDay(date)) {
                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                    selectedDateTv.setText("Selected date is " + df.format(date));
//                } else {
//                    selectedDateTv.setText("Selected date is disabled!");
//                }
            }

            @Override
            public void onMonthChanged(Date date) {
                //SimpleDateFormat df = new SimpleDateFormat("MM-yyyy");
                //Toast.makeText(getActivity(), df.format(date), Toast.LENGTH_SHORT).show();
            }
        });

        List<DayDecorator> decorators = new ArrayList<>();
        decorators.add(new DisabledColorDecorator());
        calendarView.setDecorators(decorators);
        calendarView.refreshCalendar(currentCalendar);

        return parentView;

    }


    private class DisabledColorDecorator implements DayDecorator {
        @Override
        public void decorate(DayView dayView) {
           // if (CalendarUtils.isPastDay(dayView.getDate())) {
//                int color = Color.parseColor("#a9afb9");
//                dayView.setBackgroundColor(color);
            //}
        }
    }



 }
