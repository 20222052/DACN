package com.example.dacn.Controller;

import android.graphics.Color;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class QL_BCTKController {

    public interface EarliestYearCallback {
        void onCallback(int earliestYear);
    }

    public interface YearDataCallback {
        void onCallback(int orderCount, int totalRevenue);
    }

    public interface QuarterDataCallback {
        void onCallback(int orderCount, int totalRevenue);
    }

    public interface MonthDataCallback {
        void onCallback(int orderCount, int totalRevenue);
    }

    public interface DayDataCallback {
        void onCallback(int orderCount, int totalRevenue);
    }
    public interface WeekDataCallback {
        void onCallback(int orderCount, int totalRevenue);
    }
    public void fetchEarliestYearFromFirebase(final EarliestYearCallback callback) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Don_Hang");
        databaseReference.orderByChild("ngayDatHang").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int earliestYear = Integer.MAX_VALUE;
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String date = snapshot.child("ngayDatHang").getValue(String.class);
                        int year = Integer.parseInt(date.split("-")[2]);
                        if (year < earliestYear) {
                            earliestYear = year;
                        }
                    }
                    callback.onCallback(earliestYear);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }

    public void fetchYearDataFromFirebase(int year, final YearDataCallback callback, final LineChart lineChart) {
        DatabaseReference donHangRef = FirebaseDatabase.getInstance().getReference("Don_Hang");
        donHangRef.orderByChild("ngayDatHang").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot donHangSnapshot) {
                final Wrapper<Integer> orderCount = new Wrapper<>(0);
                final Wrapper<Integer> totalRevenue = new Wrapper<>(0);

                // Initialize arrays to store monthly data
                int[] monthlyOrderCount = new int[12];
                int[] monthlyRevenue = new int[12];

                if (donHangSnapshot.exists()) {
                    for (DataSnapshot snapshot : donHangSnapshot.getChildren()) {
                        String date = snapshot.child("ngayDatHang").getValue(String.class);
                        if (date != null) {
                            String[] dateParts = date.split("-");
                            int orderYear = Integer.parseInt(dateParts[2]); // Year is the third part
                            int month = Integer.parseInt(dateParts[1]) - 1; // Month is 0-based for array indexing

                            if (orderYear == year) {
                                orderCount.value++;  // Increment order count
                                int revenue = snapshot.child("tongTien").getValue(Integer.class);
                                totalRevenue.value += revenue;  // Add to total revenue

                                // Increment monthly data
                                monthlyOrderCount[month]++;
                                monthlyRevenue[month] += revenue;

                                String maDonHang = snapshot.getKey();  // Get order ID
                                Log.d("QL_BCTKController", "maDonHang: " + maDonHang);
                            }
                        }
                    }

                    // Prepare entries for the chart
                    List<Entry> orderEntries = new ArrayList<>();
                    List<Entry> revenueEntries = new ArrayList<>();
                    for (int i = 0; i < 12; i++) {
                        orderEntries.add(new Entry(i + 1, monthlyOrderCount[i]));
                        revenueEntries.add(new Entry(i + 1, monthlyRevenue[i]));
                    }

                    // Send the result to the callback
                    callback.onCallback(orderCount.value, totalRevenue.value);
                    updateChart(orderEntries, revenueEntries, lineChart);
                } else {
                    Log.e("QL_BCTKController", "No orders found for the year: " + year);
                    callback.onCallback(0, 0);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Firebase", "Failed to fetch orders: " + databaseError.getMessage());
            }
        });
    }
    public void fetchQuarterDataFromFirebase(int year, String quarter, final QuarterDataCallback callback, final LineChart lineChart) {
        DatabaseReference donHangRef = FirebaseDatabase.getInstance().getReference("Don_Hang");
        donHangRef.orderByChild("ngayDatHang").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot donHangSnapshot) {
                final Wrapper<Integer> orderCount = new Wrapper<>(0);
                final Wrapper<Integer> totalRevenue = new Wrapper<>(0);

                // Initialize arrays to store monthly data
                int[] monthlyOrderCount = new int[3];
                int[] monthlyRevenue = new int[3];

                if (donHangSnapshot.exists()) {
                    for (DataSnapshot snapshot : donHangSnapshot.getChildren()) {
                        String date = snapshot.child("ngayDatHang").getValue(String.class);
                        if (date != null) {
                            String[] dateParts = date.split("-");
                            int orderYear = Integer.parseInt(dateParts[2]);
                            int month = Integer.parseInt(dateParts[1]);

                            if (orderYear == year && isMonthInQuarter(month, quarter)) {
                                orderCount.value++;  // Increment order count
                                int revenue = snapshot.child("tongTien").getValue(Integer.class);
                                totalRevenue.value += revenue;  // Add to total revenue

                                // Increment monthly data
                                int monthIndex = getMonthIndexInQuarter(month, quarter);
                                monthlyOrderCount[monthIndex]++;
                                monthlyRevenue[monthIndex] += revenue;

                                String maDonHang = snapshot.getKey();  // Get order ID
                                Log.d("QL_BCTKController", "maDonHang: " + maDonHang);
                            }
                        }
                    }

                    // Prepare entries for the chart
                    List<Entry> orderEntries = new ArrayList<>();
                    List<Entry> revenueEntries = new ArrayList<>();
                    for (int i = 0; i < 3; i++) {
                        orderEntries.add(new Entry(i + 1, monthlyOrderCount[i]));
                        revenueEntries.add(new Entry(i + 1, monthlyRevenue[i]));
                    }

                    // Send the result to the callback
                    callback.onCallback(orderCount.value, totalRevenue.value);
                    updateChart(orderEntries, revenueEntries, lineChart);
                } else {
                    Log.e("QL_BCTKController", "No orders found for the year: " + year);
                    callback.onCallback(0, 0);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Firebase", "Failed to fetch orders: " + databaseError.getMessage());
            }
        });
    }

    public void fetchMonthDataFromFirebase(int year, String month, final MonthDataCallback callback, final LineChart lineChart) {
        DatabaseReference donHangRef = FirebaseDatabase.getInstance().getReference("Don_Hang");
        donHangRef.orderByChild("ngayDatHang").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot donHangSnapshot) {
                final Wrapper<Integer> orderCount = new Wrapper<>(0);
                final Wrapper<Integer> totalRevenue = new Wrapper<>(0);
                int monthInt = Integer.parseInt(month.replaceAll("[^0-9]", ""));
                // Determine the number of days in the selected month
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthInt - 1, 1);
                int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

                // Initialize arrays to store daily data
                int[] dailyOrderCount = new int[daysInMonth];
                int[] dailyRevenue = new int[daysInMonth];

                if (donHangSnapshot.exists()) {
                    for (DataSnapshot snapshot : donHangSnapshot.getChildren()) {
                        String date = snapshot.child("ngayDatHang").getValue(String.class);
                        if (date != null) {
                            String[] dateParts = date.split("-");
                            int orderYear = Integer.parseInt(dateParts[2]);
                            int orderMonth = Integer.parseInt(dateParts[1]);
                            int day = Integer.parseInt(dateParts[0]);

                            if (orderYear == year && orderMonth == monthInt) {
                                orderCount.value++;  // Increment order count
                                int revenue = snapshot.child("tongTien").getValue(Integer.class);
                                totalRevenue.value += revenue;  // Add to total revenue

                                // Increment daily data
                                dailyOrderCount[day - 1]++;
                                dailyRevenue[day - 1] += revenue;

                                String maDonHang = snapshot.getKey();  // Get order ID
                                Log.d("QL_BCTKController", "maDonHang: " + maDonHang);
                            }
                        }
                    }

                    // Prepare entries for the chart
                    List<Entry> orderEntries = new ArrayList<>();
                    List<Entry> revenueEntries = new ArrayList<>();
                    for (int i = 0; i < daysInMonth; i++) {
                        orderEntries.add(new Entry(i + 1, dailyOrderCount[i]));
                        revenueEntries.add(new Entry(i + 1, dailyRevenue[i]));
                    }

                    // Send the result to the callback
                    callback.onCallback(orderCount.value, totalRevenue.value);
                    updateChart(orderEntries, revenueEntries, lineChart);
                } else {
                    Log.e("QL_BCTKController", "No orders found for the year: " + year);
                    callback.onCallback(0, 0);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Firebase", "Failed to fetch orders: " + databaseError.getMessage());
            }
        });
    }

    public void fetchDayDataFromFirebase(String formattedDate, final DayDataCallback callback, final LineChart lineChart) {
        DatabaseReference donHangRef = FirebaseDatabase.getInstance().getReference("Don_Hang");
        donHangRef.orderByChild("ngayDatHang").equalTo(formattedDate).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot donHangSnapshot) {
                final Wrapper<Integer> orderCount = new Wrapper<>(0);
                final Wrapper<Integer> totalRevenue = new Wrapper<>(0);

                if (donHangSnapshot.exists()) {
                    for (DataSnapshot snapshot : donHangSnapshot.getChildren()) {
                        orderCount.value++;  // Increment order count
                        int revenue = snapshot.child("tongTien").getValue(Integer.class);
                        totalRevenue.value += revenue;  // Add to total revenue

                        String maDonHang = snapshot.getKey();  // Get order ID
                        Log.d("QL_BCTKController", "maDonHang: " + maDonHang);
                    }

                    // Prepare entries for the chart
                    List<Entry> orderEntries = new ArrayList<>();
                    List<Entry> revenueEntries = new ArrayList<>();
                    orderEntries.add(new Entry(1, orderCount.value));
                    revenueEntries.add(new Entry(1, totalRevenue.value));

                    // Send the result to the callback
                    callback.onCallback(orderCount.value, totalRevenue.value);
                    updateChart(orderEntries, revenueEntries, lineChart);
                } else {
                    Log.e("QL_BCTKController", "No orders found for the date: " + formattedDate);
                    callback.onCallback(0, 0);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Firebase", "Failed to fetch orders: " + databaseError.getMessage());
            }
        });
    }

    public void fetchWeekDataFromFirebase(String startDate, final WeekDataCallback callback, final LineChart lineChart) {
        DatabaseReference donHangRef = FirebaseDatabase.getInstance().getReference("Don_Hang");

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        try {
            Date date = sdf.parse(startDate);
            calendar.setTime(date);
        } catch (Exception e) {
            Log.e("QL_BCTKController", "Invalid date format: " + startDate, e);
            callback.onCallback(0, 0);
            return;
        }

        final Wrapper<Integer> orderCount = new Wrapper<>(0);
        final Wrapper<Integer> totalRevenue = new Wrapper<>(0);
        final Wrapper<Integer> daysProcessed = new Wrapper<>(0);

        List<Entry> orderEntries = new ArrayList<>();
        List<Entry> revenueEntries = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            String currentDate = sdf.format(calendar.getTime());
            Log.d("QL_BCTKController", "currentDate: " + currentDate);
            final int dayIndex = i;
            donHangRef.orderByChild("ngayDatHang").equalTo(currentDate).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot donHangSnapshot) {
                    int dailyOrderCount = 0;
                    int dailyRevenue = 0;

                    if (donHangSnapshot.exists()) {
                        for (DataSnapshot snapshot : donHangSnapshot.getChildren()) {
                            dailyOrderCount++;
                            int revenue = snapshot.child("tongTien").getValue(Integer.class);
                            dailyRevenue += revenue;
                        }
                    }

                    orderEntries.add(new Entry(dayIndex, dailyOrderCount));
                    revenueEntries.add(new Entry(dayIndex, dailyRevenue));

                    orderCount.value += dailyOrderCount;
                    totalRevenue.value += dailyRevenue;
                    daysProcessed.value++;

                    if (daysProcessed.value == 7) {
                        callback.onCallback(orderCount.value, totalRevenue.value);
                        updateChart(orderEntries, revenueEntries, lineChart);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("Firebase", "Failed to fetch orders: " + databaseError.getMessage());
                    daysProcessed.value++;
                    if (daysProcessed.value == 7) {
                        callback.onCallback(orderCount.value, totalRevenue.value);
                        updateChart(orderEntries, revenueEntries, lineChart);
                    }
                }
            });
            calendar.add(Calendar.DAY_OF_MONTH, -1); // Move to the next day
        }
    }

    public void fetchMonthDataFromFirebase(String startDate, final MonthDataCallback callback, final LineChart lineChart) {
        DatabaseReference donHangRef = FirebaseDatabase.getInstance().getReference("Don_Hang");

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        try {
            Date date = sdf.parse(startDate);
            calendar.setTime(date);
        } catch (Exception e) {
            Log.e("QL_BCTKController", "Invalid date format: " + startDate, e);
            callback.onCallback(0, 0);
            return;
        }

        final Wrapper<Integer> orderCount = new Wrapper<>(0);
        final Wrapper<Integer> totalRevenue = new Wrapper<>(0);
        final Wrapper<Integer> daysProcessed = new Wrapper<>(0);

        List<Entry> orderEntries = new ArrayList<>();
        List<Entry> revenueEntries = new ArrayList<>();

        for (int i = 0; i < 30; i++) {
            String currentDate = sdf.format(calendar.getTime());
            Log.d("QL_BCTKController", "currentDate: " + currentDate);
            final int dayIndex = i;
            donHangRef.orderByChild("ngayDatHang").equalTo(currentDate).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot donHangSnapshot) {
                    int dailyOrderCount = 0;
                    int dailyRevenue = 0;

                    if (donHangSnapshot.exists()) {
                        for (DataSnapshot snapshot : donHangSnapshot.getChildren()) {
                            dailyOrderCount++;
                            int revenue = snapshot.child("tongTien").getValue(Integer.class);
                            dailyRevenue += revenue;
                        }
                    }

                    orderEntries.add(new Entry(dayIndex + 1, dailyOrderCount));
                    revenueEntries.add(new Entry(dayIndex + 1, dailyRevenue));

                    orderCount.value += dailyOrderCount;
                    totalRevenue.value += dailyRevenue;
                    daysProcessed.value++;

                    if (daysProcessed.value == 30) {
                        callback.onCallback(orderCount.value, totalRevenue.value);
                        updateChart(orderEntries, revenueEntries, lineChart);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("Firebase", "Failed to fetch orders: " + databaseError.getMessage());
                    daysProcessed.value++;
                    if (daysProcessed.value == 30) {
                        callback.onCallback(orderCount.value, totalRevenue.value);
                        updateChart(orderEntries, revenueEntries, lineChart);
                    }
                }
            });
            calendar.add(Calendar.DAY_OF_MONTH, -1); // Move to the next day
        }
    }
private void updateChart(List<Entry> orderEntries, List<Entry> revenueEntries, LineChart lineChart) {
    LineDataSet orderDataSet = new LineDataSet(orderEntries, "Số đơn hàng");
    orderDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);

    orderDataSet.setValueTextSize(15);
    orderDataSet.setColor(Color.BLUE); // Set color for order data set
    orderDataSet.setValueTextColor(Color.BLUE); // Set text color for order data set
    orderDataSet.setValueFormatter(new ValueFormatter() {
        @Override
        public String getPointLabel(Entry entry) {
            return String.valueOf((int) entry.getY());
        }
    });

    LineDataSet revenueDataSet = new LineDataSet(revenueEntries, "Số tiền");
    revenueDataSet.setAxisDependency(YAxis.AxisDependency.RIGHT);
    revenueDataSet.setValueTextSize(15);
    revenueDataSet.setColor(Color.BLACK); // Set color for revenue data set
    revenueDataSet.setValueTextColor(Color.BLACK); // Set text color for revenue data set
    revenueDataSet.setValueFormatter(new ValueFormatter() {
        @Override
        public String getPointLabel(Entry entry) {
            return String.valueOf((int) entry.getY());
        }
    });

    LineData lineData = new LineData(orderDataSet, revenueDataSet);
    lineData.setValueFormatter(new ValueFormatter() {
        @Override
        public String getPointLabel(Entry entry) {
            int index = (int) entry.getX();
            if (index >= 0 && index < orderEntries.size() && index < revenueEntries.size()) {
                Entry orderEntry = orderEntries.get(index);
                Entry revenueEntry = revenueEntries.get(index);
                if (orderEntry.getX() != revenueEntry.getX() || orderEntry.getY() != revenueEntry.getY()) {
                    int orderValue = (int) orderEntry.getY();
                    int revenueValue = (int) revenueEntry.getY();
                    return orderValue + "/" + revenueValue;
                } else {
                    return String.valueOf((int) entry.getY());
                }
            } else {
                return String.valueOf((int) entry.getY());
            }
        }
    });
    lineChart.getDescription().setText("Biểu đồ thống kê");
    lineChart.setExtraLeftOffset(10);
    lineChart.setExtraRightOffset(10);
    lineChart.setData(lineData);
    lineChart.invalidate(); // Refresh the chart
}
    private boolean isMonthInQuarter(int month, String quarter) {
        switch (quarter) {
            case "Quý 1":
                return month >= 1 && month <= 3;
            case "Quý 2":
                return month >= 4 && month <= 6;
            case "Quý 3":
                return month >= 7 && month <= 9;
            case "Quý 4":
                return month >= 10 && month <= 12;
            default:
                return false;
        }
    }
    private int getMonthIndexInQuarter(int month, String quarter) {
        switch (quarter) {
            case "Quý 1":
                return month - 1;
            case "Quý 2":
                return month - 4;
            case "Quý 3":
                return month - 7;
            case "Quý 4":
                return month - 10;
            default:
                return -1;
        }
    }
    private static class Wrapper<T> {
        T value;

        Wrapper(T value) {
            this.value = value;
        }
    }
}