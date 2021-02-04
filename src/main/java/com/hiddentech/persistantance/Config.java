package com.hiddentech.persistantance;

import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

public class Config {

    private final String name;
    private final File file;
    private final FileConfiguration fileConfiguration;
    private int ID;
    public Config(String name, File file, FileConfiguration fileConfiguration){

        this.name = name;
        this.file = file;
        this.fileConfiguration = fileConfiguration;
        //set to zero since its not been cached yet
        this.ID = 0;
        //cache ID value
        this.ID = getID();
    }
    public String getName() {
        return name;
    }

    public File getFile() {
        return file;
    }

    public FileConfiguration getFileConfiguration() {
        return fileConfiguration;
    }

    public int getNextID(){
        setID(getID() + 1);
        return this.ID;
    }
    public int getID(){
        //checks if the id is loaded into memory
        if(this.ID ==0){
            //checks if the config even has an id
            if(this.fileConfiguration.contains("id")){
            this.ID =this.fileConfiguration.getInt("id");
            //check if the config also had 0 for its ID and update it if so
            if(this.ID== 0){ setID(1); }
            }else {
                //adds ID to the config
                setID(1);
            }
        }
        return this.ID;
    }

    public void setID(int id) {
        fileConfiguration.set("id",id);
        Persistence.getConfigHandler().saveFile(this);
        this.ID = id;
    }
}
