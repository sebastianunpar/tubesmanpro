package com.example.tubesmanpro.owner;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/owner")
public class OwnerController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping({"", "/"})
    public String showLogin (Model model) {
        return "owner/login";
    }

    public List<Owner> findOwner (String username, String password) {
        String sql = "SELECT * FROM pemilik WHERE usernamepemilik = ? AND passwordpemilik = ?";
        return jdbcTemplate.query(sql, this::mapRowToOwner, username, password);
    }

    private Owner mapRowToOwner(ResultSet resultSet, int rowNum) throws SQLException {
        return new Owner(
            resultSet.getString("namapemilik"),
            resultSet.getString("usernamepemilik"),
            resultSet.getString("passwordpemilik")
        );
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model, HttpSession session) {
        if (session.getAttribute("loggedInOwner") == null) {
            return "redirect:/owner";
        }
        return "owner/dashboard";
    }

    @PostMapping("/login")
    public String login (@RequestParam("username") String username, @RequestParam("password") String password, Model model, HttpSession session) {
        List<Owner> owners = findOwner(username, password);
        if (owners.isEmpty()) {
            model.addAttribute("error", "Invalid");
            model.addAttribute("username", username);
            model.addAttribute("password", password);
            return "owner/login";
        }

        Owner owner = owners.get(0);
        session.setAttribute("loggedInOwner", owner.getUsername());

        return "redirect:/owner/dashboard";
    }

    @GetMapping("/tambah-data")
    public String tambahData(HttpSession session) {
        if (session.getAttribute("loggedInOwner") == null) {
            return "redirect:/owner";
        }
        return "owner/insertData";
    }

    @GetMapping("/list-pegawai")
    public String showListPegawai(HttpSession session) {
        if (session.getAttribute("loggedInOwner") == null) {
            return "redirect:/owner";
        }
        return "owner/listPegawai";
    }

    @GetMapping("/update-pegawai")
    public String updatePegawai(HttpSession session) {
        if (session.getAttribute("loggedInOwner") == null) {
            return "redirect:/owner";
        }
        return "owner/updatePegawai";
    }

    @GetMapping("/laporan-kehadiran")
    public String showTambahData(HttpSession session) {
        if (session.getAttribute("loggedInOwner") == null) {
            return "redirect:/owner";
        }
        return "owner/laporanKehadiran";
    }

    @GetMapping("/bayar-gaji")
    public String bayarGaji(HttpSession session) {
        if (session.getAttribute("loggedInOwner") == null) {
            return "redirect:/owner";
        }
        return "owner/bayarGaji";
    }

    @PostMapping("/logout")
    public String logOut (HttpSession session) {
        session.invalidate();
        return "redirect:/owner";
    }
}
