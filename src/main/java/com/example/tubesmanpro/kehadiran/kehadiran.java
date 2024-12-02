package com.example.tubesmanpro.kehadiran;
import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class Kehadiran {
    private String namapegawai;
    private String tanggal;
    private String jammasuk;
    private String jamkeluar;
    private String durasikerja;
    private double gaji;
}
