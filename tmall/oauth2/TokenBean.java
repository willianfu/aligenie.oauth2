package com.jiawei.tmall.oauth2;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author : willian fu
 * @date : 2019-02-14
 */
public class TokenBean {
    //token值
    String access_token;
    //有效时间
    Integer expires_in;
    //新token
    String refresh_token;
    //权限范围
    @JSONField(serialize=false)
    String scope;

    public TokenBean(String access_token, Integer expires_in, String refresh_token, String scope) {
        this.access_token = access_token;
        this.expires_in = expires_in;
        this.refresh_token = refresh_token;
        this.scope = scope;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public Integer getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(Integer expires_in) {
        this.expires_in = expires_in;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
