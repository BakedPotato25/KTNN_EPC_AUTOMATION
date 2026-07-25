package com.ktnn.database.config;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DatabaseManager {
    static Map<String, ThreadLocal<SessionFactory>> databaseManagerMap = new HashMap<>();


    /**
     * Connect to the database using the database information
     *
     * @param databaseInfos : List of database information {@link DatabaseInfo}
     */
    public static void connectDatabases(List<DatabaseInfo> databaseInfos) {
        databaseInfos.forEach(databaseInfo -> {
            DatabaseFactory.initDatabaseConnection(databaseInfo.getType(), databaseInfo.getName(), databaseInfo.getUserName(), databaseInfo.getPassword(), databaseInfo.getUrl());
        });
    }


    public static SessionFactory getSessionFactory(String dbName) {
        ThreadLocal<SessionFactory> threadLocalDriver = databaseManagerMap.get(dbName.trim().toUpperCase());
        return threadLocalDriver.get();
    }

    public static void setSessionFactory(SessionFactory session, String dbName) {
        if (Objects.nonNull(session)) {
            ThreadLocal<SessionFactory> threadLocalDriver = new ThreadLocal<>();
            threadLocalDriver.set(session);
            databaseManagerMap.put(dbName.trim().toUpperCase(), threadLocalDriver);
        }
    }

    public static void shutdown(String dbName) {
        DatabaseManager.getSessionFactory(dbName).close();
    }

    public static Session openDBSession(String dbName) {
        Session session = DatabaseManager.getSessionFactory(dbName).openSession();
        session.clear();
        return session;
    }

    public static void closeDBSession(Session session) {
        if (Objects.nonNull(session)) session.close();
    }
}
