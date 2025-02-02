package com.xzavier0722.mc.plugin.slimefun4.storage.patch;

import java.sql.SQLException;
import java.sql.Statement;

import com.xzavier0722.mc.plugin.slimefun4.storage.adapter.sqlcommon.ISqlCommonConfig;

import lombok.Getter;

@Getter
public abstract class DatabasePatch {
    protected int version;

    public abstract void patch(Statement stmt, ISqlCommonConfig config) throws SQLException;
}
