package com.example.tubesmanpro.owner;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.tubesmanpro.kehadiran.kehadiran;

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
        String sql = "select namapegawai, tanggal, jammasuk, jamkeluar, durasikerja from daftarkehadiran inner join pegawai on daftarkehadiran.nomorhp = pegawai.nomorhp";
        return jdbcTemplate.query(sql, this::mapRowToKehadiran);
    }

    private Owner mapRowToOwner(ResultSet resultSet, int rowNum) throws SQLException {
        return new Owner(
            resultSet.getString("namapemilik"),
            resultSet.getString("usernamepemilik"),
            resultSet.getString("passwordpemilik")
        );
    }

    private kehadiran mapRowToKehadiran(ResultSet resultSet, int rowNum) throws SQLException {
        return new kehadiran(
            resultSet.getString("namapegawai"),
            resultSet.getString("tanggal"),
            resultSet.getString("jammasuk"),
            resultSet.getString("jamkeluar"),
            resultSet.getDouble("durasikerja")
        );
    }
}