package com.example.tubesmanpro.owner;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.LocalDate;

import com.example.tubesmanpro.kehadiran.kehadiran;
import com.example.tubesmanpro.pegawai.Pegawai;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/owner")
public class OwnerController {

    @Autowired
    private JdbcOwnerImplementation repo;

    @GetMapping({"", "/"})
    public String showLogin (Model model) {
        return "owner/login";
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model, HttpSession session) {
        if (session.getAttribute("loggedInOwner") == null) {
            return "redirect:/owner";
        }
        List<kehadiran> seluruhKehadiran = this.repo.showKehadiran();
        model.addAttribute("kehadirans",seluruhKehadiran);
        model.addAttribute("today", LocalDate.now().toString());
        return "owner/dashboard";
    }

    @PostMapping("/login")
    public String login (@RequestParam("username") String username, @RequestParam("password") String password, Model model, HttpSession session) {
        List<Owner> owners = this.repo.findOwner(username, password);
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
    public String updatePegawai(Model model, HttpSession session) {
        List<Pegawai> allPegawai = this.repo.showUpdatePegawai();
        if (session.getAttribute("loggedInOwner") == null) {
            return "redirect:/owner";
        }
        model.addAttribute("pegawaiList", allPegawai);
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
