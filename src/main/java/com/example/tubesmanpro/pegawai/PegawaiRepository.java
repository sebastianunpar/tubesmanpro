package com.example.tubesmanpro.pegawai;

import java.util.List;

import com.example.tubesmanpro.kehadiran.Kehadiran;

public interface PegawaiRepository {
    List<Pegawai> findPegawai(String noHp);
    List<String> getJabatanFromId (int idJabatan);
    List<String> getAlamatFromId (int idAlamat);
    List<String> getKelurahanFromIdAlamat (int idKelurahan);
    List<String> getKecamatanFromIdAlamat (int idKecamatan);
    List<Kehadiran> getKehadiran (String noHp);
}
