/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.registration;

import alertmaker.AlertMaker;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import database.DatabaseHandler;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import mailer.JavaMailer;
import ui.login.LoginController;

/**
 * FXML Controller class
 *
 * @author Wecom
 */
public class RegisterController implements Initializable {

    @FXML
    private JFXTextField surname;
    @FXML
    private JFXTextField first_name;
    @FXML
    private JFXTextField email;
    @FXML
    private JFXTextField phone;
    @FXML
    private JFXDatePicker dob;
    @FXML
    private JFXTextField bvn;
    @FXML
    private JFXPasswordField password;
    @FXML
    private JFXPasswordField confirm_password;
    @FXML
    private JFXButton register;
    @FXML
    private JFXButton sign_in;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void handleRegister(ActionEvent event) {

//        handle register here
        if ("".equals(confirm_password.getText())
                || "".equals(surname.getText())
                || "".equals(first_name.getText())
                || "".equals(email.getText())
                || "".equals(phone.getText())
                || "".equals(bvn.getText())
                || "".equals(password.getText())) {
            AlertMaker.showError("Login Form", "All the Fiedlds are Required");
        } else {
            if (confirm_password.getText().equals(password.getText())) {

                String query = "INSERT INTO USERS(email, surname, first_name, phone, dob, bvn, password, status) VALUES (?,?,?,?,?,?,?,?)";
                Random rnd = new Random();
                int n = 100000 + rnd.nextInt(999999);
                String OTP = Integer.toString(n);
                
                
                String[] data = {email.getText(),
                    surname.getText(),
                    first_name.getText(),
                    phone.getText(),
                    dob.getValue().toString(),
                    bvn.getText(),
                    password.getText(),
                    OTP};

                if(JavaMailer.sendMail(email.getText(), OTP)) {
                    if (DatabaseHandler.execAction(query, data)) {
                        //                    JOptionPane.showMessageDialog(null, "Check your email for your Verification Code");
                    
                        AlertMaker.showInformation("Registration", "Check your email for your Verification Code");

                        //                 AlertMaker.showConfirm("Registration", "Check your email for your Verification Code");
                        //                if(action.get() == ButtonType.OK){
                        try {
                            Parent parent = FXMLLoader.load(getClass().getResource("/ui/verification/verify.fxml"));
                            Stage stage = new Stage(StageStyle.DECORATED);
                            stage.setScene(new Scene(parent));
                            stage.setTitle("Verification Form");
                            DatabaseHandler.setEmail(email.getText());
                            stage.show();

                            ((Stage) register.getScene().getWindow()).close();
                        } catch (IOException ex) {
                            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        //                }
                    }else{ 
                        AlertMaker.showError("Registration", "Please Check Your Email Address and Make Sure It's VAlid");
                    }
                }else{
//                    AlertMaker.showError("Registration", "");
                }
            }else{
                AlertMaker.showError("Registration", "Password Mismatch");
//                JOptionPane.showMessageDialog(null, "Password Mismatch");
            }
        }
    }

    @FXML
    private void handleSignIn(ActionEvent event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/ui/login/Login.fxml"));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setScene(new Scene(parent));
            stage.setTitle("Registration Form");
            stage.show();

            ((Stage) register.getScene().getWindow()).close();
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
