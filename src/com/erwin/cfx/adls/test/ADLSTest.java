///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
package com.erwin.cfx.adls.test;
//
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.List;
//
///**
// *
// * @author SMastanV
// */
public class ADLSTest {
// private static final String path = "C:\\MappingManager_11_0\\CATFX\\CATS\\39\\Files\\ADLSGen2\\";
//private static final String storageConnectionString = "BlobEndpoint=https://erwindsgen2.blob.core.windows.net/;QueueEndpoint=https://erwindsgen2.queue.core.windows.net/;FileEndpoint=https://erwindsgen2.file.core.windows.net/;TableEndpoint=https://erwindsgen2.table.core.windows.net/;SharedAccessSignature=sv=2020-08-04&ss=bfqt&srt=sco&sp=rwdlacupitfx&se=2022-03-23T15:08:07Z&st=2022-03-23T07:08:07Z&spr=https&sig=7wIf2swJZ935e%2B5PbBO7xqsVMjq%2BGaVPISefX33oOqU%3D";
//private static final String containerName = "erwinadls";
private static CloudBlobClient serviceClient;
//
//    private static String upload(String sysName , String envName, String jsonData) throws InvalidKeyException, URISyntaxException, StorageException, IOException {
//    // Container name must be lower case.
//    CloudBlobContainer container = serviceClient.getContainerReference(containerName);
//    container.createIfNotExists();
//    String blobName = sysName+"\\"+envName+".json";
//    System.out.println(blobName);
//    CloudBlockBlob blob = container.getBlockBlobReference(blobName);
//    if (blob.exists() == false) {
//        blob.uploadText(jsonData);
//        return "File "+blobName+" Uploaded Successfully";
//    } else {
//        return "File " + blobName + " Already exist in storage";
//    }
//}
//    private static String upload(File file,String localUploadFolderPath,String containerName) throws InvalidKeyException, URISyntaxException, StorageException, IOException {
//    // Container name must be lower case.
//    CloudBlobContainer container = serviceClient.getContainerReference(containerName);
//    container.createIfNotExists();
//    String blobName = file.getAbsolutePath().replace(localUploadFolderPath, "");
//    System.out.println(blobName);
//    CloudBlockBlob blob = container.getBlockBlobReference(blobName);
//    if (blob.exists() == false) {
//        blob.uploadFromFile(file.getAbsolutePath());
//        return "File "+file.getAbsolutePath()+" Uploaded Successfully";
//    } else {
//        return "File " + file.getAbsolutePath() + " Already exist in storage";
//    }
//}
//    public static String uploadEnvObjFilesFromJSON(String localUploadFolderpath ,String storageConnectionString,String containerName,List<String> sysJSONList) throws URISyntaxException, InvalidKeyException, StorageException, IOException{
//    CloudStorageAccount account = CloudStorageAccount.parse(storageConnectionString);
//    serviceClient = account.createCloudBlobClient();
//   // createAndStoreFilesInLocal(sysJSONList,localUploadFolderpath);
//    String result = "";
//    File source = new File(localUploadFolderpath);
//    for (File fileOrDir : source.listFiles()) {
//        boolean isFile = fileOrDir.isFile();
//        if(isFile) {
//            result = result +"\n"+upload(fileOrDir,localUploadFolderpath,containerName);
//        } else {
//            for(File file: fileOrDir.listFiles()) {
//                result = result +"\n"+upload(file,localUploadFolderpath,containerName);
//            }
//        }
//
//    }
    public static void main(String[] args) throws URISyntaxException, StorageException, InvalidKeyException, IOException {
        String ConnectionString = "DefaultEndpointsProtocol=https;AccountName=account-name;AccountKey=account-key";
        CloudStorageAccount account = CloudStorageAccount.parse(ConnectionString);
        serviceClient = account.createCloudBlobClient();
        //upload("demo","folder","This is sample data");
    }
}
