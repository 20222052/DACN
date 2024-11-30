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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_q_l__d_h_, container, false);
    }
}