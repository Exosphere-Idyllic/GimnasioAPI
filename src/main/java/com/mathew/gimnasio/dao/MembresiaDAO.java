package com.mathew.gimnasio.dao;

import com.mathew.gimnasio.configuracion.ConexionDB;
import com.mathew.gimnasio.modelos.MembresiaAsignacionDTO;

import java.sql.*;
import java.time.LocalDate;

/**
 * DAO para asignación de membresías (RF07).
 * Nota: clientes.id_membresia referencia membresias(id_membresia). Si se envía
 * idTipoMembresia,
 * se asume que coincide con id_membresia (p. ej. mismos IDs en ambas tablas);
 * si no, usar solo idMembresia.
 */
public class MembresiaDAO {

    /**
     * Asigna una membresía a un cliente: registra el pago, actualiza id_membresia y
     * fecha_vencimiento.
     */
    public boolean asignarMembresia(int idUsuario, MembresiaAsignacionDTO dto) {
        Connection conn = null;
        try {
            conn = ConexionDB.getConnection();
            conn.setAutoCommit(false);

            int idCliente = -1;
            try (PreparedStatement psC = conn
                    .prepareStatement("SELECT id_cliente FROM clientes WHERE id_usuario = ?")) {
                psC.setInt(1, idUsuario);
                try (ResultSet rsC = psC.executeQuery()) {
                    if (rsC.next()) {
                        idCliente = rsC.getInt("id_cliente");
                    } else {
                        // The user exists but is not yet in the 'clientes' table (created from Admin
                        // Dashboard without email)
                        // Fetch their details from the 'usuarios' table
                        String nombre = "";
                        String apellido = "";
                        String username = "";
                        try (PreparedStatement psU = conn.prepareStatement(
                                "SELECT nombre, apellido, usuario FROM usuarios WHERE id_usuario = ?")) {
                            psU.setInt(1, idUsuario);
                            try (ResultSet rsU = psU.executeQuery()) {
                                if (rsU.next()) {
                                    nombre = rsU.getString("nombre");
                                    apellido = rsU.getString("apellido");
                                    username = rsU.getString("usuario");
                                } else {
                                    conn.rollback();
                                    return false;
                                }
                            }
                        }

                        // Create the client record
                        try (PreparedStatement psIns = conn.prepareStatement(
                                "INSERT INTO clientes (id_usuario, nombre, apellido, email) VALUES (?, ?, ?, ?) RETURNING id_cliente")) {
                            psIns.setInt(1, idUsuario);
                            psIns.setString(2, nombre != null ? nombre : "Sin Nombre");
                            psIns.setString(3, apellido != null ? apellido : "Sin Apellido");
                            psIns.setString(4, username + "_" + System.currentTimeMillis() + "@gym.local"); // Dummy
                                                                                                            // email
                                                                                                            // since
                                                                                                            // it's NOT
                                                                                                            // NULL and
                                                                                                            // UNIQUE
                            try (ResultSet rsIns = psIns.executeQuery()) {
                                if (rsIns.next()) {
                                    idCliente = rsIns.getInt("id_cliente");
                                } else {
                                    conn.rollback();
                                    return false;
                                }
                            }
                        }
                    }
                }
            }

            int idMembresia = dto.getIdMembresia() != null ? dto.getIdMembresia()
                    : (dto.getIdTipoMembresia() != null ? dto.getIdTipoMembresia() : 0);
            if (idMembresia <= 0) {
                conn.rollback();
                return false;
            }

            int duracionDias = 30; // Default a 30 días para Smart y Black
            double precio = 0;

            PreparedStatement psM = conn
                    .prepareStatement("SELECT precio, duracion_dias FROM membresias WHERE id_membresia = ?");
            psM.setInt(1, idMembresia);
            ResultSet rs = psM.executeQuery();
            if (rs.next()) {
                precio = rs.getDouble("precio");
                // Si tu BD ya tiene duracion_dias en membresias, úsalo:
                // int dur = rs.getInt("duracion_dias");
                // if (!rs.wasNull()) duracionDias = dur;
            } else {
                conn.rollback();
                return false;
            }

            LocalDate vencimiento = LocalDate.now().plusDays(duracionDias);

            try (PreparedStatement psPago = conn.prepareStatement(
                    "INSERT INTO pagos (id_membresia, monto_pagado, metodo_pago, id_cliente) VALUES (?, ?, 'EFECTIVO', ?)")) {
                psPago.setInt(1, idMembresia);
                psPago.setDouble(2, precio);
                psPago.setInt(3, idCliente);
                psPago.executeUpdate();
            }
            PreparedStatement psCli = conn.prepareStatement(
                    "UPDATE clientes SET id_membresia = ?, fecha_vencimiento = ? WHERE id_cliente = ?");
            psCli.setInt(1, idMembresia);
            psCli.setDate(2, Date.valueOf(vencimiento));
            psCli.setInt(3, idCliente);
            if (psCli.executeUpdate() == 0) {
                conn.rollback();
                return false;
            }
            conn.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (conn != null)
                    conn.rollback();
            } catch (SQLException ex) {
            }
            return false;
        } finally {
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
            }
        }
    }

    /**
     * Cancela la membresía de un cliente (elimina id_membresia y
     * fecha_vencimiento).
     */
    public boolean cancelarMembresia(int idUsuario) {
        String sql = "UPDATE clientes SET id_membresia = NULL, fecha_vencimiento = NULL WHERE id_usuario = ?";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
