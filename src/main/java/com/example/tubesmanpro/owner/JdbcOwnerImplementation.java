package com.example.tubesmanpro.owner;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.tubesmanpro.kehadiran.Kehadiran;
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

    public List<Kehadiran> showKehadiran () {
        String sql = "select namapegawai, tanggal, jammasuk, jamkeluar, gaji from daftarkehadiran inner join pegawai on daftarkehadiran.nomorhp = pegawai.nomorhp";
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

    private KehadiranPegawai mapRowToKehadiranPegawai(ResultSet resultSet, int rowNum) throws SQLException {
        return new KehadiranPegawai(
            resultSet.getString("namapegawai"),
            resultSet.getString("tanggal"),
            resultSet.getString("jammasuk"),
            resultSet.getString("jamkeluar"),
            resultSet.getDouble("gaji")
        );
    }

    public List<KehadiranPegawai> findKehadiranByNomorHp(String nomorHp) {
        String query = """
            SELECT pegawai.namapegawai, daftarkehadiran.tanggal, 
                   daftarkehadiran.jammasuk, daftarkehadiran.jamkeluar, daftarkehadiran.gaji
            FROM daftarkehadiran
            JOIN pegawai ON pegawai.nomorhp = daftarkehadiran.nomorhp
            WHERE pegawai.nomorhp = ?
            """;        
        try {
            return jdbcTemplate.query(query, this::mapRowToKehadiranPegawai, nomorHp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        return null;
    }

    public List<KehadiranPegawai> findAllKehadiran() {
        String query = """
            SELECT pegawai.namapegawai, daftarkehadiran.tanggal, 
                   daftarkehadiran.jammasuk, daftarkehadiran.jamkeluar, daftarkehadiran.gaji
            FROM daftarkehadiran
            JOIN pegawai ON pegawai.nomorhp = daftarkehadiran.nomorhp
            """;        
        try {
            return jdbcTemplate.query(query, this::mapRowToKehadiranPegawai);
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        return null;
    }    
        

    private Kehadiran mapRowToKehadiran(ResultSet resultSet, int rowNum) throws SQLException {
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

    public Pegawai getPegawaiByNama(String nama) {
        String sql = "SELECT * FROM pegawai WHERE namapegawai = ?";
        try {
            return jdbcTemplate.queryForObject(sql, this::mapRowToPegawai, nama);
        } catch (Exception e) {
            return null;
        }
    }

    public List<String> showAllKecamatan() {
        String sql = "SELECT namakecamatan FROM kecamatan";
        try {
            return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getString("namakecamatan"));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<String> showAllKelurahan() {
        String sql = "SELECT namakelurahan FROM kelurahan";
        try {
            return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getString("namakelurahan"));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<String> showAllJabatan() {
        String sql = "SELECT namajabatan FROM jabatan";
        try {
            return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getString("namajabatan"));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public Boolean savePegawai(String nama, String noHp, String email, String namajalan, String kelurahan, String jabatan) {
        String sql = "INSERT INTO Pegawai (nomorHP, namaPegawai, email, idJabatan, idAlamat, status) VALUES (?, ?, ?, ?, ?, ?)";
        Integer idJabatan = getJabatanIdByName(jabatan);
        Integer idKelurahan = getKelurahanIdByName(kelurahan);

        String insertSql = "INSERT INTO alamat (namajalan, idkelurahan) VALUES (?, ?)";
        jdbcTemplate.update(insertSql, namajalan, idKelurahan);
        Integer idAlamat = getAlamatIdByName(namajalan);

        if (idJabatan == null || idAlamat == null || idKelurahan == null) {
            return false;
        }
    
        try {

            int result = jdbcTemplate.update(sql, noHp, nama, email, idJabatan, idAlamat, 1);
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean updatePegawai(String nama, String noHp, String email, String namajalan, String kelurahan, String jabatan) {
        Integer idJabatan = getJabatanIdByName(jabatan);
        Integer idKelurahan = getKelurahanIdByName(kelurahan);

        Integer idAlamat = getAlamatIdByName(namajalan);
        
        if (idAlamat == null) {
            String insertSql = "INSERT INTO alamat (namajalan, idkelurahan) VALUES (?, ?)";
            jdbcTemplate.update(insertSql, namajalan, idKelurahan);
            idAlamat = getAlamatIdByName(namajalan);
        }

        Pegawai pegawaiLama = getPegawaiByNama(nama);
        if (pegawaiLama == null) {
            return false; 
        }

        if (!noHp.equals(pegawaiLama.getNomorhp())) {
            String deleteDaftarKehadiranSql = "DELETE FROM daftarkehadiran WHERE nomorhp = ?";
            try {
                jdbcTemplate.update(deleteDaftarKehadiranSql, pegawaiLama.getNomorhp());

                String updatePegawaiSql = "UPDATE pegawai SET nomorhp = ?, email = ?, idjabatan = ?, idalamat = ? WHERE namapegawai = ?";
                jdbcTemplate.update(updatePegawaiSql, noHp, email, idJabatan, idAlamat, nama);

                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false; 
            }
        }

        String sql = "UPDATE pegawai SET email = ?, idjabatan = ?, idalamat = ? WHERE namapegawai = ?";
        try {
            int result = jdbcTemplate.update(sql, email, idJabatan, idAlamat, nama);
            return result > 0; 
        } catch (Exception e) {
            e.printStackTrace();
            return false; 
        }
    }


    private Integer getKelurahanIdByName(String Namakelurahan) {
        String sql = "SELECT idkelurahan FROM kelurahan WHERE namakelurahan = ?";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, Namakelurahan);
        } catch (Exception e) {
            return null;
        }
    }

    private Integer getAlamatIdByName(String namajalan) {
        String sql = "SELECT idalamat FROM alamat WHERE namajalan = ?";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, namajalan);
        } catch (Exception e) {
            return null;
        }
    }


    public boolean saveJabatan(String namajabatan, double gaji) {
        String checkSql = "SELECT COUNT(*) FROM jabatan WHERE namajabatan = ?";
        int count = jdbcTemplate.queryForObject(checkSql, new Object[]{namajabatan}, Integer.class);
    
        if (count > 0) {
            return false;
        }
    
        String insertSql = "INSERT INTO jabatan (namajabatan, satuangaji) VALUES (?, ?)";
        int result = jdbcTemplate.update(insertSql, namajabatan, gaji);
    
        return result > 0;
    }

    private Integer getJabatanIdByName(String jabatanName) {
        String sql = "SELECT idJabatan FROM Jabatan WHERE namaJabatan = ?";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, jabatanName);
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
