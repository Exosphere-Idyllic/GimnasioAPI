package com.mathew.gimnasio.modelos;

import java.io.Serializable;

/**
 * MODELO DE CUENTA BANCARIA
 * Representa una cuenta bancaria del gimnasio que los clientes pueden usar
 * como destino para depósitos o transferencias.
 */
public class CuentaBancaria implements Serializable {
    private int idCuenta;
    private String nombreBanco;
    private String numeroCuenta;
    private String tipoCuenta;
    private String titular;
    private boolean activa;

    public CuentaBancaria() {}

    public int getIdCuenta() { return idCuenta; }
    public void setIdCuenta(int idCuenta) { this.idCuenta = idCuenta; }

    public String getNombreBanco() { return nombreBanco; }
    public void setNombreBanco(String nombreBanco) { this.nombreBanco = nombreBanco; }

    public String getNumeroCuenta() { return numeroCuenta; }
    public void setNumeroCuenta(String numeroCuenta) { this.numeroCuenta = numeroCuenta; }

    public String getTipoCuenta() { return tipoCuenta; }
    public void setTipoCuenta(String tipoCuenta) { this.tipoCuenta = tipoCuenta; }

    public String getTitular() { return titular; }
    public void setTitular(String titular) { this.titular = titular; }

    public boolean isActiva() { return activa; }
    public void setActiva(boolean activa) { this.activa = activa; }
}
