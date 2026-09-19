package br.com.fiap.hoteldurmabem.repository;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public final class DatabaseConfig {

    private static final String PROPERTIES_FILE = "/application.properties";

    private final String url;
    private final String user;
    private final String password;

    private static DatabaseConfig instance;

    private DatabaseConfig(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public static synchronized DatabaseConfig getInstance() {
        if (instance == null) {
            Properties props = loadProperties();
            String url = firstNonBlank(
                    System.getenv("DB_URL"),
                    props.getProperty("db.url", "jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL"));
            String user = firstNonBlank(
                    System.getenv("DB_USER"),
                    props.getProperty("db.user", "RM572019"));
            String password = firstNonBlank(
                    System.getenv("DB_PASSWORD"),
                    props.getProperty("db.password", "070108"));
            instance = new DatabaseConfig(url, user, password);
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }


    public boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Falha ao conectar no Oracle: " + e.getMessage());
            System.err.println("URL=" + url + " USER=" + user);
            return false;
        }
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    private static Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream in = DatabaseConfig.class.getResourceAsStream(PROPERTIES_FILE)) {
            if (in != null) {
                props.load(in);
            } else {
                System.out.println("Aviso: " + PROPERTIES_FILE + " nao encontrado no classpath. Usando defaults/env.");
            }
        } catch (IOException e) {
            System.out.println("Aviso: nao foi possivel ler " + PROPERTIES_FILE + ": " + e.getMessage());
        }
        return props;
    }

    private static String firstNonBlank(String... values) {
        for (String v : values) {
            if (v != null && !v.isBlank()) {
                return v;
            }
        }
        return "";
    }
}
