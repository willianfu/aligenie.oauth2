package com.jiawei.tmall.oauth2;

import com.alibaba.fastjson.JSON;
import com.jiawei.dao.SqlOperat;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author uthor : willian fu
 * @date : 2019-02-14.
 * Token发放服务
 * errorCode :
 *      1000 code过期
 *      2000 grant_type类型错误
 *      3000 OAuth用户名/密码错误
 */
@WebServlet("/token")
public class GetToken extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        //resp.setContentType("text/html;charset=utf-8");
        System.out.println("hello token!");
        String client_id = req.getParameter("client_id");
        String code = req.getParameter("code");
        String redirect_uri = SqlOperat.queryOauthUser(client_id,"redirect_uri");
        //校验url中OAuth用户名和密码及参数正确性
        if (SqlOperat.queryOauthUser(client_id,"client_secret")
                .equals(req.getParameter("client_secret"))){
                //&& redirect_uri.equals(req.getParameter("redirect_uri"))
            if ("authorization_code".equals(req.getParameter("grant_type"))) {
                //获取客户端code  ,校验code正确有效性
                if (checkAccessCodeOk(code, client_id)) {
                    //发布token
                    String token = getJsonData(client_id);
                    resp.setContentType("application/json;charset=utf-8");
                    System.out.println(token);
                    resp.getWriter().write(token);
               }else {
                    responseError(resp, 1000);
                }
            }else if("refresh_token".equals(req.getParameter("grant_type"))){
                //验证refresh_token正确性
                if (req.getParameter("refresh_token")
                        .equals(SqlOperat.queryLastOauthRefreshToken(client_id))){
                    String token = getJsonData(client_id);
                    resp.setContentType("application/json;charset=utf-8");
                    System.out.println(token);
                    resp.getWriter().write(token);
                }

            }else {responseError(resp, 2000);}
        }else {
            //返回错误响应
           responseError(resp, 3000);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }

    /**
     *
     * @param client_id OAuth用户名
     * @return token json
     */
    private static String getJsonData(String client_id){
        System.out.println("getJsonData-------");
        //刷新access_token
        String access_token = CodeFactory.getToken();
        //刷新refresh_token
        String refresh_token = CodeFactory.getToken();
        //保存token记录
        SqlOperat.saveAccessOrRefreshToken(access_token,true,client_id);
        SqlOperat.saveAccessOrRefreshToken(refresh_token,false,client_id);
        //token过期后进行刷新
        String token = JSON.toJSONString(new TokenBean(access_token, 259200, refresh_token, null));
        return token;
    }

    /**
     * 返回错误响应
     * @param resp s
     */
    private static void responseError(HttpServletResponse resp, int errorCode) throws IOException {
        resp.setContentType("application/json;charset=utf-8");
        System.out.println("error!!");
        //返回错误状态
        resp.getWriter().write("{error\":"+errorCode+",\"error_description\":\"description}");
    }

    /**
     * 校验code是否发放且正确未过期
     * @param code code
     * @param client_id oauth user
     * @return code result?ok
     */
    private static boolean checkAccessCodeOk(String code, String client_id){
        String getCodeDate = SqlOperat.getAccessCode(code,client_id);
        Date date = new Date();//获得系统时间.
        String nowDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        if (getCodeDate  != null){
            long codeTime = Long.parseLong(SqlOperat.dateToStamp(getCodeDate));
            long nowTime = Long.parseLong(SqlOperat.dateToStamp(nowDate));
            System.out.println(codeTime+"--"+nowTime+"="+(nowTime-codeTime));
            if (codeTime != 0 && (nowTime - codeTime)/1000 <= 60*2){
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }
    }
}
