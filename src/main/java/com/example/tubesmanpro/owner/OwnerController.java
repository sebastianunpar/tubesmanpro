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

import com.example.tubesmanpro.kehadiran.Kehadiran;
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
        List<Kehadiran> seluruhKehadiran = this.repo.showKehadiran();
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
    public String tambahData(HttpSession session, Model model) {
        if (session.getAttribute("loggedInOwner") == null) {
            return "redirect:/owner";
        }
        return "owner/insertData";
    }

    @PostMapping("/tambah-jabatan")
    public String tambahDataPost(@RequestParam("namaJabatan") String namaJabatan,
                                @RequestParam("gaji") double gaji,
                                HttpSession session, Model model) {
        if (session.getAttribute("loggedInOwner") == null) {
            return "redirect:/owner";
        }
        boolean res = this.repo.saveJabatan(namaJabatan, gaji);
        if (res) {
            model.addAttribute("successMessage", "Data berhasil ditambahkan ke Database!");
        } else{
            model.addAttribute("errorMessage", "Gagal menambahkan data ke Database!");
        }
        return "owner/insertData";
    }


    // Menambahkan data pegawai ke dalam database
    @PostMapping("/tambah-pegawai")
    public String tambahPegawai(@RequestParam("nama") String nama, 
                                @RequestParam("no_hp") String noHp, 
                                @RequestParam("email") String email, 
                                @RequestParam("alamat") String alamat, 
                                @RequestParam("jabatan") String jabatan, 
                                HttpSession session, Model model) {
        if (session.getAttribute("loggedInOwner") == null) {
            return "redirect:/owner";
        }

        // Membuat objek Pegawai baru dengan data yang diterima
        Pegawai pegawai = new Pegawai(nama, noHp, email, jabatan, alamat);
        
        // Simpan pegawai ke dalam database
        boolean isSuccess = repo.savePegawai(pegawai);
        
        if (isSuccess) {
            model.addAttribute("message", "Pegawai berhasil ditambahkan!");
        } else {
            model.addAttribute("error", "Gagal menambahkan pegawai. Coba lagi.");
        }

        return "owner/insertData";
    }

    @GetMapping("/list-pegawai")
    public String showListPegawai(HttpSession session, Model model) {
        if (session.getAttribute("loggedInOwner") == null) {
            return "redirect:/owner";
        }
        List<Pegawai> pegawaiList = this.repo.showAllPegawai();
        System.out.println(pegawaiList);
        model.addAttribute("pegawaiList", pegawaiList);
        return "owner/listPegawai";
    }

    @PostMapping("/cek-data-pegawai")
    public String cekDataPegawai(@RequestParam("nomorhp") String nomorhp, Model model) {
        if (nomorhp == null || nomorhp.isEmpty()) {
            model.addAttribute("error", "Silakan pilih pegawai terlebih dahulu.");
            return "owner/listPegawai";
        }
        Pegawai pegawai = this.repo.getPegawaiByNomorHp(nomorhp);
        
        if (pegawai != null) {
            model.addAttribute("pegawai", pegawai);
        } else {
            model.addAttribute("error", "Pegawai tidak ditemukan.");
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
