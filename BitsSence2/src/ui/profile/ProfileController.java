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

    public void setSurnameUpdate(String newSurname) {
//        surnameUpdate.setText(newSurname);
        surnameUpdate.setText(newSurname);
    }

    public void setFirstNameUpdate(String newFirstName) {
        firstNameUpdate.setText(newFirstName);
//        this.firstNameUpdate = firstNameUpdate;
    }

    public void setDOBUpdate(String newDOB) {
        DOBUpdate.setValue(LocalDate.parse(newDOB));
//        this.DOBUpdate = DOBUpdate;
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

        String query = "UPDATE USERS SET surname = ?, first_name = ?, dob = ? WHERE email = '" + DatabaseHandler.getEmail() + "'";
        String[] data = {surnameUpdate.getText(), firstNameUpdate.getText(), DOBUpdate.getValue().toString()};

        if (DatabaseHandler.execAction(query, data)) {
            AlertMaker.showInformation("Update Profile", "Profile Update Successful :-)");
            loadData();
        }

    }

    @FXML
    private void handleCancel(ActionEvent event) {
        int action = JOptionPane.showConfirmDialog(null, "You Want to Close the App??");

        if (action == JOptionPane.YES_OPTION) {
            DatabaseHandler.setEmail(null);
            ((Stage) logout.getScene().getWindow()).close();
        }
    }

    private void loadData() {
        try {
            String query = "SELECT * FROM USERS WHERE email='" + DatabaseHandler.getEmail() + "'";
            ResultSet result = DatabaseHandler.execQuery(query);

            while (result.next()) {
                surnameUpdate.setText(result.getString("surname"));
                firstNameUpdate.setText(result.getString("first_name"));
                DOBUpdate.setValue(LocalDate.parse(result.getString("dob")));
                System.out.println("Surname: " + result.getString("surname"));
                System.out.println("First Name: " + result.getString("first_name"));
                System.out.println("DOB: " + result.getString("dob"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProfileController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void viewProfile(ActionEvent event) {
        try {
            String qu = "SELECT * FROM USERS WHERE email='" + DatabaseHandler.getEmail() + "'";
            ResultSet rs = DatabaseHandler.execQuery(qu);
            if (rs.next()) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/profile/setails.fxml"));
                Parent parent = loader.load();

                Stage stage = new Stage(StageStyle.DECORATED);
                stage.setScene(new Scene(parent));
                stage.setTitle("Update Profile");

                SetailsController controller = loader.getController();
                controller.setDob(rs.getString("dob"));
                controller.setFullName(rs.getString("surname") + " " + rs.getString("first_name"));
                controller.setEmail(rs.getString("email"));
                controller.setPhone(rs.getString("phone"));
                controller.setPassword(rs.getString("password"));
                controller.setBvn(rs.getString("bvn"));
                controller.setStatus(rs.getString("status"));

                stage.show();

            }
        } catch (Exception ex) {
            Logger.getLogger(ProfileController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
