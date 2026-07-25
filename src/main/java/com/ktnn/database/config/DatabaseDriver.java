package com.ktnn.database.config;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import static org.testng.Assert.fail;

@Slf4j
public abstract class DatabaseDriver {
    public abstract SessionFactory initSessionFactory(String dbURL, String userName, String password);

    SessionFactory buildSessionFactory(Configuration configuration) {
        try {
            if (configuration != null) {
                configuration.setProperty("show_sql", String.valueOf(true));
                configuration.setProperty("hibernate.temp.use_jdbc_metadata_defaults", String.valueOf(false));
                return configuration.buildSessionFactory();
            }
        } catch (Throwable ex) {
            log.error("Unable to connect DB, Please check again!!! {}", ex.getMessage());
            fail("Unable to connect DB, Please check again!!! " + ex.getMessage());
        }
        return null;
    }

}
