package com.rnziparchive;

import android.os.Environment;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.UnzipParameters;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import org.json.JSONObject;

import java.io.File;

/**
 * Created by ram on 10/01/17.
 */

public class Zip4jArchive {
    /**
     * unzip password protected zip files
     *
     * @param compressedFile
     * @param destinationPath
     */
    public static JSONObject unzip(File compressedFile, String destinationPath, String password) {
        JSONObject jsonObject = new JSONObject();
        boolean isSuccess = true;
        String response = "";
        try {
            ZipFile zipFile = new ZipFile(compressedFile);
            if (zipFile.isEncrypted()) {
                zipFile.setPassword(password);
            }
            zipFile.extractAll(destinationPath);
            compressedFile.delete();
            isSuccess = true;
            response = destinationPath;
        } catch (Exception e) {
            e.printStackTrace();
            isSuccess = false;
            response = e.getMessage();
        }
        try {
            jsonObject.put("isSuccess", isSuccess);
            jsonObject.put("response", response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * @param sourcePath
     * @param destPathWithName
     */
    public static JSONObject zip(String sourcePath, String destPathWithName, String password) {
        JSONObject jsonObject = new JSONObject();
        try {
            File sourceFolder = new File(sourcePath);
            ZipFile zipFile = new ZipFile(destPathWithName);
            ZipParameters parameters = new ZipParameters();
            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
            parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
            if (password != null && !password.trim().isEmpty()) {
                parameters.setEncryptFiles(true);
                parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD);
                parameters.setPassword(password);
            }
            if (sourceFolder != null) {
                File[] sourceFiles = sourceFolder.listFiles();
                for (File sourceFile : sourceFiles) {
                    if (sourceFile.isFile()) {
                        zipFile.addFile(sourceFile, parameters);
                    } else {
                        zipFile.addFolder(sourceFile, parameters);
                    }
                }
                jsonObject.put("isSuccess", true);
                jsonObject.put("response", "success");
            } else {
                jsonObject.put("isSuccess", false);
                jsonObject.put("response", "Source folder not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                jsonObject.put("isSuccess", false);
                jsonObject.put("response", "Error.");
            } catch (Exception e1) {

            }

        }
        return jsonObject;
    }
}