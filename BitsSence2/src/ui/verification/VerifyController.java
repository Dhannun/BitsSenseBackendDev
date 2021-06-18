/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.verification;

import alertmaker.AlertMaker;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import database.DatabaseHandler;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
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
public class VerifyController implements Initializable {

    @FXML
    private JFXTextField opt_text;
    @FXML
    private JFXButton resendOTP;
    @FXML
    private JFXButton verify;

    private String OTP, name;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

    }

    @FXML
    private void handleResendOTP(ActionEvent event) {
        String qu = "SELECT * FROM USERS WHERE email='" + DatabaseHandler.getEmail() + "'";
        ResultSet rs = DatabaseHandler.execQuery(qu);

        try {
            while (rs.next()) {
                OTP = rs.getString("status");
                if (JavaMailer.sendMail(DatabaseHandler.getEmail(), OTP)) {
                    AlertMaker.showInformation("Verification Code", "Check your email\nyour Verification Code was Resend");
                }
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }

//        opt_text.setText(DatabaseHandler.getEmail());
        System.out.println(DatabaseHandler.getEmail());
    }

    @FXML
    private void handleVerification(ActionEvent event) {

        if (!"".equals(opt_text.getText())) {
            String qu = "SELECT * FROM USERS WHERE email='" + DatabaseHandler.getEmail() + "'";
            ResultSet rs = DatabaseHandler.execQuery(qu);

            try {
                while (rs.next()) {
                    OTP = rs.getString("status");
                    name = rs.getString("surname") + " " + rs.getString("first_name");
                }
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }

            System.out.println("OTP: " + OTP);
            System.out.println("Name: " + name);

            if (opt_text.getText().equals(OTP)) {

                String query = "UPDATE USERS SET status = \'verified\' WHERE email = ?";

                String[] data = {DatabaseHandler.getEmail()};

                if (DatabaseHandler.execAction(query, data)) {
                    AlertMaker.showInformation("Verification", "VERIFIED\n" + name + ", Your Details are now Virified");

                    try {
                        Parent parent = FXMLLoader.load(getClass().getResource("/ui/login/Login.fxml"));
                        Stage stage = new Stage(StageStyle.DECORATED);
                        stage.setScene(new Scene(parent));
                        stage.setTitle("Login Form");
                        stage.show();

                        ((Stage) verify.getScene().getWindow()).close();
                    } catch (IOException ex) {
                        Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    AlertMaker.showError("Verification", "There is a Probelm in Updating Your Status");
                }
            } else {
                AlertMaker.showError("Verification", "Oops! Invalid Verification Code.\n Please Try Again");
            }
        } else {
            AlertMaker.showError("Verification", "Empty Input");
        }

        // After verified
    }

}
