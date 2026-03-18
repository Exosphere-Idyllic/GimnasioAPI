package com.mathew.gimnasio.controladores;

import com.mathew.gimnasio.dao.CuentaBancariaDAO;
import com.mathew.gimnasio.modelos.CuentaBancaria;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

/**
 * CONTROLADOR DE CUENTAS BANCARIAS
 * Gestiona las cuentas bancarias del gimnasio donde los clientes pueden
 * realizar depósitos o transferencias para activar sus membresías.
 *
 * Endpoints:
 *   GET    /api/cuentas-bancarias          → Listar cuentas activas (público)
 *   GET    /api/cuentas-bancarias/todas    → Listar todas (admin)
 *   POST   /api/cuentas-bancarias          → Crear cuenta (admin)
 *   DELETE /api/cuentas-bancarias/{id}     → Eliminar cuenta (admin)
 */
@Path("/cuentas-bancarias")
public class CuentaBancariaController {

    private CuentaBancariaDAO dao = new CuentaBancariaDAO();

    /**
     * LISTAR CUENTAS ACTIVAS
     * Devuelve solo las cuentas bancarias activas (visibles para clientes).
     * URL: GET /api/cuentas-bancarias
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarActivas() {
        List<CuentaBancaria> cuentas = dao.listarActivas();
        return Response.ok(cuentas).build();
    }

    /**
     * LISTAR TODAS LAS CUENTAS (Admin)
     * Incluye cuentas activas e inactivas para gestión administrativa.
     * URL: GET /api/cuentas-bancarias/todas
     */
    @GET
    @Path("/todas")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarTodas() {
        List<CuentaBancaria> cuentas = dao.listarTodas();
        return Response.ok(cuentas).build();
    }

    /**
     * CREAR CUENTA BANCARIA
     * Registra una nueva cuenta bancaria destino.
     * URL: POST /api/cuentas-bancarias
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response crear(CuentaBancaria c) {
        if (dao.crearCuenta(c)) {
            return Response.ok("{\"mensaje\":\"Cuenta bancaria registrada correctamente\"}").build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("{\"mensaje\":\"Error al registrar la cuenta bancaria\"}").build();
        }
    }

    /**
     * ELIMINAR CUENTA BANCARIA
     * Elimina definitivamente una cuenta bancaria del sistema.
     * URL: DELETE /api/cuentas-bancarias/{id}
     */
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminar(@PathParam("id") int id) {
        if (dao.eliminarDefinitivamente(id)) {
            return Response.ok("{\"mensaje\":\"Cuenta bancaria eliminada\"}").build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("{\"mensaje\":\"Error al eliminar la cuenta bancaria\"}").build();
        }
    }
}
