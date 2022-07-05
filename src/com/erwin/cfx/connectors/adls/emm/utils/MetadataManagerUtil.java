/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.erwin.cfx.connectors.adls.emm.utils;

import com.ads.api.beans.common.AuditHistory;
import com.ads.api.beans.common.Node;
import com.ads.api.beans.kv.KeyValue;
import com.ads.api.beans.sm.SMColumn;
import com.ads.api.beans.sm.SMEnvironment;
import com.ads.api.beans.sm.SMTable;
import com.ads.api.util.KeyValueUtil;
import com.ads.api.util.SystemManagerUtil;
import static com.erwin.cfx.connectors.adls.ADLSFilesUploader.appender;
import com.erwin.cfx.connectors.adls.bean.ColumnBean;
import com.erwin.cfx.connectors.adls.bean.EnvironmentBean;
import com.erwin.cfx.connectors.adls.bean.KeyValueBean;
import com.erwin.cfx.connectors.adls.bean.TableBean;
import static com.erwin.cfx.connectors.adls.emm.utils.CommonUtil.cal;
import static com.erwin.cfx.connectors.adls.emm.utils.CommonUtil.formatter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.icc.util.ADSConnectionPool;
import com.icc.util.RequestStatus;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;


/**
 *
 * @author SMastanV
 */
public class MetadataManagerUtil { 

    private static final Logger logger = Logger.getLogger(MetadataManagerUtil.class);
    private static List<String> userdata = Arrays.asList("description", "keyValueId", "parentId", "kv_orde", "Type", "objectId");
    CommonUtil commonUtil = CommonUtil.getInstance();
    private Map<Integer,List<SMTable>> allTables = new HashMap();
    private Map<Integer,List<SMColumn>> allColumns = new HashMap();
    private List<Integer> tableIds = new ArrayList();
    private List<Integer> columnIds = new ArrayList();
    StringBuffer sb = new StringBuffer();
    
    /**
     * Iterates partial environmentjsons and adds complete table json to it
     *
     * @param systemManagerUtil
     * @param keyValueUtil
     * @param metadataSelection
     * @return
     * @throws JsonProcessingException
     * @throws JSONException
     */
    public Map getEnvironmentJSONSMap(SystemManagerUtil systemManagerUtil, KeyValueUtil keyValueUtil,
            Map metadataSelection,List<Integer> selectedTbls,boolean isExtendedProp,String logFilePath) {
        logger.addAppender(appender);
        commonUtil.setLogAppender();
        cal.setTimeInMillis(System.currentTimeMillis());
             
        logger.info("Method_Name :: getEnvironmentJSONSMap :: Starting Time :" + formatter.format(cal.getTime()));
        sb.append("---------------------  \n Executution status \n---------------------\n");
        
        sb.append("Method_Name :: getEnvironmentJSONSMap :: Starting Time :" + formatter.format(cal.getTime())+"\n");
        
        Map<Integer, SMEnvironment> environmentObjects = new HashMap();
        
        Map<String, String> sysEnvJSONMap = null;
         try { 
             environmentObjects = getEnvironments(systemManagerUtil, metadataSelection,sb);
             sysEnvJSONMap = new HashMap();
             sysEnvJSONMap = getEnvironmentJSONS(systemManagerUtil,keyValueUtil, environmentObjects,selectedTbls,isExtendedProp,sb);
             
       
        cal.setTimeInMillis(System.currentTimeMillis());
        logger.info("Method_Name::getEnvironmentJSONSMap::Ending Time :" + formatter.format(cal.getTime())+"\n");
        sb.append("Method_Name :: getEnvironmentJSONSMap :: Ending Time :" + formatter.format(cal.getTime())+"\n");
        
         //START :Creating file and writing the logs
         FileUtils.writeStringToFile(new File(logFilePath + File.separator + "ActivityLog.txt"), sb.toString(),true);
         //END :Creating file and writing the logs
         } catch (Exception ex) {
            logger.error("Error occured in getEnvironmentJSONSMap method :\n"+ex.getMessage()+"\n");
            sb.append("Error occured in getEnvironmentJSONSMap method :\n " +ex.getMessage()+"\n");
        }
        return sysEnvJSONMap;
    }

    /**
     * gets environment objects by system id and stores in map key
     * enironmentid,value environment object
     *
     * @param systemManagerUtil
     * @param metadataSelection
     * @return
     */
    private Map getEnvironments(SystemManagerUtil systemManagerUtil, Map<Integer, String> metadataSelection,StringBuffer sb) {
        cal.setTimeInMillis(System.currentTimeMillis());
        
        logger.info("::Method_NAME::getEnvironments::Starting Time :" + formatter.format(cal.getTime())+"\n");
        sb.append("::Method_NAME::getEnvironments::Starting Time :" + formatter.format(cal.getTime())+"\n");
        Map<Integer, SMEnvironment> environments = new HashMap();
        SMEnvironment environment;
        String environmentName;
        RequestStatus requestStatus;
        
        long start;
        long end;
        long executionTimeinSec; 
        
        try {
            if (metadataSelection != null) {
                for (int environmentId : metadataSelection.keySet()) {
                    start = System.nanoTime();
                    
                    environmentName = metadataSelection.get(environmentId).split(commonUtil.delimiter)[0];
                    int systemId = Integer.parseInt(metadataSelection.get(environmentId).split(commonUtil.delimiter)[1]);
                    environment = systemManagerUtil.getEnvironmentBySystemId(systemId, environmentName);
                    environments.put(environmentId, environment);
                    
                    end = System.nanoTime();
                    executionTimeinSec = TimeUnit.SECONDS.convert(end - start, TimeUnit.NANOSECONDS);
                    logger.info("Method_Name::getEnvironments:: Time for fetching  "+environmentName+ "::SMEnvironment::"+ + executionTimeinSec);
                    sb.append("Method_Name::getEnvironments:: Time for fetching  "+environmentName+ "::SMEnvironment::"+ + executionTimeinSec+"\n");
                }
            }
        } catch (Exception ex) {
            logger.error("Error in getEnvironments :\n"+ex.getMessage());
            sb.append("Error in getEnvironments :\n"+ex.getMessage());
        }
        cal.setTimeInMillis(System.currentTimeMillis());
        logger.info("Ended getEnvironments method::END time"+formatter.format(cal.getTime()));
        sb.append("Ended getEnvironments method::END time \n");
        return environments;
    }
    /**
     * Collects all tables of all environements
     * @param systemManagerUtil
     * @param envIds
     * @return 
     */
private void collectAllTables(SystemManagerUtil systemManagerUtil,Set<Integer> envIds){
    cal.setTimeInMillis(System.currentTimeMillis());
    logger.info("Starting Time :" + formatter.format(cal.getTime()));
    try{
    envIds.stream().forEach(envId->{
    List<SMTable> tbls = systemManagerUtil.getEnvironmentTables(envId);
    allTables.put(envId, tbls);
    tbls.stream().forEach(table->{
        tableIds.add(table.getTableId());
    });
    });
    }catch(Exception ex){
        logger.error("Error occured in getAllTableIds : "+ex.getMessage());
    }
    cal.setTimeInMillis(System.currentTimeMillis());
        logger.info("Ending Time :" + formatter.format(cal.getTime()));
    }
/**
 * collects all table ids of all environments
 * @return 
 */
//private List getAllTableIds(){
//    cal.setTimeInMillis(System.currentTimeMillis());
//        logger.info("Starting Time :" + formatter.format(cal.getTime()));
//        logger.info("Started getAllTableIds Method");
//    List<Integer> tableIds = new ArrayList();
//    try{
//    for(int envId : allTables.keySet()){
//        allTables.get(envId).stream().forEach(table->{
//        tableIds.add(table.getTableId());
//        });
//    }
//    
//    
//    }catch(Exception ex){
//        logger.error("Error occured in getAllTableIds : ",ex);
//    }
//    cal.setTimeInMillis(System.currentTimeMillis());
//        logger.info("Ending Time :" + formatter.format(cal.getTime()));
//        logger.info("Ended getAllTableIds method");
//    return tableIds;
//}
/**
     * Collects all columns of all tables
     * @param systemManagerUtil
     * @param tableIds
     * @return 
     */
private void collectAllColumns(SystemManagerUtil systemManagerUtil,List<Integer> tableIds){
    cal.setTimeInMillis(System.currentTimeMillis());
    
    logger.info("Starting Time :" + formatter.format(cal.getTime()));
    
    try{
    tableIds.stream().forEach(tableId->{
    List<SMColumn> clumns = systemManagerUtil.getColumns(tableId);
    allColumns.put(tableId, clumns);
    clumns.stream().forEach(column->{
        columnIds.add(column.getColumnId());
    });
    }
    );
    }catch(Exception ex){
        logger.error("Error occured in getAllTableIds : "+ex.getMessage());
    }
    cal.setTimeInMillis(System.currentTimeMillis());
     logger.info("Ending Time :" + formatter.format(cal.getTime()));
}

    /**
     * Iterate environment objects map and map it to json object and stores in
     * map
     *
     * @param keyValueUtil
     * @param environments
     * @return
     */
    private Map getEnvironmentJSONS(SystemManagerUtil systemManagerUtil, KeyValueUtil keyValueUtil, Map<Integer, SMEnvironment> environments, List<Integer> selectedTbls, boolean isExtendedPorp,StringBuffer sb) {
        cal.setTimeInMillis(System.currentTimeMillis());
        logger.info("::Method_Name::getEnvironmentJSONS::Starting Time :" + formatter.format(cal.getTime()));
        sb.append("::Method_Name::getEnvironmentJSONS::Starting Time :" + formatter.format(cal.getTime())+"\n");
        List<KeyValue> extendedProperties = null;
        String environmentJSON;
        JSONObject envJSONObj = null;
        String tablesJSONList;
        Map<String, String> sysEnvJSONMap = new HashMap();
        
        long start;
        long end;
        long executionTimeinSec; 
        
        try {
            Set<Integer> envIds = environments.keySet();
            List<Integer> envIdsList = new ArrayList(envIds);
            
            start = System.nanoTime();
            collectAllTables(systemManagerUtil,envIds);
            end = System.nanoTime();
            executionTimeinSec = TimeUnit.SECONDS.convert(end - start, TimeUnit.NANOSECONDS);
            logger.info("Method_Name::getEnvironmentJSONS:: Time for fetching collectAllTables :: "+ executionTimeinSec);
            sb.append("Method_Name::getEnvironmentJSONS:: Time for fetching collectAllTables :: "+ executionTimeinSec+"\n");
                    
            start = System.nanoTime();
            collectAllColumns(systemManagerUtil,tableIds);
            end = System.nanoTime();
            executionTimeinSec =  TimeUnit.SECONDS.convert(end - start, TimeUnit.NANOSECONDS);
            logger.info("Method_Name::getEnvironmentJSONS:: Time for fetching collectAllColumns :: "+ executionTimeinSec);
            sb.append("Method_Name::getEnvironmentJSONS:: Time for fetching collectAllColumns :: "+ executionTimeinSec+"\n");
            
            if (isExtendedPorp) {
                
            start = System.nanoTime();
            Map<Integer, List<KeyValue>> envExtProps = getExtendedProperties(envIdsList, commonUtil.SM_ENVIRONMENT, keyValueUtil);
            collectAllColumns(systemManagerUtil,tableIds);
            end = System.nanoTime();
            executionTimeinSec = TimeUnit.SECONDS.convert(end - start, TimeUnit.NANOSECONDS);
            logger.info("Method_Name::getEnvironmentJSONS:: Time for fetching getExtendedProperties for SM_ENVIRONMENT :: "+ executionTimeinSec);
            sb.append("Method_Name::getEnvironmentJSONS:: Time for fetching getExtendedProperties for SM_ENVIRONMENT :: "+ executionTimeinSec+"\n");
             
            start = System.nanoTime();
            Map<Integer, List<KeyValue>> tblExtProps = getExtendedProperties(tableIds, commonUtil.SM_TABLE, keyValueUtil);
            end = System.nanoTime();
            executionTimeinSec = TimeUnit.SECONDS.convert(end - start, TimeUnit.NANOSECONDS);
            logger.info("Method_Name::getEnvironmentJSONS:: Time for fetching getExtendedProperties for SM_TABLE :: "+ executionTimeinSec );
            sb.append("Method_Name::getEnvironmentJSONS:: Time for fetching getExtendedProperties for SM_TABLE :: "+ executionTimeinSec+"sec \n");
              
            start = System.nanoTime();
            //Map<Integer, List<KeyValue>> clmExtProps = getExtendedProperties(columnIds, commonUtil.SM_COLUMN, keyValueUtil);
            Map<Integer, List<KeyValueBean>> clmExtProps = getColumnExtendedProperties(columnIds, commonUtil.SM_COLUMN, keyValueUtil,sb);
            end = System.nanoTime();
            executionTimeinSec = TimeUnit.SECONDS.convert(end - start, TimeUnit.NANOSECONDS);
            logger.info("Method_Name::getEnvironmentJSONS:: Time for fetching getExtendedProperties for SM_COLUMN :: "+ executionTimeinSec);
            sb.append("Method_Name::getEnvironmentJSONS:: Time for fetching getExtendedProperties for SM_COLUMN :: "+ executionTimeinSec+"sec \n"); 
            
               start = System.nanoTime();
                for (int environmentId : envIds) {
                    SMEnvironment environment = environments.get(environmentId); //If environment object is null
                    EnvironmentBean mmEnv = setEnvironmentProperties(environment);
                    //System.out.println(envExtProps.size());
                    if(envExtProps!=null && !envExtProps.isEmpty()){
                    extendedProperties = envExtProps.get(environmentId);
                    //System.out.println("Coming extended properties : "+extendedProperties);
                    if (extendedProperties != null && !extendedProperties.isEmpty()) {
                        int length = extendedProperties.size();
                        List<KeyValueBean> extProp = getModifiedExtProperties(extendedProperties, length);
                        mmEnv.setExtendedProperties(extProp);
                    }
                    }
                    environmentJSON = commonUtil.mapSMObjectToJSON(mmEnv);
                    if (extendedProperties != null && !extendedProperties.isEmpty()) {
                        environmentJSON = commonUtil.modifyExtendPorpertiesDateFormats(environmentJSON);
                    }
                    environmentJSON = commonUtil.modifyAuditDateFormats(environmentJSON);
                    List<SMTable> tables = allTables.get(environmentId);
                    if(!selectedTbls.isEmpty()|| selectedTbls!=null){
                        tables = getSelectedTables(tables,selectedTbls);
                    }
                    tablesJSONList = getTablesJSONListWithExtProp(tables,tblExtProps,clmExtProps);
                    envJSONObj = commonUtil.addListToJSONArray(environmentJSON, tablesJSONList, commonUtil.SM_ENVIRONMENT);
                    environmentJSON = envJSONObj.toString();
                    SMEnvironment obj = systemManagerUtil.getEnvironment(environmentId);
                    sysEnvJSONMap.put(obj.getSystemName() + commonUtil.delimiter + obj.getSystemEnvironmentName(), environmentJSON);
                }
                end = System.nanoTime();
                executionTimeinSec = TimeUnit.SECONDS.convert(end - start, TimeUnit.NANOSECONDS);
                logger.info("Method_Name::getEnvironmentJSONS:: Time taking for setting extended properties :: "+ executionTimeinSec);
                sb.append("Method_Name::getEnvironmentJSONS:: Time taking for setting extended properties :: "+ executionTimeinSec+"\n");
                
            } else {
                
                start = System.nanoTime();
                for (int environmentId : environments.keySet()) {
                    SMEnvironment environment = environments.get(environmentId);
                    EnvironmentBean mmEnv = setEnvironmentProperties(environment);
                    environmentJSON = commonUtil.mapSMObjectToJSON(mmEnv);
                    environmentJSON = commonUtil.modifyAuditDateFormats(environmentJSON);
                    List<SMTable> tables = allTables.get(environmentId);
                    if(!selectedTbls.isEmpty()&&selectedTbls!=null){
                        tables = getSelectedTables(tables,selectedTbls);
                    }
                    tablesJSONList = getTablesJSONListWithoutExtProp(tables);
                    envJSONObj = commonUtil.addListToJSONArray(environmentJSON, tablesJSONList, commonUtil.SM_ENVIRONMENT);
                    environmentJSON = envJSONObj.toString();
                    SMEnvironment obj = systemManagerUtil.getEnvironment(environmentId);
                    sysEnvJSONMap.put(obj.getSystemName() + commonUtil.delimiter + obj.getSystemEnvironmentName(), environmentJSON);
                }
                end = System.nanoTime();
                executionTimeinSec = TimeUnit.SECONDS.convert(end - start, TimeUnit.NANOSECONDS);
                logger.info("Method_Name::getEnvironmentJSONS:: Time taking for with out extended properties :: "+ executionTimeinSec);
                sb.append("Method_Name::getEnvironmentJSONS:: Time taking for with out extended properties :: "+ executionTimeinSec+"\n");
            }
        } catch (Exception ex) {
            logger.error("Error in 11111 getEnvironmentJSONS:\n"+ex.getMessage());
            sb.append("Error in 11111 getEnvironmentJSONS:"+ ex.getMessage());
        }
        cal.setTimeInMillis(System.currentTimeMillis());
        logger.info("::Method_Name:: getEnvironmentJSONS :: Ending Time :" + formatter.format(cal.getTime()));
        sb.append("::Method_Name:: getEnvironmentJSONS :: Ending Time :" + formatter.format(cal.getTime())+"\n");
        return sysEnvJSONMap;
    }

    /**
     * gets extended properties of metadata manager objects
     *
     * @param object
     * @param ObjectType
     * @param keyValueUtil
     * @return
     */
    private Map getExtendedProperties(List ids, String ObjectType, KeyValueUtil keyValueUtil) {
        cal.setTimeInMillis(System.currentTimeMillis());
        logger.info("Starting Time :" + formatter.format(cal.getTime()));
        Map<Integer, List<KeyValue>> keyValues = null;
        try {
            
            if (ObjectType.equalsIgnoreCase("SM_ENVIRONMENT")) {
                 keyValues = keyValueUtil.getKeyValues(Node.NodeType.SM_ENVIRONMENT,  ids);
            } else if (ObjectType.equalsIgnoreCase("SM_TABLE")) {
                 keyValues = keyValueUtil.getKeyValues(Node.NodeType.SM_TABLE,  ids);
            } else if (ObjectType.equalsIgnoreCase("SM_COLUMN")) {
                 keyValues = keyValueUtil.getKeyValues(Node.NodeType.SM_COLUMN, ids);
            }                 
        } catch (Exception ex) {
            logger.error("Error in getExtendedProperties:\n", ex);
        }
        cal.setTimeInMillis(System.currentTimeMillis());
        logger.info("Ending Time :" + formatter.format(cal.getTime()));
        return keyValues;
    }
    
    
    private Map getColumnExtendedProperties(List ids, String ObjectType, KeyValueUtil keyValueUtil,StringBuffer sb) throws SQLException {
        cal.setTimeInMillis(System.currentTimeMillis());
        logger.info("Starting Time :" + formatter.format(cal.getTime()));
        sb.append("::Method_name :: getColumnExtendedProperties");
        Map<Integer, List<KeyValueBean>> keyValues = new HashMap();
        KeyValueBean kvBean = null;
        List<KeyValueBean> kv = null;
        AuditHistory auditHistory;
        Map<String, Object> userData;
        Connection connection = null;
        try {
            String qry = "SELECT KV_ID AS keyValueId,KEY_NAME AS Name,KEY_VALUE AS value,OBJECT_TYPE_ID,OBJECT_ID AS objectId,KV_TYPE AS Type,DESCRIPTION AS description,CREATED_DATE_TIME,LAST_MODIFIED_DATE_TIME,CREATED_BY,LAST_MODIFIED_BY  from ADS_KEY_VALUE WHERE OBJECT_TYPE_ID =4 AND OBJECT_ID IN ("+StringUtils.join(ids,",")+") order by OBJECT_ID desc";
            logger.info(":: Query ::" + qry);
            sb.append(":: Query ::" + qry + "\n");
            
            connection = ADSConnectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement(qry);
                       
            ResultSet rs = statement.executeQuery();
            int objectId = 0;
            while(rs.next()){
                objectId = rs.getInt("objectId");
                 kvBean = new KeyValueBean();
                 auditHistory = new AuditHistory();
                 userData = new HashMap();
                 //setting properties
                 userData.put("Type", rs.getString("Type"));
                 userData.put("description", rs.getString("description"));
                 userData.put("keyValueId", rs.getString("keyValueId"));
                 userData.put("objectId", rs.getString("objectId"));
                 auditHistory.setCreatedBy(rs.getString("CREATED_BY"));
                 auditHistory.setCreatedDate(rs.getTimestamp("CREATED_DATE_TIME"));
                 auditHistory.setLastModifiedBy(rs.getString("LAST_MODIFIED_BY"));
                 auditHistory.setLastModifiedDate(rs.getTimestamp("LAST_MODIFIED_DATE_TIME"));
                 kvBean.setKey(rs.getString("Name"));
                 kvBean.setValue(rs.getString("Value"));
                 kvBean.setObjectTypeId(rs.getString("OBJECT_TYPE_ID"));
                 kvBean.setAuditHistory(auditHistory);
                 kvBean.setUserData(userData);
                 kvBean.setKeyValues(null);
                 if(keyValues.containsKey(objectId)){
                   kv = keyValues.get(objectId);
                }else{
                   kv = new ArrayList();
                }
                kv.add(kvBean);
                keyValues.put(objectId, kv);
            } 
        } catch (Exception ex) {
            logger.error("Error in getColumnExtendedProperties:\\n ",ex);
            sb.append("Error in getColumnExtendedProperties:\n "+ex.getMessage());
        }
        finally{
             connection.close();
        }
        cal.setTimeInMillis(System.currentTimeMillis());
        logger.info("Ending Time :" + formatter.format(cal.getTime()));
        return keyValues;
    }

    /**
     * gets all tables with environmetid , iterates , maps to json and stores it
     * in map
     *
     * @param systemManagerUtil
     * @param keyValueUtil
     * @param environments
     * @return
     * @throws JSONException
     */
    private String getTablesJSONListWithExtProp(List<SMTable> tables, Map<Integer, List<KeyValue>> tblExtProps, Map<Integer, List<KeyValueBean>> clmExtProps) throws JSONException {
        cal.setTimeInMillis(System.currentTimeMillis());
        logger.info("Starting Time :" + formatter.format(cal.getTime()));
        List<KeyValue> extendedProperties;
        //Map<Integer, List> tablesJson;
        List<JSONObject> tableJsonList = null;
        JSONObject tableJSONObject = new JSONObject();
        String tableJSON;
        String columnsJSON;
        int tableId = 0;
        List<SMColumn> columns = null;
        //tablesJson = new HashMap();
        try {
                tableJsonList = new ArrayList();
                for (SMTable table : tables) {
                    tableId = table.getTableId();
                    TableBean tableBean = setTableProperties(table);
                    extendedProperties = tblExtProps.get(tableId);
                    if (extendedProperties != null && !extendedProperties.isEmpty()) {
                        int length = extendedProperties.size();
                        List<KeyValueBean> extProp = getModifiedExtProperties(extendedProperties, length);
                        tableBean.setExtendedProperties(extProp);
                    }
                    tableJSON = commonUtil.mapSMObjectToJSON(tableBean);
                    if (extendedProperties != null && !extendedProperties.isEmpty()) {
                        tableJSON = commonUtil.modifyExtendPorpertiesDateFormats(tableJSON);
                    }
                    tableJSON = commonUtil.modifyAuditDateFormats(tableJSON);
                    columns = allColumns.get(tableId);
                    columnsJSON = getColumnsJsonWithExtProp(columns,clmExtProps);
                    //adding columns json to table json
                  //tableJSONObject = commonUtil.addListToJSONArray(tableJSON, columnsJSON, commonUtil.SM_TABLE);
                    //adding complete table json to list
                  //tableJsonList.add(tableJSONObject);
            }
        } catch (Exception ex) {
            logger.error("Error in getTablesJSONList:\n"+ex.getMessage());
        }
        cal.setTimeInMillis(System.currentTimeMillis());
        logger.info("Ending Time :" + formatter.format(cal.getTime()));
        return tableJsonList.toString();
    }

     /**
     * gets all tables with environmetid , iterates , maps to json and stores it
     * in map
     *
     * @param systemManagerUtil
     * @param keyValueUtil
     * @param environments
     * @return
     * @throws JSONException
     */
    private String getTablesJSONListWithoutExtProp(List<SMTable> tables) throws JSONException {
        cal.setTimeInMillis(System.currentTimeMillis());
        //logger.info("Starting Time :" + formatter.format(cal.getTime()));
        //logger.info("Started getTablesJSONList Method");
        List<JSONObject> tableJsonList = null;
        JSONObject tableJSONObject = new JSONObject();
        String tableJSON;
        String columnsJSON;
        List<SMColumn> columns = null;
        int tableId = 0;
        try {
                tableJsonList = new ArrayList();
                for (SMTable table : tables) {
                    tableId = table.getTableId();
                    TableBean tableBean = setTableProperties(table);
                    tableJSON = commonUtil.mapSMObjectToJSON(tableBean);
                    tableJSON = commonUtil.modifyAuditDateFormats(tableJSON);
                    columns = allColumns.get(tableId);
                    columnsJSON = getColumnsJsonWithoutExtProp(columns, tableId);
                    //adding columns json to table json
                    tableJSONObject = commonUtil.addListToJSONArray(tableJSON, columnsJSON, commonUtil.SM_TABLE);
                    //adding complete table json to list
                    tableJsonList.add(tableJSONObject);
            }
        } catch (Exception ex) {
            logger.error("Error in getTablesJSONList:\n", ex);
        }
        cal.setTimeInMillis(System.currentTimeMillis());
        //logger.info("Ending Time :" + formatter.format(cal.getTime()));
        //logger.info("Ended getTablesJSONList method");
        return tableJsonList.toString();
    }

    
    /**
     * gets columns of table by tableid , maps to json object
     *
     * @param systemManagerUtil
     * @param keyValueUtil
     * @param tableId
     * @return
     */
    private String getColumnsJsonWithExtProp(List<SMColumn> columns, Map<Integer, List<KeyValueBean>> clmExtProps) {
        cal.setTimeInMillis(System.currentTimeMillis());
        //logger.info("START :: Method_Name :: getColumnsJsonWithExtProp Method"+formatter.format(cal.getTime()));
        List<KeyValueBean> extendedProperties = null;
        String columnJSON;
        List<String> columnjsons = new ArrayList();
        try {
            if (columns != null && !columns.isEmpty()) {
                for (int i = 0; i < columns.size(); i++) {
                    SMColumn column = columns.get(i);
                    int columnId = column.getColumnId();
                    ColumnBean columnBean = setColumnProperties(column);
                    
                    if (clmExtProps.containsKey(columnId)) {
                        extendedProperties = clmExtProps.get(columnId);
                        columnBean.setExtendedProperties(extendedProperties);
                    }
                    columnJSON = commonUtil.mapSMObjectToJSON(columnBean);
                    //logger.info(":: columnJSON ::"+columnJSON);
                    if (extendedProperties != null && !extendedProperties.isEmpty()) {
                        columnJSON = commonUtil.modifyExtendPorpertiesDateFormats(columnJSON);
                        //logger.info(":: modifyExtendPorpertiesDateFormats :: columnJSON ::"+columnJSON);
                    }
                    columnJSON = commonUtil.modifyAuditDateFormats(columnJSON);
                    //logger.info(":: modifyAuditDateFormats :: columnJSON ::"+columnJSON);
                     columnjsons.add(columnJSON);
                    //columnsJSON = commonUtil.mapSMObjectToJSON(columns);
                }
            }
        } catch (Exception ex) {
            logger.error("Error in getColumnsJsonWithExtProp:\n", ex);
        }
        cal.setTimeInMillis(System.currentTimeMillis());
        //logger.info("END :: Method_Name :: getColumnsJsonWithExtProp Method"+formatter.format(cal.getTime()));
        return columnjsons.toString();
    }

     /**
     * gets columns of table by tableid , maps to json object
     *
     * @param systemManagerUtil
     * @param keyValueUtil
     * @param tableId
     * @return
     */
    private String getColumnsJsonWithoutExtProp(List<SMColumn> columns, int tableId) {
        cal.setTimeInMillis(System.currentTimeMillis());
        //logger.info("Starting Time :" + formatter.format(cal.getTime()));
        //logger.info("Started getColumnsJson Method");
        String columnJSON;
        List<String> columnjsons = new ArrayList();
        try {
            if (columns != null && !columns.isEmpty()) {
                for (int i = 0; i < columns.size(); i++) {
                    SMColumn column = columns.get(i);
                    ColumnBean columnBean = setColumnProperties(column);
                    columnJSON = commonUtil.mapSMObjectToJSON(columnBean);
                    columnJSON = commonUtil.modifyAuditDateFormats(columnJSON);
                    columnjsons.add(columnJSON);
                    //columnsJSON = commonUtil.mapSMObjectToJSON(columns);
                }
            }
        } catch (Exception ex) {
            logger.error("Error in getColumnsJsonWithoutExtProp:\n", ex);
        }
        cal.setTimeInMillis(System.currentTimeMillis());
        //logger.info("Ending Time :" + formatter.format(cal.getTime()));
        //logger.info("Ended getColumnsJson method");
        return columnjsons.toString();
    }
    
    private List<KeyValueBean> getModifiedExtProperties(List<KeyValue> extendedProperties, int length) {
        List<KeyValueBean> extProp = new ArrayList();
        for (int i = 0; i < length; i++) {
            KeyValueBean extBean = new KeyValueBean();
            extBean.setKey(extendedProperties.get(i).getKey());
            extBean.setAuditHistory(extendedProperties.get(i).getAuditHistory());
            extBean.setKeyValues(extendedProperties.get(i).getKeyValues());
            extBean.setObjectTypeId(extendedProperties.get(i).getObjectTypeId());
            Map<String, Object> userData = extendedProperties.get(i).getUserData().entrySet().stream()
                    .filter(map -> userdata.contains(map.getKey()))
                    .collect(Collectors.toMap(map -> map.getKey(), map -> map.getValue()));
            extBean.setUserData(userData);
            extBean.setValue(extendedProperties.get(i).getValue());
            extProp.add(extBean);
        }
        return extProp;
    }

    private EnvironmentBean setEnvironmentProperties(SMEnvironment environment) {
        EnvironmentBean mmEnv = new EnvironmentBean();
        mmEnv.setSystemName(environment.getSystemName());
        mmEnv.setSystemId(environment.getSystemId());
        mmEnv.setSystemEnvironmentName(environment.getSystemEnvironmentName());
        mmEnv.setEnvironmentId(environment.getEnvironmentId());
        mmEnv.setEnvironmentType(environment.getEnvironmentType());
        mmEnv.setEnvironmentNotes(environment.getEnvironmentNotes());
        mmEnv.setAuditHistory(environment.getAuditHistory());
        mmEnv.setDatabaseName(environment.getDatabaseName());
        mmEnv.setDatabaseType(environment.getDatabaseType());
        mmEnv.setDatabasePort(environment.getDatabasePort());
        mmEnv.setDatabaseDriver(environment.getDatabaseDriver());
        mmEnv.setDatabaseIPAddress(environment.getDatabaseIPAddress());
        mmEnv.setDatabaseURL(environment.getDatabaseURL());
        mmEnv.setDataStewardName(environment.getDataStewardName());
        mmEnv.setDatabaseInstanceSchema(environment.getDatabaseInstanceSchema());
        mmEnv.setSdiClassificationName(environment.getSdiClassificationName());
        mmEnv.setSdiFlag(environment.isSdiFlag());
        mmEnv.setSdiDescription(environment.getSdiDescription());
        mmEnv.setStatus(environment.getStatus());

        return mmEnv;
    }

    private TableBean setTableProperties(SMTable table) {
        TableBean tableBean = new TableBean();
        tableBean.setTableName(table.getTableName());
        tableBean.setTableId(table.getTableId());
        tableBean.setTableDefinition(table.getTableDefinition());
        tableBean.setDataStewardName(table.getDataStewardName());
        tableBean.setAuditHistory(table.getAuditHistory());
        tableBean.setEnvironmentId(table.getEnvironmentId());
        tableBean.setLogicalTableName(table.getLogicalTableName());
        tableBean.setPhysicalTableName(table.getPhysicalTableName());
        tableBean.setSchemaName(table.getSchemaName());
        tableBean.setSdiClassificationName(table.getSdiClassificationName());
        tableBean.setSdiDescription(table.getSdiDescription());
        tableBean.setSdiFlag(table.isSdiFlag());
        tableBean.setSystemEnvironmentName(table.getSystemEnvironmentName());
        tableBean.setTableComments(table.getTableComments());
        return tableBean;
    }
    private ColumnBean setColumnProperties(SMColumn column) {
        ColumnBean columnBean = new ColumnBean();
        columnBean.setAuditHistory(column.getAuditHistory());
        columnBean.setColumnComments(column.getColumnComments());
        columnBean.setColumnDatatype(column.getColumnDatatype());
        columnBean.setColumnDefinition(column.getColumnDefinition());
        columnBean.setColumnId(column.getColumnId());
        columnBean.setColumnLength(column.getColumnLength());
        columnBean.setColumnName(column.getColumnName());
        columnBean.setColumnNullableFlag(column.isColumnNullableFlag());
        columnBean.setColumnPrecision(column.getColumnPrecision());
        columnBean.setColumnScale(column.getColumnScale());
        columnBean.setDataStewardName(column.getDataStewardName());
        columnBean.setForeignKeyColumnName(column.getForeignKeyColumnName());
        columnBean.setForeignKeyTableName(column.getForeignKeyTableName());
        columnBean.setLogicalColumnName(column.getLogicalColumnName());
        columnBean.setPhysicalColumnName(column.getPhysicalColumnName());
        columnBean.setPrimaryKeyFlag(column.isPrimaryKeyFlag());
        columnBean.setSdidescription(column.getSDIDescription());
        columnBean.setSdiflag(column.isSDIFlag());
        columnBean.setTableId(column.getTableId());
        columnBean.setTableName(column.getTableName());
        columnBean.setValidValues(column.getValidValues());
        return columnBean;
    }

    private List<SMTable> getSelectedTables(List<SMTable> tables, List<Integer> selectedTbls) {
        List<SMTable> selTables = null;
        try {
            selTables = new ArrayList();
            selTables = tables.stream()
                    .filter(table -> selectedTbls.contains(table.getTableId()))
                    .collect(Collectors.toList());
            if(selTables.isEmpty()){
                selTables = tables;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return selTables;
    }
    
    
    /*public String getEnvironmentJSONSFile(SystemManagerUtil systemManagerUtil, KeyValueUtil keyValueUtil,
            Map<Integer, String> metadataSelection,List<Integer> selectedTbls,boolean isExtendedProp) throws JsonProcessingException, JSONException {
        logger.addAppender(appender);
        commonUtil.setLogAppender();
        cal.setTimeInMillis(System.currentTimeMillis());
        logger.info("::Method_Name::getEnvironmentJSONSFile::Starting Time :" + formatter.format(cal.getTime()));
        Map<Integer, SMEnvironment> environmentObjects = new HashMap();
        Map<String, String> sysEnvJSONMap = null;
        String predefinedPath = "C:\\MappingManager"; //Need to modify
        ArrayList<String> objArrayList = new ArrayList<String>();
       
        RequestStatus requestStatus;
        try {
             //START : Fetching xml file by using exportEnvironmentMethod
             if (metadataSelection != null) {
                for (int environmentId : metadataSelection.keySet()) {
                    requestStatus = systemManagerUtil.exportEnvironment(environmentId, APIConstants.FileExtension.XML);
                    objArrayList.add(predefinedPath+"\\"+requestStatus.getUserObject());
                    logger.info("::requestStatus::"+requestStatus.getUserObject());
                }
            }
             //END : Fetching xml file by using exportEnvironmentMethod
             
             //START : Convert XML to JSON file and adding extra fields
             //JSONObject json = XML.toJSONObject(xml2String);
             //String jsonString = json.toString();
             //System.out.println(jsonString);
             //File file=new File("D://json1.json");
              //FileUtils.writeStringToFile(file,jsonString);
             //END : Convert XML to JSON file and adding extra fields
             
             
             //START : Uploading XML file to Azure
             
              //START : Uploading XML file to Azure
             
  
        } catch (Exception ex) {
            logger.error("Error occured in getEnvironmentJSONSMap method :\n", ex);
        }
        cal.setTimeInMillis(System.currentTimeMillis());
        logger.info("Method_Name::getEnvironmentJSONSMap::+Ending Time :" + formatter.format(cal.getTime()));
        //logger.info("Ended getEnvironmentJSONSMap method");
        return "sysEnvJSONMap";
    }*/
    
    
    
}
