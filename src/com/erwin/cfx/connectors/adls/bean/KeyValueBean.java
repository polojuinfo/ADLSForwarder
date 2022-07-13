/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.erwin.cfx.connectors.adls.bean;

import com.ads.api.beans.common.AuditHistory;
import com.ads.api.beans.kv.KeyValue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author SMastanV
 */
public class KeyValueBean {
     private String objectTypeId = "";
    private Map<String, Object> userData = new HashMap();
    private AuditBean auditHistory;
    private String value = "";
    private String key = "";
    private List<KeyValue> keyValues = new ArrayList();

    public String getObjectTypeId() {
        return objectTypeId;
    }

    public void setObjectTypeId(String objectTypeId) {
        this.objectTypeId = objectTypeId;
    }

    public Map<String, Object> getUserData() {
        return userData;
    }

    public void setUserData(Map<String, Object> userData) {
        this.userData = userData;
    }

    public AuditBean getAuditHistory() {
        return auditHistory;
    }

    public void setAuditHistory(AuditBean auditHistory) {
        this.auditHistory = auditHistory;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<KeyValue> getKeyValues() {
        return keyValues;
    }

    public void setKeyValues(List<KeyValue> keyValues) {
        this.keyValues = keyValues;
    }
}
