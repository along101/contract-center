package com.along101.contract.entity;

import java.util.Date;

public class AuditLogEntity {
    private Long id;

    private String classMethod;

    private String classMethodArgs;

    private String classMethodReturn;

    private String clientIp;

    private String httpMethod;

    private String httpUri;

    private Date insertTime;

    private Boolean isActive;

    private Date updateTime;

    private String userName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClassMethod() {
        return classMethod;
    }

    public void setClassMethod(String classMethod) {
        this.classMethod = classMethod == null ? null : classMethod.trim();
    }

    public String getClassMethodArgs() {
        return classMethodArgs;
    }

    public void setClassMethodArgs(String classMethodArgs) {
        this.classMethodArgs = classMethodArgs == null ? null : classMethodArgs.trim();
    }

    public String getClassMethodReturn() {
        return classMethodReturn;
    }

    public void setClassMethodReturn(String classMethodReturn) {
        this.classMethodReturn = classMethodReturn == null ? null : classMethodReturn.trim();
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp == null ? null : clientIp.trim();
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod == null ? null : httpMethod.trim();
    }

    public String getHttpUri() {
        return httpUri;
    }

    public void setHttpUri(String httpUri) {
        this.httpUri = httpUri == null ? null : httpUri.trim();
    }

    public Date getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }
}