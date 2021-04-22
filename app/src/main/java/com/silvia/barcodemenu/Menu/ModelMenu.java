package com.silvia.barcodemenu.Menu;

public class ModelMenu {
    int id_kategori, jenis;
    String kategori, gambar_kategori;

    public ModelMenu(int id_kategori, int jenis, String kategori, String gambar_kategori) {
        this.id_kategori = id_kategori;
        this.jenis = jenis;
        this.kategori = kategori;
        this.gambar_kategori = gambar_kategori;
    }

    public int getId_kategori() {
        return id_kategori;
    }

    public void setId_kategori(int id_kategori) {
        this.id_kategori = id_kategori;
    }

    public int getJenis() {
        return jenis;
    }

    public void setJenis(int jenis) {
        this.jenis = jenis;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getGambar_kategori() {
        return gambar_kategori;
    }

    public void setGambar_kategori(String gambar_kategori) {
        this.gambar_kategori = gambar_kategori;
    }
}
