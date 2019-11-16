package com.example.sewain.ui.cari_kendaraan;

public class CariKendaraanViewModel {

    private String nama_kendaraan, jenis_kendaraan, harga_sewa, latit, longit, deskripsi, lokasi, foto;

    public CariKendaraanViewModel() {
    }

    public CariKendaraanViewModel(String nama_kendaraan, String jenis_kendaraan, String harga_sewa, String latit, String longit, String deskripsi, String lokasi) {
        this.nama_kendaraan = nama_kendaraan;
        this.jenis_kendaraan = jenis_kendaraan;
        this.harga_sewa = harga_sewa;
        this.latit = latit;
        this.longit = longit;
        this.deskripsi = deskripsi;
        this.lokasi = lokasi;
    }

    public CariKendaraanViewModel(String nama_kendaraan, String harga_sewa, String lokasi, String foto) {
        this.nama_kendaraan = nama_kendaraan;
        this.harga_sewa = harga_sewa;
        this.lokasi = lokasi;
        this.foto = foto;
    }

    public String getNama_kendaraan() {
        return nama_kendaraan;
    }

    public void setNama_kendaraan(String nama_kendaraan) {
        this.nama_kendaraan = nama_kendaraan;
    }

    public String getJenis_kendaraan() {
        return jenis_kendaraan;
    }

    public void setJenis_kendaraan(String jenis_kendaraan) {
        this.jenis_kendaraan = jenis_kendaraan;
    }

    public String getHarga_sewa() {
        return harga_sewa;
    }

    public void setHarga_sewa(String harga_sewa) {
        this.harga_sewa = harga_sewa;
    }

    public String getLatit() {
        return latit;
    }

    public void setLatit(String latit) {
        this.latit = latit;
    }

    public String getLongit() {
        return longit;
    }

    public void setLongit(String longit) {
        this.longit = longit;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
