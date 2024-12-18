package com.example.dacn.View;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.dacn.Controller.QL_BCTKController;
import com.example.dacn.R;
import com.github.mikephil.charting.charts.LineChart;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class StatisticsFragment extends Fragment {

    private LineChart lineChart;
    private QL_BCTKController controller;

    public StatisticsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);
        lineChart = view.findViewById(R.id.lineChart);
        controller = new QL_BCTKController();

        // Fetch data for the current week
        fetchWeekData();

        return view;
    }

    private void fetchWeekData() {
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String formattedDate = sdf.format(calendar.getTime());

        // Log the current date
        Log.e("Current Date", formattedDate);

        // Fetch data for the current week
        controller.fetchWeekDataFromFirebase(formattedDate, new QL_BCTKController.WeekDataCallback() {
            @Override
            public void onCallback(int orderCount, int totalRevenue) {
                // Update UI with order count and total revenue if needed

            }
        }, lineChart);
    }
}