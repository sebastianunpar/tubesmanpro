package com.example.tubesmanpro.pegawai;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PegawaiProfileDTO {
    private String nama;
    private String noHp;
    private String jabatan;
    private String alamat;
    private String kelurahan;
    private String kecamatan;
}
