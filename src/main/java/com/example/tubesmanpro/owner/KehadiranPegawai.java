package com.example.tubesmanpro.owner;

import lombok.Data;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import lombok.AllArgsConstructor;
 
@Data
@AllArgsConstructor
public class KehadiranPegawai {
    private String nama;        // Nama pegawai
    private String tanggal;     // Tanggal kehadiran
    private String jamMasuk;    // Waktu masuk kerja
    private String jamKeluar;   // Waktu keluar kerja
    private Double gaji;

    public String getDurasiKerja() {
        try {
            // Format waktu
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

            // Parse waktu masuk dan keluar
            LocalTime masuk = LocalTime.parse(jamMasuk, timeFormatter);
            LocalTime keluar = LocalTime.parse(jamKeluar, timeFormatter);

            // Hitung durasi
            Duration durasi = Duration.between(masuk, keluar);

            // Konversi durasi ke jam dan menit
            long hours = durasi.toHours();
            long minutes = durasi.toMinutes() % 60;

            return hours + " jam " + minutes + " menit";
        } catch (Exception e) {
            e.printStackTrace();
            return "Durasi tidak valid";
        }
    }
}
