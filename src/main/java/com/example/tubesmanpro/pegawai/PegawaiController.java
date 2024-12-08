package com.example.tubesmanpro.pegawai;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import com.example.tubesmanpro.kehadiran.Kehadiran;


@Controller
@RequestMapping("")
public class PegawaiController {
    @Autowired
    private PegawaiService pegawaiService;

    @GetMapping("/")
    public String showLogin (Model model) {
        return "index";
    }

    @PostMapping("/")
    public String login (@RequestParam("noHp") String noHp, Model model, HttpSession session) {
        List<Pegawai> workers = pegawaiService.findPegawaiByNoHp(noHp);
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
        String noHp = (String) session.getAttribute("loggedInPegawai");
        LocalDate today = LocalDate.now();
        LocalDate monday = pegawaiService.getMondayOfWeek(today);
        LocalDate sunday = pegawaiService.getSundayOfWeek(monday);

        List<Kehadiran> kehadirans = pegawaiService.getKehadiranPerWeek(noHp, monday, sunday);
        double totalGaji = pegawaiService.calculateTotalGaji(kehadirans);
        int jamKerja = pegawaiService.calculateJamKerja(kehadirans);
        model.addAttribute("senin", pegawaiService.formatDate(monday));
        model.addAttribute("minggu", pegawaiService.formatDate(sunday));
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
        PegawaiProfileDTO pegawaiProfile = pegawaiService.getPegawaiProfileDTO(noHp);


        if (pegawaiProfile != null) {
            model.addAttribute("nama", pegawaiProfile.getNama());
            model.addAttribute("nohp", pegawaiProfile.getNoHp());
            model.addAttribute("jabatan", pegawaiProfile.getJabatan());
            model.addAttribute("alamat", pegawaiProfile.getAlamat());
            model.addAttribute("kelurahan", pegawaiProfile.getKelurahan());
            model.addAttribute("kecamatan", pegawaiProfile.getKecamatan());
        }
        return "pegawai/profil";
    }

    @GetMapping("/pegawai/kehadiran")
    public String showKehadiran (HttpSession session, Model model) {
        if (session.getAttribute("loggedInPegawai") == null) {
            return "redirect:/";
        }
        String noHp = (String) session.getAttribute("loggedInPegawai");
        LocalDate today = LocalDate.now();
        LocalDate monday = pegawaiService.getMondayOfWeek(today);
        LocalDate sunday = pegawaiService.getSundayOfWeek(monday);

        List<Kehadiran> kehadirans = pegawaiService.getKehadiranPerWeek(noHp, monday, sunday);
        
        model.addAttribute("senin", pegawaiService.formatDate(monday));
        model.addAttribute("minggu", pegawaiService.formatDate(sunday));
        model.addAttribute("kehadiran", kehadirans);
        return "pegawai/kehadiran";
    }

    @GetMapping("/pegawai/gaji")
    public String showAttendace (HttpSession session, Model model) {
        if (session.getAttribute("loggedInPegawai") == null) {
            return "redirect:/";
        }
        String noHp = (String) session.getAttribute("loggedInPegawai");
        LocalDate today = LocalDate.now();
        LocalDate monday = pegawaiService.getMondayOfWeek(today);
        LocalDate sunday = pegawaiService.getSundayOfWeek(monday);

        List<Kehadiran> kehadirans = pegawaiService.getKehadiranPerWeek(noHp, monday, sunday);
        double totalGaji = pegawaiService.calculateTotalGaji(kehadirans);
        model.addAttribute("senin", pegawaiService.formatDate(monday));
        model.addAttribute("minggu", pegawaiService.formatDate(sunday));
        model.addAttribute("kehadiran", kehadirans);
        model.addAttribute("totalGaji", totalGaji);
        return "pegawai/gaji";
    }

    @PostMapping("/pegawai/logout")
    public String logOut (HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @PostMapping("/pegawai/kehadiran")
    public String absen(HttpSession session, RedirectAttributes redirectAttributes){
        if (session.getAttribute("loggedInPegawai") == null) {
            return "redirect:/";
        }

        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();
        String noHp = (String) session.getAttribute("loggedInPegawai");

        boolean success = pegawaiService.absen(currentDate, currentTime, noHp);
        if (!success) {
            redirectAttributes.addFlashAttribute("absenError", "sudah absen");
        }

        return "redirect:/pegawai/kehadiran";
    }
}