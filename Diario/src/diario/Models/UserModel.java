/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diario.Models;

/**
 *
 * @author pedrocavaleiro
 */
public class UserModel {
    public String Username;
    public String Password; // To load from db only during the login process
    public String Salt;     // To load from db only during the login process
}
