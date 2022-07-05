/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.erwin.cfx.connectors.adls.bean;

import com.ads.api.beans.common.AuditHistory;
import com.ads.api.beans.kv.KeyValue;
import com.ads.api.beans.sm.SMEnvironment;
import java.util.Date;
import java.util.List;

/**
 *
 * @author SMastanV
 */
public class EnvironmentBean {
    String systemEnvironmentType;
    String systemName = "";
    private int systemId;
    private String systemEnvironmentName = "";    
    private int environmentId;
    private SMEnvironment.DatabaseType environmentType = SMEnvironment.DatabaseType.None;
    private String environmentNotes = "";
    private List<KeyValueBean> extendedProperties;
    private AuditHistory auditHistory;
    private Date lastLoadingTime;
    String databaseName;
    private String databaseType = "";
    private String databaseDriver = "";
    private String databaseIPAddress = "";
    private String databasePort = "";
    private String databaseURL = "";
    private String databaseInstanceSchema = "";
    private String dataStewardName = "";
    private String sdiClassificationName = "";
    private boolean sdiFlag;
    private String sdiDescription = "";
    private String status = "";

    public String getSystemEnvironmentType() {
        return systemEnvironmentType;
    }

    public void setSystemEnvironmentType(String systemEnvironmentType) {
        this.systemEnvironmentType = systemEnvironmentType;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public int getSystemId() {
        return systemId;
    }

    public void setSystemId(int systemId) {
        this.systemId = systemId;
    }

    public String getSystemEnvironmentName() {
        return systemEnvironmentName;
    }

    public void setSystemEnvironmentName(String systemEnvironmentName) {
        this.systemEnvironmentName = systemEnvironmentName;
    }

    public int getEnvironmentId() {
        return environmentId;
    }

    public void setEnvironmentId(int environmentId) {
        this.environmentId = environmentId;
    }

    public SMEnvironment.DatabaseType getEnvironmentType() {
        return environmentType;
    }

    public void setEnvironmentType(SMEnvironment.DatabaseType environmentType) {
        this.environmentType = environmentType;
    }

    public String getEnvironmentNotes() {
        return environmentNotes;
    }

    public void setEnvironmentNotes(String environmentNotes) {
        this.environmentNotes = environmentNotes;
    }

    public AuditHistory getAuditHistory() {
        return auditHistory;
    }

    public void setAuditHistory(AuditHistory auditHistory) {
        this.auditHistory = auditHistory;
    }

    public Date getLastLoadingTime() {
        return lastLoadingTime;
    }

    public void setLastLoadingTime(Date lastLoadingTime) {
        this.lastLoadingTime = lastLoadingTime;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getDatabaseType() {
        return databaseType;
    }

    public void setDatabaseType(String databaseType) {
        this.databaseType = databaseType;
    }

    public String getDatabaseDriver() {
        return databaseDriver;
    }

    public void setDatabaseDriver(String databaseDriver) {
        this.databaseDriver = databaseDriver;
    }

    public String getDatabaseIPAddress() {
        return databaseIPAddress;
    }

    public void setDatabaseIPAddress(String databaseIPAddress) {
        this.databaseIPAddress = databaseIPAddress;
    }

    public String getDatabasePort() {
        return databasePort;
    }

    public void setDatabasePort(String databasePort) {
        this.databasePort = databasePort;
    }

    public String getDatabaseURL() {
        return databaseURL;
    }

    public void setDatabaseURL(String databaseURL) {
        this.databaseURL = databaseURL;
    }

    public String getDatabaseInstanceSchema() {
        return databaseInstanceSchema;
    }

    public void setDatabaseInstanceSchema(String databaseInstanceSchema) {
        this.databaseInstanceSchema = databaseInstanceSchema;
    }

    public String getDataStewardName() {
        return dataStewardName;
    }

    public void setDataStewardName(String dataStewardName) {
        this.dataStewardName = dataStewardName;
    }

    public String getSdiClassificationName() {
        return sdiClassificationName;
    }

    public void setSdiClassificationName(String sdiClassificationName) {
        this.sdiClassificationName = sdiClassificationName;
    }

    public boolean isSdiFlag() {
        return sdiFlag;
    }

    public void setSdiFlag(boolean sdiFlag) {
        this.sdiFlag = sdiFlag;
    }

    public String getSdiDescription() {
        return sdiDescription;
    }

    public void setSdiDescription(String sdiDescription) {
        this.sdiDescription = sdiDescription;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<KeyValueBean> getExtendedProperties() {
        return extendedProperties;
    }

    public void setExtendedProperties(List<KeyValueBean> extendedProperties) {
        this.extendedProperties = extendedProperties;
    }
 
}
