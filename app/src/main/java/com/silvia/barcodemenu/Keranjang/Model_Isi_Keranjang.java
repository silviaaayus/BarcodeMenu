package com.silvia.barcodemenu.Keranjang;

public class Model_Isi_Keranjang {
    int id_produk, id_kategori;
    String nama_produk, harga_produk, gambar_produk;

    public Model_Isi_Keranjang(int id_produk, int id_kategori, String nama_produk, String harga_produk, String gambar_produk) {
        this.id_produk = id_produk;
        this.id_kategori = id_kategori;
        this.nama_produk = nama_produk;
        this.harga_produk = harga_produk;
        this.gambar_produk = gambar_produk;
    }

    public int getId_produk() {
        return id_produk;
    }

    public void setId_produk(int id_produk) {
        this.id_produk = id_produk;
    }

    public int getId_kategori() {
        return id_kategori;
    }

    public void setId_kategori(int id_kategori) {
        this.id_kategori = id_kategori;
    }

    public String getNama_produk() {
        return nama_produk;
    }

    public void setNama_produk(String nama_produk) {
        this.nama_produk = nama_produk;
    }

    public String getHarga_produk() {
        return harga_produk;
    }

    public void setHarga_produk(String harga_produk) {
        this.harga_produk = harga_produk;
    }

    public String getGambar_produk() {
        return gambar_produk;
    }

    public void setGambar_produk(String gambar_produk) {
        this.gambar_produk = gambar_produk;
    }
}
