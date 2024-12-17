package com.example.tubesmanpro.owner;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        List<String> listKelurahan = this.repo.showAllKelurahan();
        List<String> listKecamatan = this.repo.showAllKecamatan();
        List<String> listJabatan = this.repo.showAllJabatan();

        model.addAttribute("listKecamatan", listKecamatan);
        model.addAttribute("listKelurahan", listKelurahan);
        model.addAttribute("listJabatan", listJabatan);

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
        List<String> listKelurahan = this.repo.showAllKelurahan();
        List<String> listKecamatan = this.repo.showAllKecamatan();
        List<String> listJabatan = this.repo.showAllJabatan();

        model.addAttribute("listKecamatan", listKecamatan);
        model.addAttribute("listKelurahan", listKelurahan);
        model.addAttribute("listJabatan", listJabatan);
        return "owner/insertData";
    }

    @PostMapping("/tambah-pegawai")
    public String tambahPegawai(@RequestParam("nama") String nama, 
                                @RequestParam("no_hp") String noHp, 
                                @RequestParam("email") String email, 
                                @RequestParam("alamat") String alamat, 
                                @RequestParam("kecamatan") String kecamatan,
                                @RequestParam("kelurahan") String kelurahan,
                                @RequestParam("jabatan") String jabatan,
                                HttpSession session, 
                                Model model) {
        if (session.getAttribute("loggedInOwner") == null) {
            return "redirect:/owner";
        }

        boolean isSuccess = this.repo.savePegawai(nama, noHp, email, alamat, kelurahan, jabatan);
        
        if (isSuccess) {
            model.addAttribute("successMessage", "Data berhasil ditambahkan ke Database!");
        } else {
            model.addAttribute("errorMessage", "Gagal menambahkan data ke Database!");
        }
        List<String> listKelurahan = this.repo.showAllKelurahan();
        List<String> listKecamatan = this.repo.showAllKecamatan();
        List<String> listJabatan = this.repo.showAllJabatan();

        model.addAttribute("listKecamatan", listKecamatan);
        model.addAttribute("listKelurahan", listKelurahan);
        model.addAttribute("listJabatan", listJabatan);
        return "owner/insertData";
    }

    @GetMapping("/list-pegawai")
    public String showListPegawai(HttpSession session, Model model) {
        if (session.getAttribute("loggedInOwner") == null) {
            return "redirect:/owner";
        }
        List<Pegawai> pegawaiList = this.repo.showAllPegawai();
        model.addAttribute("pegawaiList", pegawaiList);
        model.addAttribute("listPegawai", pegawaiList);
        return "owner/listPegawai";
    }

    @PostMapping("/list-pegawai")
    public String cekDataPegawai(@RequestParam("nomorhp") String nomorhp, Model model) {
        List<Pegawai> pegawaiList = this.repo.showAllPegawai();
        model.addAttribute("pegawaiList", pegawaiList);
        model.addAttribute("listPegawai", pegawaiList);
        if (nomorhp.equals("")) {
            List<Pegawai> listPegawai = this.repo.showAllPegawai();
            model.addAttribute("listPegawai", listPegawai);
        } else {
            Pegawai pegawai = this.repo.getPegawaiByNomorHp(nomorhp);
            model.addAttribute("pegawai", pegawai);
        }
        return "owner/listPegawai"; 
    }
    

    @GetMapping("/update-pegawai")
    public String updatePegawai(Model model, HttpSession session) {
        List<Pegawai> allPegawai = this.repo.showAllPegawai();
        if (session.getAttribute("loggedInOwner") == null) {
            return "redirect:/owner";
        }
        model.addAttribute("pegawaiList", allPegawai);
        return "owner/updatePegawai";
    }

    @PostMapping("/update-pegawai")
    public String updatePegawaiPost(@RequestParam("namapegawai") String namapegawai, Model model, HttpSession session) {
        List<Pegawai> allPegawai = this.repo.showAllPegawai();
        if (session.getAttribute("loggedInOwner") == null) {
            return "redirect:/owner";
        }
        Pegawai pegawai = this.repo.getPegawaiByNama(namapegawai);
        List<String> listKelurahan = this.repo.showAllKelurahan();
        List<String> listKecamatan = this.repo.showAllKecamatan();
        List<String> listJabatan = this.repo.showAllJabatan();

        model.addAttribute("listKecamatan", listKecamatan);
        model.addAttribute("listKelurahan", listKelurahan);
        model.addAttribute("listJabatan", listJabatan);
        model.addAttribute("pegawaiList", allPegawai);
        model.addAttribute("pegawai", pegawai);
        return "owner/updatePegawai";
    }

    @PostMapping("/update-pegawai2")
    public String updatePegawaiPost2(@RequestParam("nama") String nama, 
                                    @RequestParam("no_hp") String noHp, 
                                    @RequestParam("email") String email, 
                                    @RequestParam("alamat") String alamat, 
                                    @RequestParam("kecamatan") String kecamatan,
                                    @RequestParam("kelurahan") String kelurahan,
                                    @RequestParam("jabatan") String jabatan,
                                    HttpSession session, 
                                    Model model) {
        List<Pegawai> allPegawai = this.repo.showAllPegawai();
        if (session.getAttribute("loggedInOwner") == null) {
            return "redirect:/owner";
        }
        Pegawai pegawai = this.repo.getPegawaiByNama(nama);
        List<String> listKelurahan = this.repo.showAllKelurahan();
        List<String> listKecamatan = this.repo.showAllKecamatan();
        List<String> listJabatan = this.repo.showAllJabatan();
        boolean isupdate = this.repo.updatePegawai(nama, noHp, email, alamat, kelurahan, jabatan);

        model.addAttribute("listKecamatan", listKecamatan);
        model.addAttribute("listKelurahan", listKelurahan);
        model.addAttribute("listJabatan", listJabatan);
        model.addAttribute("pegawaiList", allPegawai);
        model.addAttribute("pegawai", pegawai);
        if (isupdate) {
            model.addAttribute("successMessage", "Data berhasil diupdate ke Database!");
        } else{
            model.addAttribute("errorMessage", "Gagal update data ke Database!");
        }
        return "owner/updatePegawai";
    }

    @GetMapping("/laporan-kehadiran")
    public String showTambahData(HttpSession session, Model model) {
        if (session.getAttribute("loggedInOwner") == null) {
            return "redirect:/owner";
        }
        List<Pegawai> allPegawai = this.repo.showAllPegawai();
        model.addAttribute("pegawaiList", allPegawai);
        return "owner/laporanKehadiran";
    }

    @PostMapping("/laporan-kehadiran")
    public String postTambahData(@RequestParam("nomorhp") String noHp, HttpSession session, Model model) {
        if (session.getAttribute("loggedInOwner") == null) {
            return "redirect:/owner";
        }
        List<Pegawai> allPegawai = this.repo.showAllPegawai();
        List<KehadiranPegawai> pegawai = this.repo.findAllKehadiran();
        if (!noHp.equals("full")) {
            pegawai = this.repo.findKehadiranByNomorHp(noHp);
        }
        model.addAttribute("pegawai", pegawai);
        model.addAttribute("pegawaiList", allPegawai);
        return "owner/laporanKehadiran";
    }

    @GetMapping("/bayar-gaji")
    public String bayarGaji(HttpSession session, Model model) {
        if (session.getAttribute("loggedInOwner") == null) {
            return "redirect:/owner";
        }
        List<Pegawai> allPegawai = this.repo.showAllPegawai();
        model.addAttribute("pegawaiList", allPegawai);

        return "owner/bayarGaji";
    }

    @PostMapping("/bayar-gaji")
    public String bayarGajiPost(@RequestParam("nomorhp") String noHp, HttpSession session, Model model) {
        if (session.getAttribute("loggedInOwner") == null) {
            return "redirect:/owner";
        }
        List<Pegawai> allPegawai = this.repo.showAllPegawai();
        List<KehadiranPegawai> pegawai = this.repo.findAllKehadiran();
        if (!noHp.equals("full")) {
            pegawai = this.repo.findKehadiranByNomorHp(noHp);
            String nama = pegawai.getFirst().getNama();
            String periode = pegawai.getFirst().getTanggal();
            
            int totalJam = 0;
            int totalMenit = 0;
            double totalGaji = 0;

            for (KehadiranPegawai kehadiranPegawai : pegawai) {
                totalGaji += kehadiranPegawai.getGaji();
                
                String durasiKerja = kehadiranPegawai.getDurasiKerja();
                
                Pattern pattern = Pattern.compile("(\\d+)\\s*jam\\s*(\\d+)\\s*menit");
                Matcher matcher = pattern.matcher(durasiKerja);
                
                if (matcher.find()) {
                    int jam = Integer.parseInt(matcher.group(1));
                    int menit = Integer.parseInt(matcher.group(2));
                    
                    totalJam += jam;
                    totalMenit += menit;
                }
            }

            totalJam += totalMenit / 60;
            totalMenit = totalMenit % 60;

            String durasi = totalJam + " jam " + totalMenit + " menit";

            model.addAttribute("nama", nama);
            model.addAttribute("periode", periode);
            model.addAttribute("durasi", durasi);
            model.addAttribute("totalGaji", totalGaji);
            model.addAttribute("pegawai", null);
        } else {
            model.addAttribute("pegawai", pegawai);
        }
        model.addAttribute("pegawaiList", allPegawai);

        return "owner/bayarGaji";
    }

    @PostMapping("/logout")
    public String logOut (HttpSession session) {
        session.invalidate();
        return "redirect:/owner";
    }
}
