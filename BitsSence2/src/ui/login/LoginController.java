package ui.login;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import alertmaker.AlertMaker;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
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
import org.apache.commons.codec.digest.DigestUtils;
import ui.profile.ProfileController;

/**
 *
 * @author Wecom
 */
public class LoginController implements Initializable {

    @FXML
    private JFXTextField email;
    @FXML
    private JFXPasswordField password;
    @FXML
    private JFXButton login;
    @FXML
    private JFXButton register;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        DatabaseHandler.getIstance();
    }

    @FXML
    private void handleLogin(ActionEvent event) {

        try {
            String qu = "SELECT * FROM USERS WHERE email='" + email.getText() + "'";
            String qu2 = "SELECT * FROM USERS WHERE phone='" + email.getText() + "'";
            ResultSet rs = DatabaseHandler.execQuery(qu);
            ResultSet rs2 = DatabaseHandler.execQuery(qu2);
            if (rs.next()) {
                if (rs.getString("password").equals(DigestUtils.sha1Hex(password.getText()))) {
//                    email.setText(rs.getString("status"));
                    if ("verified".equals(rs.getString("status"))) {
                        AlertMaker.showInformation("Login", "Valid Details");

                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/profile/profile.fxml"));
                        Parent parent = loader.load();

                        Stage stage = new Stage(StageStyle.DECORATED);
                        stage.setScene(new Scene(parent));
                        stage.setTitle("Update Profile");

                        ProfileController controller = loader.getController();
                        controller.setDOBUpdate(rs.getString("dob"));
                        controller.setFirstNameUpdate(rs.getString("first_name"));
                        controller.setSurnameUpdate(rs.getString("surname"));

                        DatabaseHandler.setEmail(email.getText());

                        stage.show();

                        ((Stage) login.getScene().getWindow()).close();

                    } else {
                        AlertMaker.showError("Login", "Your Account is not yet Verrified!\nPlease Check your email for your Verification Code");

                        Parent parent = FXMLLoader.load(getClass().getResource("/ui/verification/verify.fxml"));
                        Stage stage = new Stage(StageStyle.DECORATED);
                        stage.setScene(new Scene(parent));
                        stage.setTitle("Verification Form");
                        DatabaseHandler.setEmail(email.getText());
                        stage.show();

                        ((Stage) login.getScene().getWindow()).close();
                    }
                } else {
                    AlertMaker.showError("Login", "Incorrect Password");
                }
            } else if (rs2.next()) {
                if (rs2.getString("password").equals(DigestUtils.sha1Hex(password.getText()))) {
//                    email.setText(rs.getString("status"));
                    if ("verified".equals(rs2.getString("status"))) {
                        AlertMaker.showInformation("Login", "Valid Details");

                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/profile/profile.fxml"));
                        Parent parent = loader.load();

                        Stage stage = new Stage(StageStyle.DECORATED);
                        stage.setScene(new Scene(parent));
                        stage.setTitle("Update Profile");

                        ProfileController controller = loader.getController();
                        controller.setDOBUpdate(rs2.getString("dob"));
                        controller.setFirstNameUpdate(rs2.getString("first_name"));
                        controller.setSurnameUpdate(rs2.getString("surname"));

                        DatabaseHandler.setEmail(rs2.getString("email"));
                        stage.show();

                        ((Stage) login.getScene().getWindow()).close();

                    } else {
                        AlertMaker.showError("Login", "Your Account is not yet Verrified!\nPlease Check your email for your Verification Code");

                        Parent parent = FXMLLoader.load(getClass().getResource("/ui/verification/verify.fxml"));
                        Stage stage = new Stage(StageStyle.DECORATED);
                        stage.setScene(new Scene(parent));
                        stage.setTitle("Verification Form");
                        DatabaseHandler.setEmail(rs2.getString("email"));
                        stage.show();

                        ((Stage) login.getScene().getWindow()).close();
                    }
                } else {
                    AlertMaker.showError("Login", "Incorrect Password");
                }
            } else {
                AlertMaker.showError("Login", "Invalid Email or Phone Number");
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void handleRegister(ActionEvent event) {
//        AlertMaker.showInformation("Alert Title", "ALert Content");
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/ui/registration/register.fxml"));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setScene(new Scene(parent));
            stage.setTitle("Registration Form");
            stage.show();

            ((Stage) login.getScene().getWindow()).close();
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void handleTemsAndCondition(ActionEvent event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/ui/termandconditions/termandconditions.fxml"));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setScene(new Scene(parent));
            stage.setTitle("Terms and Conditions");
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
