package com.itsanoj;

import java.io.*;

public class Config implements Serializable {

    private String lastNickname;


    public Config(){
    }

    public String getLastNickname() {
        return lastNickname;
    }

    public void setLastNickname(String lastNickname) {
        this.lastNickname = lastNickname;
    }

    public static Config loadConfig(File configFile) throws IOException, ClassNotFoundException {
        try(ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(configFile)))){
            return (Config) ois.readObject();
        }
    }

    public boolean saveConfig(File configFile){
        try(ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(configFile)))) {
            oos.writeObject(this);
            return true;
        }catch (Exception e){
            System.out.println("[Config] Error while saving config...");
            System.out.println("[Config] Did not save the config");
            return false;
        }
    }

}
