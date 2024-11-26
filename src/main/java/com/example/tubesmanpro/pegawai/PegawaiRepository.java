package com.example.tubesmanpro.pegawai;

import java.util.List;

public interface PegawaiRepository {
    List<Pegawai> findPegawai(String noHp);
    
}
