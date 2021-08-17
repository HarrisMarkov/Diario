/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diario;

import diario.Models.DiaryEntry;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author pedrocavaleiro
 */
public class DiaryEntries {
    
    private byte[] password;
    private String username;
    
    public DiaryEntries(String username, byte[] password) {
        this.password = password;
        this.username = username;
    }
    
    /*public List<DiaryEntry> GetEntries() {
        File curDir = new File(".");
        List<File> entries = getAllFiles(curDir);
        
    }*/
    
    public List<String> getAvailableDates() {
        List<String> availableDates = new ArrayList<String>();
        File curDir = new File(".");
        List<File> entries = getAllFiles(curDir);
        for (int i = 0; i < entries.size(); i++) {
            String[] fileName = entries.get(i).getName().split("_");
            if (fileName[0].equals(username))
                availableDates.add(fileName[1].split(".")[0]);
        }
        return availableDates;
    }
    
    private static List<File> getAllFiles(File curDir) {
        List<File> entries = new ArrayList<File>();
        File[] filesList = curDir.listFiles();
        for(File f : filesList)
        {
            if(f.isFile())
                if(f.getName().endsWith(".sde"))
                    entries.add(f);
        }
        return entries;
    }
    
}
