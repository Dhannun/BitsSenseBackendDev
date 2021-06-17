/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.profile;

import alertmaker.AlertMaker;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import database.DatabaseHandler;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
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
import javax.swing.JOptionPane;
import ui.login.LoginController;

/**
 * FXML Controller class
 *
 * @author Wecom
 */
public class ProfileController implements Initializable {

    @FXML
    private JFXTextField surnameUpdate;
    @FXML
    private JFXTextField firstNameUpdate;
    @FXML
    private JFXDatePicker DOBUpdate;
    @FXML
    private JFXButton logout;
    @FXML
    private JFXButton update;
    @FXML
    private JFXButton cancel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
    }    


    @FXML
    private void handleLogout(ActionEvent event) {
        
        int action = JOptionPane.showConfirmDialog(null, "You Want to Logout??");
        
        if (action == JOptionPane.YES_OPTION) {
            DatabaseHandler.setEmail(null);
            
            try {
                Parent parent = FXMLLoader.load(getClass().getResource("/ui/login/Login.fxml"));
                Stage stage = new Stage(StageStyle.DECORATED);
                stage.setScene(new Scene(parent));
                stage.setTitle("Login Form");
                stage.show();

                ((Stage) logout.getScene().getWindow()).close();
            } catch (IOException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
    

    @FXML
    private void handleUpdate(ActionEvent event) {
        
        String query = "UPDATE USERS SET surname = ?, first_name = ?, dob = ? WHERE email = '"+DatabaseHandler.getEmail()+"'";
        String[] data = {surnameUpdate.getText(), firstNameUpdate.getText(), DOBUpdate.getValue().toString()};
        
        if (DatabaseHandler.execAction(query, data)) {
            AlertMaker.showInformation("Update Profile", "Profile Update Successful :-)");
            loadData();
        }
        
//        System.out.println(DOBUpdate.getValue().toString());
//DOBUpdate.setValue(LocalDate.parse("2021-03-23"));
//System.out.println(DOBUpdate.getValue().toString() != "");
//        DOBUpdate.setValue(LocalDate.parse("", DateTimeFormatter.ofPattern("yyyy-mm-dd")));
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        int action = JOptionPane.showConfirmDialog(null, "You Want to Close the App??");
        
        if (action == JOptionPane.YES_OPTION) {
            DatabaseHandler.setEmail(null);
            ((Stage) logout.getScene().getWindow()).close();
        }
    }

    @FXML
    private void loadProfile(ActionEvent event) {
       loadData();
    }

    private void loadData() {
         try {
            String query = "SELECT * FROM USERS WHERE email='"+DatabaseHandler.getEmail()+"'";
            ResultSet result = DatabaseHandler.execQuery(query);
            
            while (result.next()) {
                surnameUpdate.setText(result.getString("surname"));
                firstNameUpdate.setText(result.getString("first_name"));
                DOBUpdate.setValue(LocalDate.parse(result.getString("dob")));
                System.out.println("Surname: "+result.getString("surname"));
                System.out.println("First Name: "+result.getString("first_name"));
                System.out.println("DOB: "+result.getString("dob"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProfileController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
}
