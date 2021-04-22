package com.silvia.barcodemenu.History;

public class Model_History {
    int id_detail, jumlah, harga;
    String nama_produk;

    public Model_History(int id_detail, int jumlah, int harga, String nama_produk) {
        this.id_detail = id_detail;
        this.jumlah = jumlah;
        this.harga = harga;
        this.nama_produk = nama_produk;
    }

    public int getId_detail() {
        return id_detail;
    }

    public void setId_detail(int id_detail) {
        this.id_detail = id_detail;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public String getNama_produk() {
        return nama_produk;
    }

    public void setNama_produk(String nama_produk) {
        this.nama_produk = nama_produk;
    }
}