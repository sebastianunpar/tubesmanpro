package com.example.tubesmanpro.pegawai;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.tubesmanpro.kehadiran.Kehadiran;

@Repository
public class JdbcPegawaiImplementation implements PegawaiRepository{
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Pegawai> findPegawai (String noHp) {
        String sql = "SELECT * FROM pegawai WHERE nomorhp = ?";
        List<Pegawai> seluruhPegawai = jdbcTemplate.query(sql, this::mapRowToPegawai, noHp);
        return seluruhPegawai;
    }
    private Pegawai mapRowToPegawai(ResultSet resultSet, int rowNum) throws SQLException {
        return new Pegawai(
            resultSet.getString("nomorhp"),
            resultSet.getString("namapegawai"),
            resultSet.getString("email"),
            resultSet.getString("idjabatan"),
            resultSet.getString("idalamat")
        );
    }

    @Override
    public List<String> getJabatanFromId (int idJabatan) {
        String sql = "SELECT namajabatan FROM jabatan WHERE idjabatan = ?";
        return jdbcTemplate.query(sql, this::mapRowToJabatan, idJabatan);
    }
    private String mapRowToJabatan(ResultSet resultSet, int rowNum) throws SQLException {
        return resultSet.getString("namajabatan");
    }

    public List<Double> getSatuanGajiFromNoHp (String noHp) {
        String sql = "SELECT satuangaji FROM jabatan INNER JOIN pegawai on pegawai.idjabatan = jabatan.idjabatan WHERE pegawai.nomorhp = ?";
        return jdbcTemplate.query(sql, this::mapRowToSatuanGaji, noHp);
    }
    private Double mapRowToSatuanGaji(ResultSet resultSet, int rowNum) throws SQLException {
        return resultSet.getDouble("satuangaji");
    }

    @Override
    public List<String> getAlamatFromId (int idAlamat) {
        String sql = "SELECT namajalan FROM alamat WHERE idalamat = ?";
        return jdbcTemplate.query(sql, this::mapRowToAlamat, idAlamat);
    }
    private String mapRowToAlamat(ResultSet resultSet, int rowNum) throws SQLException {
        return resultSet.getString("namajalan");
    }

    @Override
    public List<String> getKelurahanFromIdAlamat (int idAlamat) {
        String sql = "SELECT kelurahan.namakelurahan FROM kelurahan inner join alamat on alamat.idkelurahan = kelurahan.idkelurahan WHERE alamat.idalamat = ?";
        return jdbcTemplate.query(sql, this::mapRowToKelurahan, idAlamat);
    }
    private String mapRowToKelurahan(ResultSet resultSet, int rowNum) throws SQLException {
        return resultSet.getString("namakelurahan");
    }

    @Override
    public List<String> getKecamatanFromIdAlamat (int idAlamat) {
        String sql = "SELECT kecamatan.namakecamatan FROM kecamatan inner join kelurahan on kelurahan.idkecamatan = kecamatan.idkecamatan inner join alamat on alamat.idkelurahan = kelurahan.idkelurahan where alamat.idalamat = ?";
        return jdbcTemplate.query(sql, this::mapRowToKecamatan, idAlamat);
    }
    private String mapRowToKecamatan(ResultSet resultSet, int rowNum) throws SQLException {
        return resultSet.getString("namakecamatan");
    }


    @Override
    public List<Kehadiran> getKehadiran (String noHp) {
        String sql = "SELECT namapegawai, tanggal, jammasuk, jamkeluar, gaji FROM daftarkehadiran left join pegawai on daftarkehadiran.nomorhp = pegawai.nomorhp WHERE daftarkehadiran.nomorhp = ?";
        return jdbcTemplate.query(sql, this::mapRowToKehadiran, noHp);
    }
    private Kehadiran mapRowToKehadiran (ResultSet resultSet, int rowNum) throws SQLException {
        String namapegawai = resultSet.getString("namapegawai");
        String tanggal = resultSet.getString("tanggal");
        String jammasuk = resultSet.getString("jammasuk");
        String jamkeluar = resultSet.getString("jamkeluar");
        double gaji = resultSet.getDouble("gaji");
        
        LocalTime waktuMasuk = LocalTime.parse(jammasuk);
        LocalTime waktuKeluar = LocalTime.parse(jamkeluar);
        
        long durasiMenit = ChronoUnit.MINUTES.between(waktuMasuk, waktuKeluar);
        
        long jam = durasiMenit / 60;
        long menit = durasiMenit % 60;
        
        String durasiKerjaFormat = jam + " jam " + menit + " menit";
        
        return new Kehadiran(namapegawai, tanggal, jammasuk, jamkeluar, durasiKerjaFormat, gaji);
    }

    public List<Kehadiran> getKehadiranPerWeek (String noHp, LocalDate monday, LocalDate sunday) {
        String sql = "SELECT namapegawai, tanggal, jammasuk, jamkeluar, gaji FROM daftarkehadiran left join pegawai on daftarkehadiran.nomorhp = pegawai.nomorhp WHERE daftarkehadiran.nomorhp = ? AND daftarkehadiran.tanggal BETWEEN ? AND ?";
        return jdbcTemplate.query(sql, this::mapRowToGajiPerWeek, noHp, monday, sunday);
    }
    private Kehadiran mapRowToGajiPerWeek (ResultSet resultSet, int rowNum) throws SQLException {
        String namapegawai = resultSet.getString("namapegawai");
        String tanggal = resultSet.getString("tanggal");
        String jammasuk = resultSet.getString("jammasuk");
        String jamkeluar = resultSet.getString("jamkeluar");
        double gaji = resultSet.getDouble("gaji");
        
        LocalTime waktuMasuk = LocalTime.parse(jammasuk);
        LocalTime waktuKeluar = LocalTime.parse(jamkeluar);
        
        long durasiMenit = ChronoUnit.MINUTES.between(waktuMasuk, waktuKeluar);
        
        long jam = durasiMenit / 60;
        long menit = durasiMenit % 60;
        
        String durasiKerjaFormat = jam + " jam " + menit + " menit";
        
        return new Kehadiran(namapegawai, tanggal, jammasuk, jamkeluar, durasiKerjaFormat, gaji);
    }

    public boolean absen(LocalDate currentDate, LocalTime currentTime, String noHp){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedTime = currentTime.format(formatter);
        LocalTime time = LocalTime.parse(formattedTime, formatter);

        String sql = "INSERT INTO DaftarKehadiran (tanggal, jamMasuk, jamkeluar, nomorHP, gaji) VALUES (?, ?, ?, ?, ?)";
        try {
            jdbcTemplate.update(sql, currentDate, time, time, noHp, 0);
            return true;
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public int checkAttendance(LocalDate currentDate, String noHp) {
        String sql = "SELECT COUNT(*) FROM daftarkehadiran WHERE tanggal = ? AND nomorhp = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, currentDate, noHp);
        return count != null ? count : 0;
    }
}
