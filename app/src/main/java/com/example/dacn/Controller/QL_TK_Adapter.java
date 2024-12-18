package com.example.dacn.Controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.dacn.Model.TaiKhoan;
import com.example.dacn.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QL_TK_Adapter extends BaseAdapter {
    private Context context;
    private List<TaiKhoan> taiKhoanList;
    private Map<String, String> nhanVienIdToNameMap;

    public QL_TK_Adapter(@NonNull Context context, @NonNull List<TaiKhoan> taiKhoanList, @NonNull Map<String, String> nhanVienIdToNameMap) {
        this.context = context;
        this.taiKhoanList = taiKhoanList;
        this.nhanVienIdToNameMap = nhanVienIdToNameMap;
    }

    @Override
    public int getCount() {
        return taiKhoanList.size();
    }

    @Override
    public Object getItem(int position) {
        return taiKhoanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_tk, parent, false);
        TaiKhoan taiKhoan = taiKhoanList.get(position);
        TextView tvName = convertView.findViewById(R.id.name_nv);
        TextView tvUsname = convertView.findViewById(R.id.Usname);
        TextView tvPass = convertView.findViewById(R.id.Pass);
        TextView tvCV = convertView.findViewById(R.id.CV);

        String nhanVienName = nhanVienIdToNameMap.get(taiKhoan.getNhanVienId());

        tvName.setText(String.format("Tên nhân viên :%s", nhanVienName));
        tvUsname.setText(String.format("Tài khoản :%s", taiKhoan.getTenDangNhap()));
        tvPass.setText(String.format("Mật khẩu :%s", taiKhoan.getMatKhau()));
        tvCV.setText(String.format("Quyền :%s", taiKhoan.getVaiTro()));

        return convertView;
    }

    public void updateList(List<TaiKhoan> newList) {
        taiKhoanList = new ArrayList<>(newList);
        notifyDataSetChanged();
    }
}