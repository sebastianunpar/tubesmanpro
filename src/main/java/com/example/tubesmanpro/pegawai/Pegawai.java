package com.example.tubesmanpro.pegawai;

import lombok.Data;
import lombok.AllArgsConstructor;
 
@Data
@AllArgsConstructor
public class Pegawai {
    private String namapegawai;
    private String nomorhp;
    private String email;
    private int idjabatan;
    private int idalamat;
}
