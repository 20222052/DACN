package com.example.dacn.View;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.dacn.Controller.QL_BCTKController;
import com.example.dacn.R;
import com.github.mikephil.charting.charts.LineChart;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class QL_BCTK_Fragment extends Fragment {
    private LineChart lineChart;
    private Spinner yearsSpinner;
    private Spinner quarterSpinner;
    private Spinner monthSpinner;
    private Spinner daySpinner;
    private TextView sdhTextView;
    private TextView ttdTextView;
    private QL_BCTKController controller;

    public QL_BCTK_Fragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        controller = new QL_BCTKController();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_q_l__b_c_t_k_, container, false);
        yearsSpinner = view.findViewById(R.id.Years);
        quarterSpinner = view.findViewById(R.id.Quarter);
        monthSpinner = view.findViewById(R.id.Month);
        daySpinner = view.findViewById(R.id.Day);
        sdhTextView = view.findViewById(R.id.SDH);
        ttdTextView = view.findViewById(R.id.TTD);
        lineChart = view.findViewById(R.id.lineChart);
        fetchEarliestYear();
        setupSpinners();
        return view;
    }

    private void fetchEarliestYear() {
        controller.fetchEarliestYearFromFirebase(new QL_BCTKController.EarliestYearCallback() {
            @Override
            public void onCallback(int earliestYear) {
                populateYearsSpinner(earliestYear);
            }
        });
    }

    private void populateYearsSpinner(int earliestYear) {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        List<String> years = new ArrayList<>();
        years.add("Năm"); // Add default value
        for (int year = earliestYear; year <= currentYear; year++) {
            years.add(String.valueOf(year));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, years);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearsSpinner.setAdapter(adapter);

        yearsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedYearStr = years.get(position);
                if (!selectedYearStr.equals("Năm")) {
                    quarterSpinner.setSelection(0);
                    monthSpinner.setSelection(0);
                    daySpinner.setSelection(0);
                    int selectedYear = Integer.parseInt(selectedYearStr);
                    fetchYearData(selectedYear);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void setupSpinners() {
        quarterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) { // If not the default value
                    monthSpinner.setSelection(0); // Reset month spinner to default
                    daySpinner.setSelection(0); // Reset day spinner to default
                    String selectedQuarter = parent.getItemAtPosition(position).toString();
                    fetchQuarterData(selectedQuarter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) { // If not the default value
                    quarterSpinner.setSelection(0); // Reset quarter spinner to default
                    daySpinner.setSelection(0); // Reset day spinner to default
                    String selectedMonth = parent.getItemAtPosition(position).toString();
                    fetchMonthData(selectedMonth);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        daySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) { // If not the default value
                    quarterSpinner.setSelection(0); // Reset quarter spinner to default
                    monthSpinner.setSelection(0); // Reset month spinner to default
                    yearsSpinner.setSelection(0);
                    String selectedDay = parent.getItemAtPosition(position).toString();
                    fetchDayData(selectedDay);
                }
                if (position == 0){
                    sdhTextView.setText("Số đơn hàng: " + 0);
                    ttdTextView.setText("Tổng tiền thu được: " + 0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void fetchYearData(int year) {
        controller.fetchYearDataFromFirebase(year, new QL_BCTKController.YearDataCallback() {
            @Override
            public void onCallback(int orderCount, int totalRevenue) {
                sdhTextView.setText("Số đơn hàng: " + orderCount);
                ttdTextView.setText("Tổng tiền thu được: " + totalRevenue);
            }
        }, lineChart);
    }

    private void fetchQuarterData(String quarter) {
        String selectedYearStr = yearsSpinner.getSelectedItem().toString();
        if (!selectedYearStr.equals("Năm")) {
            int selectedYear = Integer.parseInt(selectedYearStr);
            controller.fetchQuarterDataFromFirebase(selectedYear, quarter, new QL_BCTKController.QuarterDataCallback() {
                @Override
                public void onCallback(int orderCount, int totalRevenue) {
                    sdhTextView.setText("Số đơn hàng: " + orderCount);
                    ttdTextView.setText("Tổng tiền thu được: " + totalRevenue);
                }
            },lineChart);
        }
        else {
            Toast.makeText(this.getContext(), "Vui lòng chọn năm!", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchMonthData(String month) {
        String selectedYearStr = yearsSpinner.getSelectedItem().toString();
        if (!selectedYearStr.equals("Năm")) {
            int selectedYear = Integer.parseInt(selectedYearStr);
            controller.fetchMonthDataFromFirebase(selectedYear, month, new QL_BCTKController.MonthDataCallback() {
                @Override
                public void onCallback(int orderCount, int totalRevenue) {
                    sdhTextView.setText("Số đơn hàng: " + orderCount);
                    ttdTextView.setText("Tổng tiền thu được: " + totalRevenue);
                }
            }, lineChart);
        }
        else {
            Toast.makeText(this.getContext(), "Vui lòng chọn năm!", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchDayData(String Day) {
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String formattedDate = sdf.format(calendar.getTime());

        // Log the current date
        Log.e("Current Date", Day);

        // Fetch data for the current date
        if(Day.equals("Ngày")){
            controller.fetchDayDataFromFirebase(formattedDate, new QL_BCTKController.DayDataCallback() {
                @Override
                public void onCallback(int orderCount, int totalRevenue) {
                    sdhTextView.setText("Số đơn hàng: " + orderCount);
                    ttdTextView.setText("Tổng tiền thu được: " + totalRevenue);
                }
            },lineChart);
        }
        if (Day.equals("Tuần")) {
            controller.fetchWeekDataFromFirebase(formattedDate, new QL_BCTKController.WeekDataCallback() {
                @Override
                public void onCallback(int orderCount, int totalRevenue) {
                    sdhTextView.setText("Số đơn hàng: " + orderCount);
                    ttdTextView.setText("Tổng tiền thu được: " + totalRevenue);
                }
            }, lineChart);
        }
        if (Day.equals("Tháng")){
            controller.fetchMonthDataFromFirebase(formattedDate, new QL_BCTKController.MonthDataCallback() {
                @Override
                public void onCallback(int orderCount, int totalRevenue) {
                    sdhTextView.setText("Số đơn hàng: " + orderCount);
                    ttdTextView.setText("Tổng tiền thu được: " + totalRevenue);
                }
            }, lineChart);
        }
    }
}