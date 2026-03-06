package com.mathew.gimnasio.util;

import org.mindrot.jbcrypt.BCrypt;

public class TestHash {
    public static void main(String[] args) {
        String hash = "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lHuu";
        String pass = "Admin2026";
        boolean match = BCrypt.checkpw(pass, hash);
        System.out.println("Matches: " + match);
    }
}
