package com.jiawei.utils;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.*;
import javax.sql.DataSource;
import java.io.FileInputStream;
import java.util.Properties;

/**
 * @author : willian fu
 * @aate   : 2019-01-11
 * function :JDBC工具类库，获取SQL连接对象和连接池对象
 */
public class JdbcUtil {
    /**
    * 静态加载C3P0连接池
    */
    static ComboPooledDataSource cpds = new ComboPooledDataSource();
    /**
    * 返回Spring -- JdbcTemplate (Druid/C3p0)对象
    * @return
    */
    public static JdbcTemplate getJdbcTemplate() {
        return new JdbcTemplate(cpds);
    }

    public static JdbcTemplate getJdbcTemplate(String path) {
        return new JdbcTemplate(getDruidPoor(path));
    }
    /**
     * 获取C3P0连接池
     */
    public static ComboPooledDataSource getC3p0Poor() {
        return cpds;
    }

    /**
     * 从c3p0池中获取数据库连接对象
     */
    public static Connection getC3p0Connection() throws SQLException {
        return cpds.getConnection();
    }

    /**
     *返回德鲁伊连接池对象
     * 参数：数据库的连接配置文件路径
     */
    public static DataSource getDruidPoor(String PropertiesFilePath) {
        try {
            if (null != cpds) {
                /**
                 * 在工具类加载时会创建c3p0连接池对象，如果不使用他，则在此释放掉
                 */
                cpds.close();
            }
            Properties SqlConnectionInfo = new Properties();
            SqlConnectionInfo.load(new FileInputStream(PropertiesFilePath));
            return DruidDataSourceFactory.createDataSource(SqlConnectionInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 从德鲁伊连接池中获取数据库连接对象
     * 参数：数据库的连接配置文件路径
     */
    public static Connection getDruidConnection(String PropertiesFilePath) throws SQLException {
        return getDruidPoor(PropertiesFilePath).getConnection();
    }

    /**
     * 释放资源
     */
    public static void free(Connection conn, Statement psmt, ResultSet reSet) {
        try {
            if (null != conn) {
                conn.close();
            }
            if (null != psmt) {
                psmt.close();
            }
            if (null != reSet) {
                reSet.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
