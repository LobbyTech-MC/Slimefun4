package com.xzavier0722.mc.plugin.slimefun4.storage.adapter.sqlite;

import com.xzavier0722.mc.plugin.slimefun4.storage.adapter.sqlcommon.ISqlCommonConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import city.norain.slimefun4.SlimefunExtended;

public record SqliteConfig(String path, int maxConnection) implements ISqlCommonConfig {
    public HikariDataSource createDataSource() {
        var config = new HikariConfig();
        config.setDriverClassName(driver());
        config.setJdbcUrl(jdbcUrl());
        config.setPoolName("SlimefunHikariPool");
        config.setMaximumPoolSize(maxConnection);

        /* ===================
         * DATABASE DEBUG MODE
         * ===================
         */
        if (SlimefunExtended.isDatabaseDebugMode()) {
            config.setLeakDetectionThreshold(5000);
        }

        return new HikariDataSource(config);
    }

    public String jdbcUrl() {
        return "jdbc:sqlite:"
                + path
                + "?foreign_keys=on"
                + "&journal_mode=WAL"
                + "&synchronous=NORMAL"
                + "&locking_mode=NORMAL";
    }

    public String driver() {
        return "org.sqlite.JDBC";
    }
}
