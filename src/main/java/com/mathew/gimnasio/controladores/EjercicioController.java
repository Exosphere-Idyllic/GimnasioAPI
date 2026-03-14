package com.mathew.gimnasio.controladores;

import com.mathew.gimnasio.dao.EjercicioDAO;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Map;

/**
 * Controlador de ejercicios (RF05) - Catálogo
 */
@Path("/rutinas")
public class EjercicioController {

    private EjercicioDAO dao = new EjercicioDAO();

    @GET
    @Path("/ejercicios")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCatalogoEjercicios() {
        String json = dao.listarEjerciciosJSON();
        return Response.ok(json).build();
    }

    @POST
    @Path("/ejercicios")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response crearEjercicio(Map<String, String> payload) {
        try {
            String nombre = payload.get("nombre");
            String grupoMuscular = payload.get("grupoMuscular");

            if (nombre == null || nombre.trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"mensaje\":\"El nombre del ejercicio es obligatorio\"}")
                        .build();
            }

            boolean exito = dao.crearEjercicio(nombre, grupoMuscular);
            if (exito) {
                return Response.ok("{\"mensaje\":\"Ejercicio creado correctamente\"}").build();
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("{\"mensaje\":\"Error al guardar el ejercicio en la BD\"}")
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"mensaje\":\"Excepción de servidor al crear ejercicio\"}")
                    .build();
        }
    }

    @PUT
    @Path("/ejercicios/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizarEjercicio(@PathParam("id") int id, Map<String, String> payload) {
        try {
            String nombre = payload.get("nombre");
            String grupoMuscular = payload.get("grupoMuscular");

            if (nombre == null || nombre.trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"mensaje\":\"El nombre del ejercicio es obligatorio\"}")
                        .build();
            }

            boolean exito = dao.actualizarEjercicio(id, nombre, grupoMuscular);
            if (exito) {
                return Response.ok("{\"mensaje\":\"Ejercicio actualizado correctamente\"}").build();
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("{\"mensaje\":\"No se pudo actualizar o el ejercicio no existe\"}")
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"mensaje\":\"Excepción de servidor al actualizar ejercicio\"}")
                    .build();
        }
    }

    @DELETE
    @Path("/ejercicios/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarEjercicio(@PathParam("id") int id) {
        boolean exito = dao.eliminarEjercicio(id);
        if (exito) {
            return Response.ok("{\"mensaje\":\"Ejercicio eliminado exitosamente\"}").build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"mensaje\":\"No se pudo eliminar. Puede estar en uso por rutinas activas o no existe.\"}")
                    .build();
        }
    }
}
