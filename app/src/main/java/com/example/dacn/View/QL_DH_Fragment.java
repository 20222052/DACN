package com.example.dacn.View;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.dacn.R;

import java.util.Calendar;

public class QL_DH_Fragment extends Fragment {
    private EditText editTextNS;
    public QL_DH_Fragment() {
    }
    public static QL_DH_Fragment newInstance(String param1, String param2) {
        QL_DH_Fragment fragment = new QL_DH_Fragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_q_l__d_h_, container, false);

        // Find the EditText in the inflated view
//        editTextNS = view.findViewById(R.id.);
//
//        setupDateInputWatcher();
        return view;
    }

    private void setupDateInputWatcher() {
        editTextNS.addTextChangedListener(new TextWatcher() {
            private String current = "";
            private final Calendar cal = Calendar.getInstance();

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed here
            }

            @SuppressLint("DefaultLocale")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d]", "");
                    String cleanC = current.replaceAll("[^\\d]", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8) {
                        String ddmmyyyy = "DDMMYYYY";
                        clean = clean + ddmmyyyy.substring(clean.length());
                    } else {
                        int day = Integer.parseInt(clean.substring(0, 2));
                        int month = Integer.parseInt(clean.substring(2, 4));
                        int year = Integer.parseInt(clean.substring(4, 8));

                        // Validate day and month
                        day = Math.min(day, cal.getActualMaximum(Calendar.DATE));
                        month = Math.min(month, 12);
                        clean = String.format("%02d%02d%02d", day, month, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = Math.max(sel, 0);
                    current = clean;
                    editTextNS.setText(current);
                    editTextNS.setSelection(Math.min(sel, current.length()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                if (input.length() == 10) { // Validate the format DD/MM/YYYY
                    String[] parts = input.split("/");
                    try {
                        int day = Integer.parseInt(parts[0]);
                        int month = Integer.parseInt(parts[1]);
                        int year = Integer.parseInt(parts[2]);

                        // Set the calendar
                        cal.set(Calendar.YEAR, year);
                        cal.set(Calendar.MONTH, month - 1);

                        int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
                        int maxYear = Calendar.getInstance().get(Calendar.YEAR);

                        // Validate day, month, and year
                        if (day < 1 || day > maxDay || month < 1 || month > 12 || year > maxYear || year < maxYear) {
                            editTextNS.setError("Ngày sinh không hợp lệ");
                        } else {
                            editTextNS.setError(null);
                        }
                    } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                        editTextNS.setError("Định dạng không hợp lệ");
                    }
                }
            }
        });
    }
}