package com.example.tubesmanpro.pegawai;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
 
import jakarta.servlet.http.HttpSession;
import com.example.tubesmanpro.kehadiran.Kehadiran;


@Controller
@RequestMapping("")
public class PegawaiController {
    @Autowired
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
    public String showDashboard (HttpSession session, Model model) {
        if (session.getAttribute("loggedInPegawai") == null) {
            return "redirect:/";
        }
        double totalGaji = 0;
        int jamKerja = 0;
        LocalDate today = LocalDate.now();
        DayOfWeek currentDayOfWeek = today.getDayOfWeek();
        int daysToSubtract = currentDayOfWeek.getValue() - DayOfWeek.MONDAY.getValue();
        LocalDate monday = today.minusDays(daysToSubtract);
        LocalDate sunday = monday.plusDays(6);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy");
        String formattedMonday = monday.format(formatter);
        String formattedSunday = sunday.format(formatter);
        String noHp = (String) session.getAttribute("loggedInPegawai");
        List<Kehadiran> kehadirans= repo.getKehadiranPerWeek(noHp, monday, sunday);
        for (Kehadiran kehadiran : kehadirans) {
            totalGaji += kehadiran.getGaji();
        }
        for (Kehadiran kehadiran : kehadirans) {
            String jammasuk = kehadiran.getJammasuk();
            String jamkeluar = kehadiran.getJamkeluar();
            LocalTime waktuMasuk = LocalTime.parse(jammasuk);
            LocalTime waktuKeluar = LocalTime.parse(jamkeluar);
            long durasiMenit = ChronoUnit.MINUTES.between(waktuMasuk, waktuKeluar);
            int jam = (int) durasiMenit / 60;
            jamKerja += jam;
        }
        model.addAttribute("senin", formattedMonday);
        model.addAttribute("minggu", formattedSunday);
        model.addAttribute("jamKerja", jamKerja);
        model.addAttribute("totalGaji", totalGaji);
        return "pegawai/dashboard";
    }

    @GetMapping("/pegawai/profil")
    public String showProfile (HttpSession session, Model model) {
        if (session.getAttribute("loggedInPegawai") == null) {
            return "redirect:/";
        }
        String noHp = (String) session.getAttribute("loggedInPegawai");
        List<Pegawai> pegawais = repo.findPegawai(noHp);
        if (pegawais.size() == 1) {
            Pegawai pegawai = pegawais.get(0);
            String idJabatan = pegawai.getJabatan();
            List<String> jabatans = repo.getJabatanFromId(Integer.parseInt(idJabatan));
            String jabatan = jabatans.get(0);
            String idAlamat = pegawai.getAlamat();
            List<String> alamats = repo.getAlamatFromId(Integer.parseInt(idAlamat));
            List<String> kelurahans = repo.getKelurahanFromIdAlamat(Integer.parseInt(idAlamat));
            List<String> kecamatans = repo.getKecamatanFromIdAlamat(Integer.parseInt(idAlamat));
            String alamat = alamats.get(0);
            String kelurahan = kelurahans.get(0);
            String kecamatan = kecamatans.get(0);
            model.addAttribute("nama", pegawai.getNamapegawai());
            model.addAttribute("nohp", pegawai.getNomorhp());
            model.addAttribute("jabatan", jabatan);
            model.addAttribute("alamat", alamat);
            model.addAttribute("kelurahan", kelurahan);
            model.addAttribute("kecamatan", kecamatan);
        }
        return "pegawai/profil";
    }

    @GetMapping("/pegawai/kehadiran")
    public String showKehadiran (HttpSession session, Model model) {
        if (session.getAttribute("loggedInPegawai") == null) {
            return "redirect:/";
        }
        LocalDate today = LocalDate.now();
        DayOfWeek currentDayOfWeek = today.getDayOfWeek();
        int daysToSubtract = currentDayOfWeek.getValue() - DayOfWeek.MONDAY.getValue();
        LocalDate monday = today.minusDays(daysToSubtract);
        LocalDate sunday = monday.plusDays(6);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy");
        String formattedMonday = monday.format(formatter);
        String formattedSunday = sunday.format(formatter);
        String noHp = (String) session.getAttribute("loggedInPegawai");
        List<Kehadiran> kehadiran= repo.getKehadiranPerWeek(noHp, monday, sunday);
        model.addAttribute("senin", formattedMonday);
        model.addAttribute("minggu", formattedSunday);
        model.addAttribute("kehadiran", kehadiran);
        return "pegawai/kehadiran";
    }

    @GetMapping("/pegawai/gaji")
    public String showAttendace (HttpSession session, Model model) {
        if (session.getAttribute("loggedInPegawai") == null) {
            return "redirect:/";
        }
        double totalGaji = 0;
        LocalDate today = LocalDate.now();
        DayOfWeek currentDayOfWeek = today.getDayOfWeek();
        int daysToSubtract = currentDayOfWeek.getValue() - DayOfWeek.MONDAY.getValue();
        LocalDate monday = today.minusDays(daysToSubtract);
        LocalDate sunday = monday.plusDays(6);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy");
        String formattedMonday = monday.format(formatter);
        String formattedSunday = sunday.format(formatter);
        String noHp = (String) session.getAttribute("loggedInPegawai");
        List<Kehadiran> kehadirans= repo.getKehadiranPerWeek(noHp, monday, sunday);
        for (Kehadiran kehadiran : kehadirans) {
            totalGaji += kehadiran.getGaji();
        }
        model.addAttribute("senin", formattedMonday);
        model.addAttribute("minggu", formattedSunday);
        model.addAttribute("kehadiran", kehadirans);
        model.addAttribute("totalGaji", totalGaji);
        return "pegawai/gaji";
    }

    @PostMapping("/pegawai/logout")
    public String logOut (HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    // @PostMapping("/pegawai/kehadiran/hadir")
    // public String absen (){

    // }

    // @PostMapping("/pegawai/kehadiran/pulang")
    // public String pulang (){
        
    // }
}