package com.example.tubesmanpro.pegawai;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.tubesmanpro.kehadiran.Kehadiran;

@Service
public class PegawaiService {

    @Autowired
    private JdbcPegawaiImplementation repo;

    public List<Pegawai> findPegawaiByNoHp(String noHp) {
        return repo.findPegawai(noHp);
    }

    public double calculateTotalGaji(List<Kehadiran> kehadirans) {
        return kehadirans.stream().mapToDouble(Kehadiran::getGaji).sum();
    }

    public int calculateJamKerja(List<Kehadiran> kehadirans) {
        int jamKerja = 0;
        for (Kehadiran kehadiran : kehadirans) {
            LocalTime waktuMasuk = LocalTime.parse(kehadiran.getJammasuk());
            LocalTime waktuKeluar = LocalTime.parse(kehadiran.getJamkeluar());
            long durasiMenit = ChronoUnit.MINUTES.between(waktuMasuk, waktuKeluar);
            jamKerja += durasiMenit / 60;
        }
        return jamKerja;
    }

    public List<Kehadiran> getKehadiranPerWeek(String noHp, LocalDate monday, LocalDate sunday) {
        return repo.getKehadiranPerWeek(noHp, monday, sunday);
    }

    public Pegawai getPegawaiProfile(String noHp) {
        List<Pegawai> pegawais = repo.findPegawai(noHp);
        if (pegawais.size() == 1) {
            return pegawais.get(0);
        }
        return null;
    }

    public String formatDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy");
        return date.format(formatter);
    }

    public LocalDate getMondayOfWeek(LocalDate date) {
        int daysToSubtract = date.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue();
        return date.minusDays(daysToSubtract);
    }

    public LocalDate getSundayOfWeek(LocalDate monday) {
        return monday.plusDays(6);
    }

    public PegawaiProfileDTO getPegawaiProfileDTO(String noHp) {
        List<Pegawai> pegawais = repo.findPegawai(noHp);

        if (pegawais.isEmpty()) {
            return null;
        }

        Pegawai pegawai = pegawais.get(0);

        String jabatan = repo.getJabatanFromId(Integer.parseInt(pegawai.getJabatan())).get(0);
        String alamat = repo.getAlamatFromId(Integer.parseInt(pegawai.getAlamat())).get(0);
        String kelurahan = repo.getKelurahanFromIdAlamat(Integer.parseInt(pegawai.getAlamat())).get(0);
        String kecamatan = repo.getKecamatanFromIdAlamat(Integer.parseInt(pegawai.getAlamat())).get(0);

        return new PegawaiProfileDTO(
                pegawai.getNamapegawai(),
                pegawai.getNomorhp(),
                jabatan,
                alamat,
                kelurahan,
                kecamatan
        );
    }

    public boolean absen(LocalDate currentDate, LocalTime currentTime, String noHp) {
        int attendanceCount = repo.checkAttendance(currentDate, noHp);

        System.out.println(attendanceCount);

        if (attendanceCount >= 1) {
            return false;
        }
        return repo.absen(currentDate, currentTime, noHp);
    }

    public boolean pulang(LocalDate currentDate, LocalTime currentTime, String noHp) {
        int attendanceCount = repo.checkAttendance(currentDate, noHp);
        if (attendanceCount == 0 || attendanceCount > 1) {
            return false;
        }
        double gajiT = repo.getGajiHariIni(noHp, currentDate).doubleValue();
        if (gajiT > 0) {
            return false;
        }

        List<Double> satuanGajiT = repo.getSatuanGajiFromNoHp(noHp);
        double satuanGaji = satuanGajiT.get(0).doubleValue();

        LocalTime waktuHadir = repo.getWaktuKehadiranHariIni(noHp, currentDate);

        double gaji = calculateGajiHarian(waktuHadir, currentTime, satuanGaji);

        return repo.pulang(currentDate, currentTime, noHp, gaji);
    }

    public double calculateGajiHarian(LocalTime waktuHadir, LocalTime waktuPulang, double satuanGaji) {
        long hoursWorked = java.time.Duration.between(waktuHadir, waktuPulang).toHours();
    
        double gajiHarian = hoursWorked * satuanGaji;
    
        return Math.max(gajiHarian, 0.0);
    }
}
