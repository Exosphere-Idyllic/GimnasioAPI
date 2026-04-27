package com.mathew.gimnasio.util;

public class CedulaTest {
    public static void main(String[] args) {
        String cedula = "0150212983";
        // Verifica longitud y que solo contenga números
        if (cedula == null || !cedula.matches("\\d{10}")) {
            System.out.println("Falla longitud/digitos");
            return;
        }
        int provincia = Integer.parseInt(cedula.substring(0, 2));
        if (provincia < 1 || provincia > 24) {
            System.out.println("Falla provincia");
            return;
        }
        int tercerDigito = Integer.parseInt(cedula.substring(2, 3));
        if (tercerDigito >= 6) {
            System.out.println("Falla 3er digito");
            return;
        }
        int[] coeficientes = { 2, 1, 2, 1, 2, 1, 2, 1, 2 };
        int suma = 0;
        for (int i = 0; i < 9; i++) {
            int valor = Character.getNumericValue(cedula.charAt(i)) * coeficientes[i];
            if (valor >= 10)
                valor -= 9;
            suma += valor;
        }
        int digitoVerificador = Character.getNumericValue(cedula.charAt(9));
        int decenaSuperior = ((suma + 9) / 10) * 10;
        int resultado = decenaSuperior - suma;
        if (resultado == 10)
            resultado = 0;
        System.out.println("Suma: " + suma + " Decena: " + decenaSuperior + " Result: " + resultado + " Verif: "
                + digitoVerificador);
        System.out.println("Valida: " + (resultado == digitoVerificador));
    }
}
