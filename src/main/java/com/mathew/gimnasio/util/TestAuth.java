package com.mathew.gimnasio.util;

import java.util.regex.Pattern;
import java.time.LocalDate;
import java.time.Period;

public class TestAuth {
    public static void main(String[] args) {
        String nombre = "Exosphere";
        String apellido = "Lawrence";
        String cedula = "0150212983";
        String telefono = "0957196004";
        String email = "dcssjg44@gmail.com";
        String fechaNacimiento = "2000-10-06";
        String usuario = "Exosphere";
        String contrasena = "Lawrence1234";

        if (nombre == null || nombre.trim().length() < 3 || !Pattern.matches(
                "^[a-zA-Z\\u00E1\\u00E9\\u00ED\\u00F3\\u00FA\\u00C1\\u00C9\\u00CD\\u00D3\\u00DA\\u00F1\\u00D1 ]+$",
                nombre)) {
            System.out.println("Falla nombre");
            return;
        }
        if (apellido == null || apellido.trim().length() < 3 || !Pattern.matches(
                "^[a-zA-Z\\u00E1\\u00E9\\u00ED\\u00F3\\u00FA\\u00C1\\u00C9\\u00CD\\u00D3\\u00DA\\u00F1\\u00D1 ]+$",
                apellido)) {
            System.out.println("Falla apellido");
            return;
        }
        if (telefono == null || !telefono.matches("\\d{9,}")) {
            System.out.println("Falla telefono");
            return;
        }
        if (contrasena == null || contrasena.length() < 8) {
            System.out.println("Falla contrasena");
            return;
        }
        if (email == null || !Pattern.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", email)) {
            System.out.println("Falla email");
            return;
        }
        try {
            LocalDate fechaNac = LocalDate.parse(fechaNacimiento);
            LocalDate ahora = LocalDate.now();
            if (fechaNac.isAfter(ahora)) {
                System.out.println("Falla futura");
                return;
            }
            int edad = Period.between(fechaNac, ahora).getYears();
            if (edad < 12) {
                System.out.println("Falla edad");
                return;
            }
        } catch (Exception e) {
            System.out.println("Falla formato fecha");
            return;
        }
        if (usuario == null || usuario.length() < 4) {
            System.out.println("Falla usuario len");
            return;
        }
        if (usuario.contains(" ")) {
            System.out.println("Falla usuario espacio");
            return;
        }

        System.out.println("TODO OK");
    }
}
