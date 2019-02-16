package com.jiawei.dao;

import com.jiawei.utils.JdbcUtil;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @author : willian fu
 * @date : 2019-02-14.
 * 数据库交互
 */
public class SqlOperat {
    /**
     * 静态加载连接池
     */
    private static JdbcTemplate jdbcTemplate = JdbcUtil.getJdbcTemplate();
    /**
     * 查询Oauth用户数据库密码
     * @param oauthUser Oauth用户
     * @param name 字段名
     * @return password
     */
    public static String queryOauthUser(String oauthUser, String name){
        String sql = "select * from oauth_clients where client_id=?";
        Map<String, Object> oauthUserMap = jdbcTemplate.queryForMap(sql, oauthUser);
        return (String) oauthUserMap.get(name);
    }

    /**
     * 查询最后发布的AccessToken
     * @param oauthUser 用户
     * @return lastAccessToken
     */
    public static String queryLastOauthAccessToken(String oauthUser){
        String sql = "SELECT * FROM oauth_access_tokens WHERE expires=\n" +
                "(SELECT MAX(expires) FROM oauth_access_tokens WHERE client_id=?)";
        Map<String, Object> oauthUserMap = jdbcTemplate.queryForMap(sql, oauthUser);
        return (String) oauthUserMap.get("access_token");
    }
    /**
     * 查询最后发布的RefreshToken
     * @param oauthUser 用户
     * @return RefreshToken
     */
    public static String queryLastOauthRefreshToken(String oauthUser){
        String sql = "SELECT * FROM oauth_refresh_tokens WHERE expires=" +
                "(SELECT MAX(expires) FROM oauth_refresh_tokens WHERE client_id=?)";
        Map<String, Object> oauthUserMap = jdbcTemplate.queryForMap(sql, oauthUser);
        return (String) oauthUserMap.get("refresh_token");
    }

    /**
     * 保存Token
     * @param token token
     * @param type true-Access / false-Refresh
     * @return result
     */
    public static int saveAccessOrRefreshToken(String token, boolean type, String client_id){
        String sqlr = "INSERT INTO oauth_refresh_tokens(refresh_token,client_id,expires) VALUES(?,?,?)";
        String sqla = "INSERT INTO oauth_access_tokens(access_token,client_id,expires) VALUES(?,?,?)";
        int rows = 0;
        Date date = new Date();//获得系统时间.
        String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);//将时间格式转换成符合Timestamp要求的格式.
        Timestamp nowDate =Timestamp.valueOf(nowTime);//把时间转换
        if (type){
            rows = jdbcTemplate.update(sqla, token,client_id,nowDate);
        }else {
            rows = jdbcTemplate.update(sqlr, token,client_id,nowDate);
        }
        return rows;
    }

    /**
     * 保存code授权码
     * @param code AccessCode
     * @param redirect_uri 回调地址
     * @param client_id OauthUsername
     * @return Sql result
     */
    public static int savaAccessCode(String code, String redirect_uri, String client_id){
        String sql = "INSERT INTO oauth_authorization_codes (authorization_code,client_id,redirect_uri,expires) VALUES (?,?,?,?)";
        Date date = new Date();//获得系统时间
        String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);//将时间格式转换成符合Timestamp要求的格式.
        Timestamp nowDate =Timestamp.valueOf(nowTime);//把时间转换
        int rows = jdbcTemplate.update(sql,code,client_id,redirect_uri,nowDate);
        return rows;
    }

    public static String getAccessCode(String code, String client_id){
        String sql = "SELECT * FROM oauth_authorization_codes WHERE expires=" +
                "(SELECT MAX(expires) FROM oauth_authorization_codes WHERE client_id=?)";
        Map<String, Object> oauthUserMap = jdbcTemplate.queryForMap(sql, client_id);
        if (code.equals(oauthUserMap.get("authorization_code"))){
            return oauthUserMap.get("expires").toString();
        }else {
            return null;
        }
    }

    /**
     * yyyy-MM-dd HH:mm:ss 格式转时间戳
     * @param s
     * @return Stamp
     * @throws ParseException
     */
    public static String dateToStamp(String s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = simpleDateFormat.parse(s);
            long ts = date.getTime();
            res = String.valueOf(ts);
            return res;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "0";
    }

    /**
     * 时间戳转时间
     * @param s
     * @return date
     */
    public static String stampToDate(String s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }
}
