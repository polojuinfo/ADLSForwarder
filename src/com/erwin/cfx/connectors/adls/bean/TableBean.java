/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.erwin.cfx.connectors.adls.bean;

import com.ads.api.beans.common.AuditHistory;
import java.util.List;

/**
 *
 * @author DBasheer
 */
public class TableBean implements java.io.Serializable{
    private String physicalTableName;
    private String schemaName;
    private String tableName;
    private String logicalTableName;
    private  AuditBean auditHistory;
    private String dataStewardName;
    private String sdiClassificationName;
    private String systemEnvironmentName;
    private List<KeyValueBean> extendedProperties;
    private boolean  sdiFlag;
    private String tableComments;
    private Integer environmentId;
    private Integer systemId;
    private String tableDefinition;
    private String sdiDescription;
    private Integer tableId;

    public String getPhysicalTableName() {
        return physicalTableName;
    }

    public void setPhysicalTableName(String physicalTableName) {
        this.physicalTableName = physicalTableName;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getLogicalTableName() {
        return logicalTableName;
    }

    public void setLogicalTableName(String logicalTableName) {
        this.logicalTableName = logicalTableName;
    }

    public AuditBean getAuditHistory() {
        return auditHistory;
    }

    public void setAuditHistory(AuditBean auditHistory) {
        this.auditHistory = auditHistory;
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

    public String getSystemEnvironmentName() {
        return systemEnvironmentName;
    }

    public void setSystemEnvironmentName(String systemEnvironmentName) {
        this.systemEnvironmentName = systemEnvironmentName;
    }

    public boolean getSdiFlag() {
        return sdiFlag;
    }

    public void setSdiFlag(boolean sdiFlag) {
        this.sdiFlag = sdiFlag;
    }

    public String getTableComments() {
        return tableComments;
    }

    public void setTableComments(String tableComments) {
        this.tableComments = tableComments;
    }

    public Integer getEnvironmentId() {
        return environmentId;
    }

    public void setEnvironmentId(Integer environmentId) {
        this.environmentId = environmentId;
    }

    public Integer getSystemId() {
        return systemId;
    }

    public void setSystemId(Integer systemId) {
        this.systemId = systemId;
    }

    public String getTableDefinition() {
        return tableDefinition;
    }

    public void setTableDefinition(String tableDefinition) {
        this.tableDefinition = tableDefinition;
    }

    public String getSdiDescription() {
        return sdiDescription;
    }

    public void setSdiDescription(String sdiDescription) {
        this.sdiDescription = sdiDescription;
    }

    public Integer getTableId() {
        return tableId;
    }

    public void setTableId(Integer tableId) {
        this.tableId = tableId;
    }

    public List<KeyValueBean> getExtendedProperties() {
        return extendedProperties;
    }

    public void setExtendedProperties(List<KeyValueBean> extendedProperties) {
        this.extendedProperties = extendedProperties;
    }

   
    
}
