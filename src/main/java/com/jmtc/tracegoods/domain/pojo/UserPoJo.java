package com.jmtc.tracegoods.domain.pojo;

/**
 * @author Chris
 * @date 2021/6/8 11:56
 * @Email:gang.wu@nexgaming.com
 */
public class UserPoJo {
    private String id;
    private String name;
    private Integer roleId;
    private Integer status;
    private String cTime;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setcTime(String cTime) {
        this.cTime = cTime;
    }

    public String getcTime() {
        return cTime;
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
}
