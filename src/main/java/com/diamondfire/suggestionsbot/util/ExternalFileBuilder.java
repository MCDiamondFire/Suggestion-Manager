package com.diamondfire.suggestionsbot.util;

import java.io.File;
import java.nio.file.Path;

public class ExternalFileBuilder {
    
    String fileName;
    String fileType = "unk";
    boolean directory = false;
    
    public ExternalFileBuilder setName(String fileName) {
        this.fileName = fileName;
        return this;
    }
    
    public ExternalFileBuilder setFileType(String fileType) {
        this.fileType = fileType;
        return this;
    }
    
    public ExternalFileBuilder isDirectory(boolean directory) {
        this.directory = directory;
        return this;
    }
    
    public Path buildFile() {
        File file = new File(fileName + (directory ? "" : "." + fileType));
        try {
            if (!file.exists()) {
                if (directory) {
                    file.mkdir();
                } else {
                    file.createNewFile();
                }
                
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file.toPath();
        
    }
}