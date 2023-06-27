package com.diamondfire.suggestionsbot.sys.externalfile;

import java.io.File;

public interface ExternalFiles {
    
    File OTHER_CACHE_DIR = new ExternalFileBuilder()
            .isDirectory(true)
            .setName("other_cache")
            .buildFile();
    
    File CONFIG = new ExternalFileBuilder()
            .isDirectory(false)
            .setName("config")
            .setFileType("json")
            .buildFile();
    
    File DISABLED_COMMANDS = new ExternalFileBuilder()
            .isDirectory(false)
            .setName("disabled_commands")
            .setFileType("txt")
            .buildFile();
}
