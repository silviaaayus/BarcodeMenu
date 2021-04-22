package com.silvia.barcodemenu;

public class API {

    private String HOST = "http://192.168.100.15/makanan/";

    public String URL_NO_MEJA = HOST + "nomeja.php";
    public String URL_Menu_Makanan = HOST + "select_kategori_makanan.php";
    public String URL_Menu_Minuman= HOST + "select_kategori_minuman.php";
    public String URL_Isi_Kategori = HOST + "select_isi_kategori.php?id_kategori=";
    public String URL_GAMBAR_PRODUK = HOST + "gambar_produk/";
    public String URL_Kerajang = HOST + "insert_keranjang.php";
    public String URL_Pesanan = HOST + "insert_pesanan.php";
    public String URL_HISTORY = HOST + "select_detail_pesanan.php?token=";



}
