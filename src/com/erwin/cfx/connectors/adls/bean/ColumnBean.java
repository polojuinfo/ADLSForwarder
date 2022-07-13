/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.erwin.cfx.connectors.adls.bean;

import com.ads.api.beans.common.AuditHistory;
import com.ads.api.beans.csm.CodeValue;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author DBasheer
 */
public class ColumnBean implements java.io.Serializable {

    private String columnLength;
    private String logicalColumnName;
    private String columnDefinition;
    private String foreignKeyTableName;
    private List<CodeValue> validValues = new CopyOnWriteArrayList();
    private String tableName;
    private String columnScale;
    private String columnPrecision;
    private String columnDatatype;
    private String sdidescription;
    private boolean sdiflag;             
    private String physicalColumnName;
    private AuditBean  auditHistory;         
    private String dataStewardName;
    private List<KeyValueBean> extendedProperties;
    private String columnComments;
    private Integer columnId;
    private boolean  columnNullableFlag;
    private String foreignKeyColumnName;
    private Integer tableId;
   private boolean primaryKeyFlag;        
    private String columnName;

    public String getColumnLength() {
        return columnLength;
    }

    public void setColumnLength(String columnLength) {
        this.columnLength = columnLength;
    }

    public String getLogicalColumnName() {
        return logicalColumnName;
    }

    public void setLogicalColumnName(String logicalColumnName) {
        this.logicalColumnName = logicalColumnName;
    }

    public String getColumnDefinition() {
        return columnDefinition;
    }

    public void setColumnDefinition(String columnDefinition) {
        this.columnDefinition = columnDefinition;
    }

    public String getForeignKeyTableName() {
        return foreignKeyTableName;
    }

    public void setForeignKeyTableName(String foreignKeyTableName) {
        this.foreignKeyTableName = foreignKeyTableName;
    }

    public List<CodeValue> getValidValues() {
        return validValues;
    }

    public void setValidValues(List<CodeValue> validValues) {
        this.validValues = validValues;
    }

    

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColumnScale() {
        return columnScale;
    }

    public void setColumnScale(String columnScale) {
        this.columnScale = columnScale;
    }

    public String getColumnPrecision() {
        return columnPrecision;
    }

    public void setColumnPrecision(String columnPrecision) {
        this.columnPrecision = columnPrecision;
    }

    public String getColumnDatatype() {
        return columnDatatype;
    }

    public void setColumnDatatype(String columnDatatype) {
        this.columnDatatype = columnDatatype;
    }

    public String getSdidescription() {
        return sdidescription;
    }

    public void setSdidescription(String sdidescription) {
        this.sdidescription = sdidescription;
    }

    public boolean getSdiflag() {
        return sdiflag;
    }

    public void setSdiflag(boolean sdiflag) {
        this.sdiflag = sdiflag;
    }

    public String getPhysicalColumnName() {
        return physicalColumnName;
    }

    public void setPhysicalColumnName(String physicalColumnName) {
        this.physicalColumnName = physicalColumnName;
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

    public String getColumnComments() {
        return columnComments;
    }

    public void setColumnComments(String columnComments) {
        this.columnComments = columnComments;
    }

    public Integer getColumnId() {
        return columnId;
    }

    public void setColumnId(Integer columnId) {
        this.columnId = columnId;
    }

    public boolean getColumnNullableFlag() {
        return columnNullableFlag;
    }

    public void setColumnNullableFlag(boolean columnNullableFlag) {
        this.columnNullableFlag = columnNullableFlag;
    }

    public String getForeignKeyColumnName() {
        return foreignKeyColumnName;
    }

    public void setForeignKeyColumnName(String foreignKeyColumnName) {
        this.foreignKeyColumnName = foreignKeyColumnName;
    }

    public Integer getTableId() {
        return tableId;
    }

    public void setTableId(Integer tableId) {
        this.tableId = tableId;
    }

    public boolean getPrimaryKeyFlag() {
        return primaryKeyFlag;
    }

    public void setPrimaryKeyFlag(boolean primaryKeyFlag) {
        this.primaryKeyFlag = primaryKeyFlag;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public List<KeyValueBean> getExtendedProperties() {
        return extendedProperties;
    }

    public void setExtendedProperties(List<KeyValueBean> extendedProperties) {
        this.extendedProperties = extendedProperties;
    }

   
}
