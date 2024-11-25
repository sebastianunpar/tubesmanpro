package com.example.tubesmanpro.pegawai;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/pegawai")
public class PegawaiController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/")
    public String showLogin (Model model) {
        return "index";
    }

    @GetMapping("/dashboard")
    public String dashboard (Model model) {
        return "pegawai/mainMenuPegawai";
    }

    @GetMapping("/kehadiran")
    public String kehadiran (Model model) {
        return "pegawai/attendance_check";
    }

    @GetMapping("/profile")
    public String profile (Model model) {
        return "pegawai/profilePegawai";
    }

    @GetMapping("/gaji")
    public String cekGaji (Model model) {
        return "pegawai/salary_check";
    }

    @PostMapping("/")
    public String login (@RequestParam("noHp") String noHp) {
        return "pegawai/confirmOtp";
    }

    @PostMapping("/pegawai/dashboard")
    public String confirmOtp (@RequestParam("otp") String otp) {
        return "pegawai/mainMenuPegawai";
    }
    
}
