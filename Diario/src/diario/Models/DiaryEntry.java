/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diario.Models;

import java.util.Date;
import java.util.List;

/**
 *
 * @author pedrocavaleiro
 */
public class DiaryEntry {
    
    public String Comment;
    public List<Date> EntriesTime;
    public List<String> Entries;
    
    public void AddEntry(String Entiry) {
        EntriesTime.add(new Date());
        Entries.add(Entiry);
    }
    
}
