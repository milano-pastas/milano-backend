package com.milano.milanopastas.config;

import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;
import com.zaxxer.hikari.HikariDataSource;

@Component
public class DataSourceShutdown {

    @Autowired
    private DataSource dataSource;

    @PreDestroy
    public void close() {
        if (dataSource instanceof HikariDataSource hikari) {
            System.out.println("🟡 Cerrando conexiones Hikari...");
            hikari.close();
            System.out.println("✅ Conexiones cerradas correctamente.");
        }
    }
}
