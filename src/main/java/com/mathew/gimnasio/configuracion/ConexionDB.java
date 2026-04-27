package com.mathew.gimnasio.configuracion;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Conexión a la base de datos usando HikariCP para pool de conexiones.
 * Configurado por defecto para usar Neon con las credenciales proporcionadas.
 */
public class ConexionDB {

    private static HikariDataSource dataSource;

    static {
        try {
            HikariConfig config = new HikariConfig();
            
            // Usamos las variables de entorno, o los valores de Neon por defecto
            String envUrl = ConfiguracionEnv.get("DB_URL", "jdbc:postgresql://ep-polished-mode-anv9fx0m.c-6.us-east-1.aws.neon.tech/neondb?sslmode=require");
            String envUser = ConfiguracionEnv.get("DB_USER", "neondb_owner");
            String envPass = ConfiguracionEnv.get("DB_PASS", "npg_fKQYtNk2dL3V");
            
            config.setJdbcUrl(buildUrl(envUrl));
            config.setUsername(envUser);
            config.setPassword(envPass);
            config.setDriverClassName("org.postgresql.Driver");
            
            // Configuración recomendada para pool en producción y cloud (Neon)
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(2);
            config.setIdleTimeout(30000); // 30 segundos
            config.setConnectionTimeout(20000); // 20 segundos
            config.setMaxLifetime(1800000); // 30 minutos
            
            dataSource = new HikariDataSource(config);
        } catch (Exception e) {
            System.err.println("Error al inicializar el pool de conexiones: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String buildUrl(String url) {
        if (url == null || url.isBlank()) return url;
        // Si viene como postgresql:// lo adaptamos a jdbc:postgresql://
        if (url.startsWith("postgresql://")) {
            url = url.replaceFirst("postgresql://", "jdbc:postgresql://");
        }
        if (url.contains("ssl")) return url;
        return url + (url.contains("?") ? "&" : "?") + "sslmode=require";
    }

    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("El pool de conexiones no está inicializado.");
        }
        return dataSource.getConnection();
    }
}