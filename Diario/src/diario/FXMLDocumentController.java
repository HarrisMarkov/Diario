/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diario;

import com.jfoenix.animation.alert.JFXAlertAnimation;
import com.jfoenix.controls.*;
import diario.Security.PWHelper;
import java.io.IOException;
import java.net.URL;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author harri
 */
public class FXMLDocumentController implements Initializable {     
    
    private boolean LoginActive = true;
   
    @FXML
    private JFXTextField user_textfield;
    
    @FXML
    private JFXPasswordField pass_textfield;      
    
    @FXML
    private AnchorPane loginPane;
    
    @FXML
    private AnchorPane registerPane;
    
    @FXML
    private JFXButton regButton;
    
    @FXML
    private JFXTextField regUserField;
    
    @FXML
    private JFXPasswordField regPassField;
    
    @FXML
    private JFXPasswordField regVerifyPassField;
    
    @FXML
    private void switcherButton(ActionEvent event) {
        LoginActive = (LoginActive) ? false : true;
        loginPane.visibleProperty().set(LoginActive);
        registerPane.visibleProperty().set(!LoginActive);
        if (LoginActive)
            regButton.setText("Registar");
        else
            regButton.setText("Iniciar Sessão");
            
    }
    
    private Button leftButton;
    

    public Node build(JFXAlert alert) {
        leftButton = new JFXButton("OK");
        leftButton.setLayoutX(50);
        leftButton.setLayoutY(50);
        leftButton.setOnAction(action->alert.close());
        return new Group(leftButton);
    }
    
    private void showDialog(String header, String body) {
        JFXDialogLayout layout = new JFXDialogLayout();
        Label l = new Label(body);
        l.setFont(new Font(15));
        layout.setBody(l);
        l = new Label(header);
        l.setFont(new Font(20));
        layout.setHeading(l);
        
        JFXAlert<Void> alert = new JFXAlert<>(Diario.loginStage);
        alert.setOverlayClose(true);
        alert.setAnimation(JFXAlertAnimation.CENTER_ANIMATION);
        layout.setActions(build(alert));
        alert.setContent(layout);
        alert.initModality(Modality.NONE);
        alert.showAndWait();
    }
    
    @FXML
    private void handleRegister(ActionEvent event) throws InvalidKeySpecException, IOException {
        if ("".equals(regUserField.getText())) {
            showDialog("Erro ao registar", "Introduza um nome de utilizador.");
            return;
        }
        if ("".equals(regPassField.getText())) {
            showDialog("Erro ao registar", "Introduza uma password.");
            return;
        }
        if ("".equals(regVerifyPassField.getText())) {
            showDialog("Erro ao registar", "Confirme a password.");
            return;
        }
        if (regPassField.getText() == null ? regVerifyPassField.getText() != null : !regPassField.getText().equals(regVerifyPassField.getText())) {
            showDialog("Erro ao registar", "As passwords não combinam!");
            return;
        }
        
        if (regUserField.getText().contains(",")) {
            showDialog("Erro ao registar", "O nome de utilizador contém caracteres inválidos");
            return;
        }
        
        boolean valid = LoginDB.Register(regUserField.getText(), regPassField.getText());
        if (valid) {
            showDialog("Utilizador registado", "O utilizador " + regUserField.getText() + " foi registado com sucesso\nPode agora efetuar o login.");
            switcherButton(null);
            user_textfield.setText(regUserField.getText());
            regUserField.setText("");
            regPassField.setText("");
            regVerifyPassField.setText("");                    
        } else
            showDialog("Erro ao registar", "O utilizador já existe!\nEscolha um novo nome de utilizador.");        
        
    }
    
    @FXML
    private void handleLogin(ActionEvent event) throws IOException, InvalidKeySpecException {
        
        if ("".equals(user_textfield.getText())) {
            showDialog("Erro ao efetuar o login", "Introduza o nome de utilizador");
            return;
        }
        if ("".equals(pass_textfield.getText())) {
            showDialog("Erro ao efetuar o login", "Introduza a password");
            return;
        }
        
        PWHelper valid = LoginDB.Login(user_textfield.getText(), pass_textfield.getText());
        
        if (valid.res) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLMainPage.fxml"));
            Parent newRoot = loader.load();
            FXMLMainPageController controller = loader.<FXMLMainPageController>getController();
            controller.initParams(valid.hash, user_textfield.getText());
            regUserField.getScene().getWindow().hide();
            Stage stage = new Stage();
            stage.setMinHeight(533);
            stage.setMinWidth(941);
            Scene scene = new Scene(newRoot);
            stage.setScene(scene);
            Diario.mainStage = stage;
            stage.show();    
        } else
            showDialog("Erro ao efetuar o login", "Utilizador não encontrado ou a password é inválida");        
        
    }
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
}
