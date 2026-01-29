package com.zjtec.travel.service;

public interface EmailService {
    public void sendEmail(String sendTo, String title, String content);
}
