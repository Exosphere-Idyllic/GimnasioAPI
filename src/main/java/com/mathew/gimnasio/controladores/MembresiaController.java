package com.mathew.gimnasio.controladores;

import com.mathew.gimnasio.dao.MembresiaDAO;
import com.mathew.gimnasio.modelos.MembresiaAsignacionDTO;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Controlador de membresías (RF07)
 */
@Path("/membresias")
public class MembresiaController {

    private MembresiaDAO dao = new MembresiaDAO();

    /**
     * POST /membresias/{idUsuario} - Asignar membresía a un cliente.
     * Body: { "idMembresia": 1 } o { "idTipoMembresia": 1 } (opcional:
     * "duracionDias": 30)
     */
    @POST
    @Path("/{idUsuario}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response asignarMembresia(@PathParam("idUsuario") int idUsuario, MembresiaAsignacionDTO dto) {
        if (dto == null || (dto.getIdMembresia() == null && dto.getIdTipoMembresia() == null)) {
            return Response.status(400).entity("{\"mensaje\":\"Debe indicar idMembresia o idTipoMembresia\"}").build();
        }
        boolean ok = dao.asignarMembresia(idUsuario, dto);
        if (ok)
            return Response.ok("{\"mensaje\":\"Membresía asignada correctamente\"}").build();
        return Response.status(400).entity("{\"mensaje\":\"Error al asignar membresía\"}").build();
    }

    /**
     * DELETE /membresias/{idUsuario} - Cancelar (quitar) membresía actual.
     */
    @DELETE
    @Path("/{idUsuario}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response cancelarMembresia(@PathParam("idUsuario") int idUsuario) {
        if (dao.cancelarMembresia(idUsuario)) {
            return Response.ok("{\"mensaje\":\"Membresía cancelada correctamente\"}").build();
        }
        return Response.status(400).entity("{\"mensaje\":\"Error al cancelar membresía\"}").build();
    }
}
