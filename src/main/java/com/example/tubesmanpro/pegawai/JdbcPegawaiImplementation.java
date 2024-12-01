package com.example.tubesmanpro.pegawai;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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
            resultSet.getString("namajabatan"),
            resultSet.getString("namajalan")
        );
    }
}
