package com.mathew.gimnasio.dao;

import com.mathew.gimnasio.configuracion.ConexionDB;
import com.mathew.gimnasio.modelos.CuentaBancaria;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO DE CUENTAS BANCARIAS
 * Gestiona las cuentas bancarias del gimnasio que se muestran a los clientes
 * como opciones de destino para depósitos y transferencias.
 * Solo el administrador puede crear y eliminar cuentas.
 */
public class CuentaBancariaDAO {

    /**
     * LISTAR CUENTAS BANCARIAS ACTIVAS
     * Devuelve todas las cuentas bancarias disponibles para que los clientes
     * puedan seleccionar a cuál depositar o transferir.
     */
    public List<CuentaBancaria> listarActivas() {
        List<CuentaBancaria> lista = new ArrayList<>();
        String sql = "SELECT id_cuenta, nombre_banco, numero_cuenta, tipo_cuenta, titular, activa "
                   + "FROM cuentas_bancarias WHERE activa = true ORDER BY nombre_banco ASC";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                CuentaBancaria c = new CuentaBancaria();
                c.setIdCuenta(rs.getInt("id_cuenta"));
                c.setNombreBanco(rs.getString("nombre_banco"));
                c.setNumeroCuenta(rs.getString("numero_cuenta"));
                c.setTipoCuenta(rs.getString("tipo_cuenta"));
                c.setTitular(rs.getString("titular"));
                c.setActiva(rs.getBoolean("activa"));
                lista.add(c);
            }
        } catch (Exception e) { e.printStackTrace(); }

        return lista;
    }

    /**
     * LISTAR TODAS LAS CUENTAS BANCARIAS (Admin)
     * Incluye activas e inactivas para la gestión administrativa.
     */
    public List<CuentaBancaria> listarTodas() {
        List<CuentaBancaria> lista = new ArrayList<>();
        String sql = "SELECT id_cuenta, nombre_banco, numero_cuenta, tipo_cuenta, titular, activa "
                   + "FROM cuentas_bancarias ORDER BY nombre_banco ASC";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                CuentaBancaria c = new CuentaBancaria();
                c.setIdCuenta(rs.getInt("id_cuenta"));
                c.setNombreBanco(rs.getString("nombre_banco"));
                c.setNumeroCuenta(rs.getString("numero_cuenta"));
                c.setTipoCuenta(rs.getString("tipo_cuenta"));
                c.setTitular(rs.getString("titular"));
                c.setActiva(rs.getBoolean("activa"));
                lista.add(c);
            }
        } catch (Exception e) { e.printStackTrace(); }

        return lista;
    }

    /**
     * CREAR CUENTA BANCARIA
     * Registra una nueva cuenta bancaria destino para el gimnasio.
     */
    public boolean crearCuenta(CuentaBancaria c) {
        String sql = "INSERT INTO cuentas_bancarias (nombre_banco, numero_cuenta, tipo_cuenta, titular) "
                   + "VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getNombreBanco());
            ps.setString(2, c.getNumeroCuenta());
            ps.setString(3, c.getTipoCuenta());
            ps.setString(4, c.getTitular());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * ELIMINAR (DESACTIVAR) CUENTA BANCARIA
     * En lugar de borrar el registro, lo desactiva para mantener el historial.
     */
    public boolean eliminarCuenta(int idCuenta) {
        String sql = "UPDATE cuentas_bancarias SET activa = false WHERE id_cuenta = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idCuenta);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * ELIMINAR CUENTA BANCARIA DEFINITIVAMENTE
     * Borra el registro por completo de la base de datos.
     */
    public boolean eliminarDefinitivamente(int idCuenta) {
        String sql = "DELETE FROM cuentas_bancarias WHERE id_cuenta = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idCuenta);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
