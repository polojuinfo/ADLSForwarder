/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.erwin.cfx.connectors.adls.emm.utils;

import com.erwin.cfx.connectors.adls.ADLSFilesUploader;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author SMastanV
 */
public class CommonUtil {
private static final Logger logger = Logger.getLogger(CommonUtil.class);

    public static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    public static Calendar cal = Calendar.getInstance();
    public String delimiter = "#Erwin#";
    public String SM_ENVIRONMENT = "SM_ENVIRONMENT";
    public String SM_TABLE = "SM_TABLE";
    public String SM_COLUMN = "SM_COLUMN";
    public String CREATED_DATE = "createdDate";
    public String LAST_MODIFIED_DATE = "lastModifiedDate";
    public String databaseUserName = "databaseUserName";
    public String databasePassword = "databasePassword";
    
    private static CommonUtil commonUtil;
/**
 * Singleton object creation
 * @return 
 */
    public static CommonUtil getInstance() {
        if (commonUtil == null) {
            commonUtil = new CommonUtil();
        }
        return commonUtil;
    }
    
    public void setLogAppender(){
        logger.addAppender(ADLSFilesUploader.appender);
    }
/**
 * maps metadata manager objects to json
 * @param object
 * @return 
 */
    public String mapSMObjectToJSON(Object object) {
        //cal.setTimeInMillis(System.currentTimeMillis());
        //logger.info("Starting Time :" + formatter.format(cal.getTime()));
        String json = "";
        try {
            ObjectMapper mapper = new ObjectMapper();
            json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (IOException ex) {
            logger.error("Error in CommonUtil :\n",ex);
        }
    //cal.setTimeInMillis(System.currentTimeMillis());
    //logger.info("Ending Time :" + formatter.format(cal.getTime()));
        return json;
    }
/**
 * adds additional list to json array
 * @param parentJSON
 * @param childJSON
 * @param smObjectType
 * @return
 * @throws JSONException 
 */
    public JSONObject addListToJSONArray(String parentJSON, String childJSON, String smObjectType) throws JSONException {
        cal.setTimeInMillis(System.currentTimeMillis());
        //logger.info("Starting Time :" + formatter.format(cal.getTime()));
        //logger.info("Started addListToJSONArray Method");
        JSONObject jsonObject = new JSONObject(parentJSON);
        try{
        if (smObjectType.equals(SM_TABLE)) {
            JSONArray insertingColumnsArray = new JSONArray(childJSON);
            jsonObject.put("columns", insertingColumnsArray);

        } else if (smObjectType.equals(SM_ENVIRONMENT)) {
            JSONArray insertingtablesArray = new JSONArray(childJSON);
            jsonObject.put("tables", insertingtablesArray);
        }
        }catch(Exception ex){
            logger.error("Error in addListToJSONArray:\n",ex);
        }
    cal.setTimeInMillis(System.currentTimeMillis());
    //logger.info("Ending Time :" + formatter.format(cal.getTime()));
    //logger.info("Ended addListToJSONArray method");
        return jsonObject;
    }
/**
 * converts milliseconds to date
 * @param epocTime
 * @return 
 */
    public String getEpocTimeInDateFormat(String epocTime) {//1645451213890
        cal.setTimeInMillis(System.currentTimeMillis());
        //logger.info(":: START ::Method_Name :: getEpocTimeInDateFormat ::" + formatter.format(cal.getTime()));
        try {
            long milliseconds = Long.parseLong(epocTime);
            //logger.info("::epocTime:: "+ epocTime + "::Long.parseLong(epocTime) :: "+milliseconds);
            
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(milliseconds);
            //SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.ENGLISH);//02/21/2022 19:16:53
            SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD", Locale.ENGLISH);
            epocTime = sdf.format(cal.getTime());
            //logger.info(":: epocTime :: "+epocTime);
            //date = sdf.parse(milliseconds);
            //System.out.println(date); // 2/21/2022 07:16:53   //02/21/2022 19:16:53
        } catch (Exception ex) {
            logger.error("Error in getEpocTimeInDateFormat:\n",ex);
        }
    cal.setTimeInMillis(System.currentTimeMillis());
    //logger.info(":: End :: Method_Name :: getEpocTimeInDateFormat :: " + formatter.format(cal.getTime()));
        return epocTime;
    }
/**
 * filtering unneccessary json objects
 * @param jsonString
 * @return 
 */
    public String filterJSON(String jsonString) {
        cal.setTimeInMillis(System.currentTimeMillis());
        //logger.info("Starting Time :" + formatter.format(cal.getTime()));
        //logger.info("Started filterJSON Method");
        String lastLoadingTime="";
        try {
            JSONObject json = new JSONObject(jsonString);
            json.remove(databaseUserName);
            json.remove(databasePassword);
            lastLoadingTime = json.getString("lastLoadingTime");
            lastLoadingTime = getEpocTimeInDateFormat(lastLoadingTime);
            json.put("lastLoadingTime", lastLoadingTime);
            jsonString = json.toString();
        } catch (JSONException ex) {
            logger.error("Error in filterJSON:\n",ex);
        }
    cal.setTimeInMillis(System.currentTimeMillis());
    //logger.info("Ending Time :" + formatter.format(cal.getTime()));
    //logger.info("Ended filterJSON method");
        return jsonString;
    }
    /**
     * updatating milliseconds to date for extending properties
     * @param jsonString
     * @return 
     */
    public String modifyExtendPorpertiesDateFormats(String jsonString){
        cal.setTimeInMillis(System.currentTimeMillis());
        logger.info("Starting Time :" + formatter.format(cal.getTime()));
        try {
            String extendedProperties = "extendedProperties";
            JSONObject json = new JSONObject(jsonString);
            JSONArray jsonArray=null;
             jsonArray = (JSONArray) json.get(extendedProperties);
             int length = jsonArray.length();
            if(length!=0){
                String modifiedJsonString ;
                JSONObject modifiedPropJSON = null;
                for(int i=0;i<length;i++){
                   modifiedJsonString = modifyAuditDateFormats(jsonArray.getJSONObject(i).toString());
                   modifiedPropJSON = new JSONObject(modifiedJsonString);
                   jsonArray.put(i, modifiedPropJSON);
                }
                json.put(extendedProperties, jsonArray);
                jsonString = json.toString();
            }
        } catch (JSONException ex) {
           logger.error("Error in modifyExtendPorpertiesDateFormats :\n",ex);
        }
        cal.setTimeInMillis(System.currentTimeMillis());
        logger.info("Ending Time :" + formatter.format(cal.getTime()));
         return jsonString;
    }
    /**
     * updating milliseconds to date for audithistory
     * @param jsonString
     * @return 
     */
    public String modifyAuditDateFormats(String jsonString){
        cal.setTimeInMillis(System.currentTimeMillis());
        //logger.info("Starting Time :" + formatter.format(cal.getTime()));
        //logger.info("Started modifyAuditDateFormats Method");
        String auditHistory = "auditHistory";
        String createdDate;
        String lastModifiedDate;
        try {
            JSONObject json = new JSONObject(jsonString);
            JSONObject auditJSON = (JSONObject) json.get(auditHistory);
            createdDate = auditJSON.getString("createdDate");
            createdDate = getEpocTimeInDateFormat(createdDate);
            lastModifiedDate = auditJSON.getString("lastModifiedDate");
            lastModifiedDate = getEpocTimeInDateFormat(lastModifiedDate);
            auditJSON.put("createdDate", createdDate);
            auditJSON.put("lastModifiedDate", lastModifiedDate);
            json.put(auditHistory, auditJSON);
            jsonString = json.toString();
        } catch (JSONException ex) {
           logger.error("Error in modifyAuditDateFormats :\n",ex);
        }
        cal.setTimeInMillis(System.currentTimeMillis());
    //logger.info("Ending Time :" + formatter.format(cal.getTime()));
    //logger.info("Ended modifyAuditDateFormats method");
        return jsonString;
    }
}
