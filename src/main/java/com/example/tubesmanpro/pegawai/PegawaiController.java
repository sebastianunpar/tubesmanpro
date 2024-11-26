package com.example.tubesmanpro.pegawai;

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
@RequestMapping("")
public class PegawaiController {
    @Autowired
    // private JdbcTemplate jdbcTemplate;
    private JdbcPegawaiImplementation repo;

    @GetMapping("/")
    public String showLogin (Model model) {
        return "index";
    }

    @PostMapping("/")
    public String login (@RequestParam("noHp") String noHp, Model model, HttpSession session) {
        List<Pegawai> workers = this.repo.findPegawai(noHp);
        if (workers.isEmpty()){
            model.addAttribute("noHp", noHp);
            model.addAttribute("error", "Not found");
            return "index";
        }
        session.setAttribute("noHp", noHp);
        return "pegawai/confirmOtp";
    }

    @PostMapping("/confirm-otp")
    public String confirmOtp (@RequestParam("otp") String otp, Model model, HttpSession session){
        if (!otp.equals("otp")) {
            model.addAttribute("otp", otp);
            model.addAttribute("error", "Invalid OTP");
            return "pegawai/confirmOtp";
        }

        String noHp = (String) session.getAttribute("noHp");
        if (noHp == null) {
            return "redirect:/";
        }
        session.removeAttribute("noHp");
        session.setAttribute("loggedInPegawai", noHp);

        return "redirect:/pegawai/dashboard";
    }

    @GetMapping("/pegawai/dashboard")
    public String showDashboard (HttpSession session) {
        if (session.getAttribute("loggedInPegawai") == null) {
            return "redirect:/";
        }
        return "pegawai/dashboard";
    }

    @GetMapping("/pegawai/profil")
    public String showProfile (HttpSession session) {
        if (session.getAttribute("loggedInPegawai") == null) {
            return "redirect:/";
        }
        return "pegawai/profil";
    }

    @GetMapping("/pegawai/kehadiran")
    public String showKehadiran (HttpSession session) {
        if (session.getAttribute("loggedInPegawai") == null) {
            return "redirect:/";
        }
        return "pegawai/kehadiran";
    }

    @GetMapping("/pegawai/gaji")
    public String showAttendace (HttpSession session) {
        if (session.getAttribute("loggedInPegawai") == null) {
            return "redirect:/";
        }
        return "pegawai/gaji";
    }

    @PostMapping("/pegawai/logout")
    public String logOut (HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}