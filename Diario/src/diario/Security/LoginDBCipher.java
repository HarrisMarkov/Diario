/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diario.Security;

import diario.Models.UserModel;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author pedrocavaleiro
 * These values will encrypt the database
 * AES 256
 * KEY: 667aea716351d70162b1349c14ea4b45045c324914a6f25afd280448bf21aeaf
 * IV:  8ae3b175fc7d9eb324cc7c8c4056e4cfe1a492bebe80ab7051f1731993af2360
 */
public class LoginDBCipher {
    
    private byte[] key;
    private byte[] iv;
    
    public LoginDBCipher() throws IOException {
        File f = new File("logindb.sdldb");
        if (!f.exists())
            f.createNewFile();
        this.key = unhexlify("c1a919119dac4b5ee68c2a17179336c7");
        this.iv = unhexlify("2753b8387a227fc3eb845dc879e95ae7");
    }
    
    public void Encrypt(List<UserModel> users) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IOException, ShortBufferException, IllegalBlockSizeException, BadPaddingException {
        SecretKeySpec key = new SecretKeySpec(this.key, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(this.iv);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        
        String db = ModelsToString(users);
        byte[] badb = db.getBytes();
        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
        byte[] encrypted= new byte[cipher.getOutputSize(badb.length)];
        int enc_len = cipher.update(badb, 0, badb.length, encrypted, 0);
        enc_len += cipher.doFinal(encrypted, enc_len);
        
        
        try (FileOutputStream fos = new FileOutputStream("logindb.sdldb")) {
            fos.write(encrypted);
        }       
    }
    
    private String ModelsToString(List<UserModel> users) {
        String db = "";
        for (int i = 0; i < users.size(); i++) {
            UserModel user = users.get(i);
            db = db + String.format("%s,%s,%s", user.Username, user.Password, user.Salt);
            if (i + 1 != users.size())
                db = db + "º";
        }
        return db;
    }
    
    public List<UserModel> Decrypt() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IOException, ShortBufferException, IllegalBlockSizeException, BadPaddingException {
        SecretKeySpec key = new SecretKeySpec(this.key, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(this.iv);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        
        Path fdb = Paths.get("logindb.sdldb");
        byte[] bdb = Files.readAllBytes(fdb);
        
        cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
        byte[] decrypted = new byte[cipher.getOutputSize(bdb.length)];
        int dec_len = cipher.update(bdb, 0, bdb.length, decrypted, 0);
        dec_len += cipher.doFinal(decrypted, dec_len);
        
        List<UserModel> users = new ArrayList<>();
        String sdb = new String(decrypted);
        if (sdb.trim().isEmpty() || sdb == null)
            return users;
        
        String[] sadb = sdb.trim().split("º");
        
        for (String sadb1 : sadb) {
            String[] sUser = sadb1.split(",");
            UserModel user = new UserModel();
            user.Username = sUser[0];
            user.Password = sUser[1];
            user.Salt = sUser[2];
            users.add(user);
        }
        return users;       
    }
    
    public static byte[] unhexlify(String ss) {
        int strlen = ss.length();
        if (strlen>0 && strlen%2!=0) {
            throw new RuntimeException("odd-length string");
        }
        byte[] ret = new byte[strlen/2];
        for (int i=0; i<strlen; i+=2) {
            int a = Character.digit(ss.charAt(i),0x10);
            int b = Character.digit(ss.charAt(i+1),0x10);
            if (a==-1 || b==-1) {
                throw new RuntimeException("non-hex digit");
            }
            ret[i/2] = (byte) ((a<<4)+b);
        }
        return ret;
    }
    
}
