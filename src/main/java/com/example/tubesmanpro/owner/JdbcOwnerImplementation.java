package com.example.tubesmanpro.owner;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.tubesmanpro.kehadiran.kehadiran;
import com.example.tubesmanpro.pegawai.Pegawai;

@Repository
public class JdbcOwnerImplementation implements OwnerRepository{
    
    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Override
    public List<Owner> showAllOwner(){
        String query = "SELECT * FROM pemilik";
        List<Owner> Owners = jdbcTemplate.query(query, this::mapRowToOwner);
        return Owners;
    }

    @Override
    public List<Owner> findOwner (String username, String password) {
        String sql = "SELECT * FROM pemilik WHERE usernamepemilik = ? AND passwordpemilik = ?";
        return jdbcTemplate.query(sql, this::mapRowToOwner, username, password);
    }

    public List<kehadiran> showKehadiran () {
        String sql = "select namapegawai, tanggal, jammasuk, jamkeluar from daftarkehadiran inner join pegawai on daftarkehadiran.nomorhp = pegawai.nomorhp";
        return jdbcTemplate.query(sql, this::mapRowToKehadiran);
    }

    public List<Pegawai> showUpdatePegawai(){
        String sql = "select namapegawai,nomorhp,email,jabatan.idjabatan,alamat.idalamat from pegawai join jabatan on pegawai.idjabatan = jabatan.idjabatan join alamat on alamat.idalamat = pegawai.idalamat";
        return jdbcTemplate.query(sql, this::mapRowToPegawai);
    }

    private Owner mapRowToOwner(ResultSet resultSet, int rowNum) throws SQLException {
        return new Owner(
            resultSet.getString("namapemilik"),
            resultSet.getString("usernamepemilik"),
            resultSet.getString("passwordpemilik")
        );
    }

    private kehadiran mapRowToKehadiran(ResultSet resultSet, int rowNum) throws SQLException {
        String namapegawai = resultSet.getString("namapegawai");
        String tanggal = resultSet.getString("tanggal");
        String jammasuk = resultSet.getString("jammasuk");
        String jamkeluar = resultSet.getString("jamkeluar");
        
        // Konversi jammasuk dan jamkeluar menjadi LocalTime
        LocalTime waktuMasuk = LocalTime.parse(jammasuk);
        LocalTime waktuKeluar = LocalTime.parse(jamkeluar);
        
        // Hitung selisih waktu dalam menit
        long durasiMenit = ChronoUnit.MINUTES.between(waktuMasuk, waktuKeluar);
        
        // Menghitung jam dan menit
        long jam = durasiMenit / 60;
        long menit = durasiMenit % 60;
        
        // Membuat durasi dalam format "X jam Y menit"
        String durasiKerjaFormat = jam + " jam " + menit + " menit";
        
        return new kehadiran(namapegawai, tanggal, jammasuk, jamkeluar, durasiKerjaFormat);
    }
    
    private Pegawai mapRowToPegawai(ResultSet resultSet, int rowNum) throws SQLException {
        return new Pegawai(
            resultSet.getString("namapegawai"),
            resultSet.getString("nomorhp"),
            resultSet.getString("email"),
            resultSet.getString("idJabatan"),
            resultSet.getString("idAlamat")
        );
    }

    public Pegawai getPegawaiByNomorHp(String nomorhp) {
        String sql = "SELECT * FROM pegawai WHERE nomorhp = ?";
        try {
            return jdbcTemplate.queryForObject(sql, this::mapRowToPegawai, nomorhp);
        } catch (Exception e) {
            return null;
        }
    }
    

    // Menyimpan data pegawai ke dalam database
    public boolean savePegawai(Pegawai pegawai) {
        String sql = "INSERT INTO Pegawai (nomorHP, namaPegawai, email, idJabatan, idAlamat, status) VALUES (?, ?, ?, ?, ?, ?)";
        
        // Mendapatkan idJabatan dan idAlamat berdasarkan jabatan dan alamat yang diberikan
        Integer idJabatan = getJabatanIdByName(pegawai.getJabatan());
        Integer idAlamat = getAlamatIdByName(pegawai.getAlamat());

        if (idJabatan != null && idAlamat != null) {
            // Simpan data pegawai
            int result = jdbcTemplate.update(sql, pegawai.getNomorhp(), pegawai.getNamapegawai(), pegawai.getEmail(), idJabatan, idAlamat, 1);
            return result > 0;
        }
        return false;
    }
    // Mengambil ID Jabatan berdasarkan nama jabatan
    private Integer getJabatanIdByName(String jabatanName) {
        String sql = "SELECT idJabatan FROM Jabatan WHERE namaJabatan = ?";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, jabatanName);
        } catch (Exception e) {
            return null;
        }
    }

    // Mengambil ID Alamat berdasarkan nama jalan
    private Integer getAlamatIdByName(String alamatName) {
        String sql = "SELECT idAlamat FROM Alamat WHERE namaJalan = ?";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, alamatName);
        } catch (Exception e) {
            return null;
        }
    }

    public List<Pegawai> showAllPegawai() {
        String sql = "SELECT * FROM pegawai";
        List<Pegawai> pegawai= jdbcTemplate.query(sql, this::mapRowToPegawai);
        return pegawai;
    }
}
