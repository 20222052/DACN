package com.example.dacn.Model;

public class Table {
    private int idTable;
    private String nameTable;
    private boolean isStatus;

    public Table() {
    }

    public Table(int idTable, String nameTable, boolean isStatus) {
        this.idTable = idTable;
        this.nameTable = nameTable;
        this.isStatus = isStatus;
    }

    public int getIdTable() {
        return idTable;
    }

    public void setIdTable(int idTable) {
        this.idTable = idTable;
    }

    public String getNameTable() {
        return nameTable;
    }

    public void setNameTable(String nameTable) {
        this.nameTable = nameTable;
    }

    public boolean isStatus() {
        return isStatus;
    }

    public void setStatus(boolean status) {
        this.isStatus = status;
    }
}
