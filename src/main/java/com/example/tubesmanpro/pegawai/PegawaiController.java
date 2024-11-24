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
@RequestMapping("")
public class PegawaiController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/")
    public String showLogin (Model model) {
        return "index";
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
