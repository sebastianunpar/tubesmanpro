package com.example.tubesmanpro.pegawai;

import java.util.Random;

public class Otp {
    private String otp;

    Otp() {
        generateOtp();
    }

    public void generateOtp() {
        Random random = new Random();
        this.otp = String.format("%06d", random.nextInt(999999));
    }

    public String getOtp() {
        return this.otp;
    }
}
