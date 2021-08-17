/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diario;

import diario.Models.UserModel;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import diario.Security.LoginDBCipher;
import diario.Security.PWHelper;
import diario.Security.PasswordSecure;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import java.util.Base64;

/**
 *
 * @author pedrocavaleiro
 */
public class LoginDB {
    
    public static PWHelper Login(String username, String password) throws InvalidKeySpecException, IOException {
        LoginDBCipher db = new LoginDBCipher();    
        try {
            List<UserModel> users = db.Decrypt();
            UserModel user = null;
            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).Username == null ? username == null : users.get(i).Username.equals(username)) {
                    user = users.get(i);
                    break;
                }
            }
            if (user != null)
                return PasswordSecure.isExpectedPassword(password.toCharArray(), Base64.getDecoder().decode(user.Salt), Base64.getDecoder().decode(user.Password));           
            return new PWHelper(false, null);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(LoginDB.class.getName()).log(Level.SEVERE, null, ex);
            return new PWHelper(false, null);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(LoginDB.class.getName()).log(Level.SEVERE, null, ex);
            return new PWHelper(false, null);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(LoginDB.class.getName()).log(Level.SEVERE, null, ex);
            return new PWHelper(false, null);
        } catch (InvalidAlgorithmParameterException ex) {
            Logger.getLogger(LoginDB.class.getName()).log(Level.SEVERE, null, ex);
            return new PWHelper(false, null);
        } catch (IOException ex) {
            Logger.getLogger(LoginDB.class.getName()).log(Level.SEVERE, null, ex);
            return new PWHelper(false, null);
        } catch (ShortBufferException ex) {
            Logger.getLogger(LoginDB.class.getName()).log(Level.SEVERE, null, ex);
            return new PWHelper(false, null);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(LoginDB.class.getName()).log(Level.SEVERE, null, ex);
            return new PWHelper(false, null);
        } catch (BadPaddingException ex) {
            Logger.getLogger(LoginDB.class.getName()).log(Level.SEVERE, null, ex);
            return new PWHelper(false, null);
        }
    }
    
    public static boolean Register(String username, String password) throws InvalidKeySpecException, IOException {
       
        byte[] salt = PasswordSecure.getNextSalt();
        byte[] hash = PasswordSecure.hash(password.toCharArray(), salt);
        UserModel user = new UserModel();
        user.Username = username;
        user.Password = Base64.getEncoder().encodeToString(hash);
        user.Salt = Base64.getEncoder().encodeToString(salt);
        
        LoginDBCipher db = new LoginDBCipher();    
        try {
            List<UserModel> users = db.Decrypt();
            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).Username.equals(username))
                    return false;
            }
            users.add(user);
            db.Encrypt(users);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(LoginDB.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(LoginDB.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (InvalidKeyException ex) {
            Logger.getLogger(LoginDB.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (InvalidAlgorithmParameterException ex) {
            Logger.getLogger(LoginDB.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (IOException ex) {
            Logger.getLogger(LoginDB.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (ShortBufferException ex) {
            Logger.getLogger(LoginDB.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(LoginDB.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (BadPaddingException ex) {
            Logger.getLogger(LoginDB.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
    
}
