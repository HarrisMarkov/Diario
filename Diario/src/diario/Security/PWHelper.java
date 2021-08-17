/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diario.Security;

/**
 *
 * @author pedrocavaleiro
 */
public class PWHelper {
    public boolean res;
    public byte[] hash;
        
    public PWHelper(boolean result, byte[] hash) {
        this.res = result;
        this.hash = hash;
    }
        
}
