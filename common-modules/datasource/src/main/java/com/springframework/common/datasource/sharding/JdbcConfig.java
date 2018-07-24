package com.springframework.common.datasource.sharding;

import lombok.Data;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceInitializationMode;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;

import javax.sql.DataSource;
import java.nio.charset.Charset;
import java.util.List;

/**
 * @author summer
 * 2018/7/23
 */
public class JdbcConfig {
    public static final String PREFIX = "sharding.jdbc.";
    private String names;

    @Data
    @ConfigurationProperties(DatasourceProperties.PREFIX)
    public class DatasourceProperties {
        public static final String PREFIX = "sharding.jdbc.datasource.";
        /**
         * Name of the datasource. Default to "testdb" when using an embedded database.
         */
        private String name;

        /**
         * Whether to generate a random datasource name.
         */
        private boolean generateUniqueName;

        /**
         * Fully qualified name of the connection pool implementation to use. By default, it
         * is auto-detected from the classpath.
         */
//        private Class<? extends DataSource> type;
        private String type;

        /**
         * Fully qualified name of the JDBC driver. Auto-detected based on the URL by default.
         */
        private String driverClassName;

        /**
         * JDBC URL of the database.
         */
        private String url;

        /**
         * Login username of the database.
         */
        private String username;

        /**
         * Login password of the database.
         */
        private String password;

        /**
         * JNDI location of the datasource. Class, url, username & password are ignored when
         * set.
         */
//        private String jndiName;
//
//        /**
//         * Initialize the datasource with available DDL and DML scripts.
//         */
//        private DataSourceInitializationMode initializationMode = DataSourceInitializationMode.EMBEDDED;
//
//        /**
//         * Platform to use in the DDL or DML scripts (such as schema-${platform}.sql or
//         * data-${platform}.sql).
//         */
//        private String platform = "all";
//
//        /**
//         * Schema (DDL) script resource references.
//         */
//        private List<String> schema;
//
//        /**
//         * Username of the database to execute DDL scripts (if different).
//         */
//        private String schemaUsername;
//
//        /**
//         * Password of the database to execute DDL scripts (if different).
//         */
//        private String schemaPassword;
//
//        /**
//         * Data (DML) script resource references.
//         */
//        private List<String> data;
//
//        /**
//         * Username of the database to execute DML scripts (if different).
//         */
//        private String dataUsername;
//
//        /**
//         * Password of the database to execute DML scripts (if different).
//         */
//        private String dataPassword;
//
//        /**
//         * Whether to stop if an error occurs while initializing the database.
//         */
//        private boolean continueOnError = false;
//
//        /**
//         * Statement separator in SQL initialization scripts.
//         */
//        private String separator = ";";
//
//        /**
//         * SQL scripts encoding.
//         */
//        private Charset sqlScriptEncoding;
//
//        private EmbeddedDatabaseConnection embeddedDatabaseConnection = EmbeddedDatabaseConnection.NONE;
//
//        private DataSourceProperties.Xa xa = new DataSourceProperties.Xa();
//
//        private String uniqueName;


    }

    @Data
    public class ConfigProperties {
        public static final String PREFIX = "sharding.jdbc.config.sharding.props";
        private String sqlShow;
        private String executorSize;

    }
}
