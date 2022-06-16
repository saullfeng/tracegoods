package com.jmtc.tracegoods.domain;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.jmtc.tracegoods.domain.pojo.UserPoJo;
import com.jmtc.tracegoods.utils.DateUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Chris
 * @date 2021/6/8 11:54
 * @Email:gang.wu@nexgaming.com
 */
public class User extends Entitys implements Serializable {
    private String id;
    private String name;
    private String password;
    private Integer roleId;
    private Integer status;
    private Date ctime;
    private String firstname;
    private String lastname;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }

    public Date getCtime() {
        return ctime;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getToken() {
        String token="";
        token= JWT.create().withAudience(this.getName())
                .sign(Algorithm.HMAC256(this.getPassword()));
        return token;
    }

    public UserPoJo toPoJo() {
        UserPoJo userPoJo = new UserPoJo();
        userPoJo.setId(this.id);
        userPoJo.setName(this.name);
        userPoJo.setRoleId(this.roleId);
        userPoJo.setStatus(this.status);
        userPoJo.setcTime(DateUtils.getStringDateLong(this.ctime));
        userPoJo.setFirstname(this.firstname);
        userPoJo.setLastname(this.lastname);
        return userPoJo;
    }
}