/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.erwin.cfx.connectors.adls;

//import com.erwin.cfx.adls.localfiles.ADLSUtils;
import com.erwin.cfx.connectors.adls.emm.utils.CommonUtil;
import static com.erwin.cfx.connectors.adls.emm.utils.CommonUtil.cal;
import static com.erwin.cfx.connectors.adls.emm.utils.CommonUtil.formatter;
import com.microsoft.azure.storage.CloudStorageAccount; 
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.BlobListingDetails;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.EnumSet;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;


/**
 *
 * @author SMastanV
 */
public class ADLSFilesUploader {
    
private static final Logger logger = Logger.getLogger(ADLSFilesUploader.class);
public static FileAppender  appender = new FileAppender();
CommonUtil commonUtil = CommonUtil.getInstance(); 
StringBuffer sb = new StringBuffer();


/**
 * uploads files to adls
 * @param sysName
 * @param envName
 * @param jsonData
 * @param container
 * @param modeofUpload
 * @param parentFolder
 * @return
 * @throws URISyntaxException
 * @throws StorageException
 * @throws IOException 
 */
    private static String upload(String sysName, String envName, String jsonData, CloudBlobContainer container, String modeofUpload, String parentFolder,StringBuffer sb) throws URISyntaxException, StorageException, IOException {
    CommonUtil.cal.setTimeInMillis(System.currentTimeMillis());
    
     logger.info("Method_Name :: upload :: Starting Time :" + formatter.format(cal.getTime()));
     sb.append("Method_Name :: upload :: Starting Time :" + formatter.format(cal.getTime())+"\n");
        
    StringBuilder blobName = new StringBuilder();
    CommonUtil.cal.setTimeInMillis(System.currentTimeMillis());
    String timestamp = CommonUtil.formatter.format(CommonUtil.cal.getTime());
    
    long start;
    long end;
    long executionTimeinSec; 
    start = System.nanoTime();
    
    if (!parentFolder.isEmpty()) {
      blobName.append(parentFolder);
      blobName.append(File.separator);
    } 
    blobName.append(sysName);
    blobName.append(File.separator);
    blobName.append(envName);
    blobName.append("_");
    blobName.append(timestamp);
    blobName.append(".json");
    CloudBlockBlob blob = container.getBlockBlobReference(blobName.toString());
    blob.getName();
    if (!blob.exists()) {
      blob.uploadText(jsonData);
      //sathish         
      end = System.nanoTime();
      executionTimeinSec = TimeUnit.SECONDS.convert(end - start, TimeUnit.NANOSECONDS);
      logger.info("Method_Name::upload:END: Time taking for uploading the"+blobName+" JSON files to container :: "+ executionTimeinSec);
      sb.append("Method_Name::upload:END: Time taking for uploading the"+blobName+" JSON files to container ::"+ executionTimeinSec+"\n");
        
      return "File " + blobName + " Uploaded Successfully";
    } 
    if (modeofUpload.equalsIgnoreCase("TRUE")) {
      blob.uploadText(jsonData);
          
        end = System.nanoTime();
        executionTimeinSec = TimeUnit.SECONDS.convert(end - start, TimeUnit.NANOSECONDS);
        logger.info("Method_Name::upload: END : Time taking for uploading the"+blobName+" JSON files to container :: "+ executionTimeinSec);
        sb.append("Method_Name::upload: END : Time taking for uploading the"+blobName+" JSON files to container ::"+ executionTimeinSec+"\n");
  
      return "File " + blobName + " Overwritten Successfully";
    }  
    
    end = System.nanoTime();
    executionTimeinSec = TimeUnit.SECONDS.convert(end - start, TimeUnit.NANOSECONDS);
    logger.info("Method_Name::upload:END: Time taking for uploading the"+blobName+" JSON files to container :: "+ executionTimeinSec);
    sb.append("Method_Name::upload:END: Time taking for uploading the"+blobName+" JSON files to container ::"+ executionTimeinSec+"\n");
  
    return "File " + blobName + " is skipped";
  }
  
/**
 * Iterated environment json map and seperates system and environment
 * @param account
 * @param containerName
 * @param sysEnvMap
 * @param modeOfUpload
 * @param parentFolder
 * @return 
 */
    
public String uploadEnvObjFilesFromJSON(CloudStorageAccount account, String containerName, Map<String, String> sysEnvMap, String modeOfUpload, String parentFolder,String logFilePath) {
    CommonUtil.cal.setTimeInMillis(System.currentTimeMillis());
    
    logger.info("Method_Name::uploadEnvObjFilesFromJSON::Starting Time :" + formatter.format(cal.getTime())); 
    sb.append("Method_Name::uploadEnvObjFilesFromJSON::Starting Time :" + formatter.format(cal.getTime())+"\n");
    
    String result = "";
    CommonUtil commonUtil = CommonUtil.getInstance();
    long start;
    long end;
    long executionTimeinSec;
    
    try {
      CloudBlobClient serviceClient = account.createCloudBlobClient();
      CloudBlobContainer container = serviceClient.getContainerReference(containerName);
      container.createIfNotExists();
      start = System.nanoTime();
      String sysName = "";
      String envName = "";
      if (sysEnvMap != null && !sysEnvMap.isEmpty()) {
        for (Map.Entry<String, String> eachsysEnv : sysEnvMap.entrySet()) {
          if ((((String)eachsysEnv.getKey()).split(commonUtil.delimiter)).length > 0) {
            sysName = ((String)eachsysEnv.getKey()).split(commonUtil.delimiter)[0];
            envName = ((String)eachsysEnv.getKey()).split(commonUtil.delimiter)[1];
          } 
          result = result + "\n" + upload(sysName, envName, eachsysEnv.getValue(), container, modeOfUpload, parentFolder,sb);
        }  
           
        end = System.nanoTime();
            
        executionTimeinSec = TimeUnit.SECONDS.convert(end - start, TimeUnit.NANOSECONDS);
        logger.info("Method_Name::uploadEnvObjFilesFromJSON:: Time taking for uploading the JSON files to container :: "+ executionTimeinSec);
        sb.append("Method_Name::uploadEnvObjFilesFromJSON:: Time taking for uploading the JSON files to container :: "+ executionTimeinSec+"\n");
      }
    cal.setTimeInMillis(System.currentTimeMillis());
    logger.info("Method_Name::uploadEnvObjFilesFromJSON::Ending Time :" + formatter.format(cal.getTime()));
    sb.append("Method_Name::uploadEnvObjFilesFromJSON::Ending Time :" + formatter.format(cal.getTime())+"\n");
    logger.info(result);
    
    //START :Creating file and writing the logs
     FileUtils.writeStringToFile(new File(logFilePath + File.separator + "ActivityLog.txt"), sb.toString(),true);
    //END :Creating file and writing the logs
        
    } catch (URISyntaxException|StorageException|IOException ex) {
      logger.error("Error occured in uploadEnvObjFilesFromJSON method :\n"+ex.getMessage());
        sb.append("Error occured in uploadEnvObjFilesFromJSON method :\n"+ex.getMessage()+"\n");
      return ex.getMessage();
    } 
    CommonUtil.cal.setTimeInMillis(System.currentTimeMillis());
    logger.info("Ending Time :" + CommonUtil.formatter.format(CommonUtil.cal.getTime()));
    logger.info("Ended uploadEnvObjFilesFromJSON method");
    return result;
}       

/**
 * gets connection with adls gen2
 * @param storageConnectionString
 * @return
 * @throws URISyntaxException
 * @throws InvalidKeyException 
 */
    public CloudStorageAccount getADLSGen2Connection(String storageConnectionString,String logFilePath) throws URISyntaxException, InvalidKeyException {
        /*appender.setFile(logFilePath);
        appender.setName("ADLS ENVIRONMENT JSON UPLOADER LOGS");
        appender.setLayout(new PatternLayout("%d %-5p [%c{1}] %m%n"));
        appender.setThreshold(Level.ALL);
        appender.setAppend(true);
        appender.activateOptions();
        logger.addAppender(appender); */
    cal.setTimeInMillis(System.currentTimeMillis());
    //logger.info("Started getADLSGen2Connection method START time"+formatter.format(cal.getTime()));
    //logger.info("Starting Time :" + formatter.format(cal.getTime()));    
        CloudStorageAccount account = CloudStorageAccount.parse(storageConnectionString);
    //logger.info("Connected To ADLS Successfully !");
    cal.setTimeInMillis(System.currentTimeMillis());
    //logger.info("Ending Time :" + formatter.format(cal.getTime()));
    //logger.info("Ended getADLSGen2Connection method :: END time"+formatter.format(cal.getTime()));
        return account;
    }
    private static void copyBlobToOtherFolder(CloudBlobContainer container, String blobFolder) throws URISyntaxException, StorageException{
        String archieveBlobName = "Erwin\\Oracle\\Archive\\OracleDB.json";
       container.listBlobs(blobFolder, true, EnumSet.noneOf(BlobListingDetails.class), null, null).forEach(bloblist->{
           CloudBlockBlob blockblob = (CloudBlockBlob) bloblist;
           System.out.println(blockblob.getName());
       });
            
       
        
//       CloudBlockBlob archieveBlob = container.getBlockBlobReference(archieveBlobName);
//       archieveBlob.startCopy(blobObj);
//       blobObj.delete();
//        System.out.println(blobObj.getName());
    }
	public static void main(String[] args) throws URISyntaxException, InvalidKeyException, StorageException {
        ADLSFilesUploader adlsObj =  new ADLSFilesUploader();
        String storageConnectionString = "BlobEndpoint=https://erwindsgen2.blob.core.windows.net/;QueueEndpoint=https://erwindsgen2.queue.core.windows.net/;FileEndpoint=https://erwindsgen2.file.core.windows.net/;TableEndpoint=https://erwindsgen2.table.core.windows.net/;SharedAccessSignature=sv=2021-06-08&ss=bfqt&srt=sco&sp=rwdlacupitfx&se=2022-06-23T13:20:41Z&st=2022-06-23T05:20:41Z&spr=https&sig=K%2BJdvlzYkt1aizjKgljRUltbRjemC2w%2Bplm6tjWCI9k%3D";
        String containerName = "erwindemo";
        String blobFolder = "Erwin/Oracle/";
        CloudStorageAccount account = CloudStorageAccount.parse(storageConnectionString);
        CloudBlobClient serviceClient = account.createCloudBlobClient();
        CloudBlobContainer container = serviceClient.getContainerReference(containerName);
        adlsObj.copyBlobToOtherFolder(container,blobFolder);
    }
    
   /*  public String getEnvironmentJSONSFile(SystemManagerUtil systemManagerUtil, KeyValueUtil keyValueUtil,
            Map<Integer, String> metadataSelection,List<Integer> selectedTbls,boolean isExtendedProp,CloudStorageAccount account, String modeofUpload, String parentFolder) throws JsonProcessingException, JSONException {
        logger.addAppender(appender);
        commonUtil.setLogAppender();
        cal.setTimeInMillis(System.currentTimeMillis());
        logger.info("::Method_Name::getEnvironmentJSONSFile::Starting Time :" + formatter.format(cal.getTime()));
        Map<Integer, SMEnvironment> environmentObjects = new HashMap();
        //Map<String, String> sysEnvJSONMap = null;
        String predefinedPath = "C:\\MappingManager"; //Need to modify
        ArrayList<String> objArrayList = new ArrayList<String>();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
               
        StringBuilder blobName = new StringBuilder();
        StringBuffer blobFolder = new StringBuffer();
        String successMsg = "";
               
              
        RequestStatus requestStatus;
        try {
             //START : Fetching xml file by using exportEnvironmentMethod
             long start = System.nanoTime();
             if (metadataSelection != null) {
                for (int environmentId : metadataSelection.keySet()) {
                    requestStatus = systemManagerUtil.exportEnvironment(environmentId, APIConstants.FileExtension.XML);
                    objArrayList.add(predefinedPath+"\\"+requestStatus.getUserObject());
                    logger.info("::requestStatus::"+requestStatus.getUserObject());
                }
            }
            long end = System.nanoTime();
            long execution = end - start;
            long executionTimeinSec = TimeUnit.SECONDS.convert(execution, TimeUnit.NANOSECONDS);
            logger.info("::Execution time in nano seconds::" + executionTimeinSec);
            logger.info("Count of Environments::"+ objArrayList.size());
             
        CloudBlobClient serviceClient = account.createCloudBlobClient();
        CloudBlobContainer container = serviceClient.getContainerReference("erwindemo");
             //END : Fetching xml file by using exportEnvironmentMethod
             
             //START : Convert XML to JSON file and adding extra fields
             //JSONObject json = XML.toJSONObject(xml2String);
             //String jsonString = json.toString();
             //System.out.println(jsonString);
             //File file=new File("D://json1.json");
              //FileUtils.writeStringToFile(file,jsonString);
             //END : Convert XML to JSON file and adding extra fields
             
             
        //START : Uploading XML file to Azure
         start = System.nanoTime();
         for (String filePath : objArrayList) {
                        
                blobName = new StringBuilder();
                blobFolder = new StringBuffer();

                File xmlFile = new File(filePath);
                Reader fileReader = new FileReader(xmlFile);
                BufferedReader bufReader = new BufferedReader(fileReader);
                StringBuilder sb = new StringBuilder();
                String line = bufReader.readLine();
                while( line != null){ sb.append(line).append("\n");
                line = bufReader.readLine();
                }
                String xml2String = sb.toString();
                
                
                //Creating Folder and file name
                if (!parentFolder.isEmpty()) {
                    blobName.append(parentFolder);
                    blobName.append(File.separator);
                    blobFolder.append(parentFolder);
                    blobFolder.append("/");
                }
                //blobName.append(sysName);
                //blobFolder.append(sysName);
                //blobFolder.append("/");
                //blobName.append(File.separator);
                //blobName.append(envName);
                blobName.append(filePath);
                blobName.append("_");
                blobName.append(timestamp);
                blobName.append(".xml");
                
                CloudBlockBlob blob = container.getBlockBlobReference(blobName.toString());
                
                logger.info(":::blobName:::"+ blobName);

                blob.uploadText(xml2String);
                cal.setTimeInMillis(System.currentTimeMillis());
                logger.info("Ending Time :" + formatter.format(cal.getTime()));
                successMsg = successMsg+"\n File " + filePath + " Uploaded Successfully";
         }
         end = System.nanoTime();
         execution = end - start;
         logger.info("::Execution time for uploading xml file to container in Nano seconds::" + execution);
         //END : Uploading XML file to Azure
             
  
        } catch (Exception ex) {
            logger.error("Error occured in getEnvironmentJSONSMap method :\n", ex);
        }
        cal.setTimeInMillis(System.currentTimeMillis());
        logger.info("Method_Name::getEnvironmentJSONSMap::+Ending Time :" + formatter.format(cal.getTime()));
        //logger.info("Ended getEnvironmentJSONSMap method");
        return successMsg;
    } */
        
     
}
