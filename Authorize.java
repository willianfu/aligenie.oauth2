package com.jiawei.tmall.oauth2;

import com.jiawei.dao.SqlOperat;
import com.jiawei.utils.UrlUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * @author : willian fu
 * @date : 2019-02-14.
 * OAUTH授权服务
 */
@WebServlet("/authorize")
public class Authorize extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("text/html;charset=utf-8");
        System.out.println(req.getRequestURI());
        HttpSession session = req.getSession();

        //如果点击了确认授权
        if ("yes".equals(req.getParameter("authorize"))){
            //确认了授权,获取第三方接收code的重定向地址
            String redirect_uri = (String) session.getAttribute("redirect_uri");
            //System.out.println(redirect_uri);
            String state = (String) session.getAttribute("state");
            //生成code授权码
            String code = CodeFactory.getCode();
            String client_id = (String) session.getAttribute("client_id");
            //发布访问令牌，解码url并且将code加在url后面返回
            redirect_uri = UrlUtil.getURLDecoderString(redirect_uri)+"&code="+code+"&state="+state;
            System.out.println(redirect_uri);
            //保存code到数据库
            SqlOperat.savaAccessCode(code, redirect_uri, client_id);
            resp.setStatus(302);
            resp.setHeader("location", redirect_uri);
        }else if ("no".equals(req.getParameter("authorize"))){
            resp.setHeader("refresh","2;url=/authorize");
            resp.getWriter().write("<h2 style='text-align:center'>授权取消，请重新授权！</h2>");
        }else {
            //缓存重定向redirect_url地址到session
            session.setAttribute("redirect_uri",req.getParameter("redirect_uri"));
            session.setAttribute("state",req.getParameter("state"));
            session.setAttribute("client_id",req.getParameter("client_id"));
            //如果是授权请求，则展示授权页面
            System.out.println("redirect_uri---"+session.getAttribute("redirect_uri"));
            req.getRequestDispatcher("oauthShow.html").forward(req,resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
