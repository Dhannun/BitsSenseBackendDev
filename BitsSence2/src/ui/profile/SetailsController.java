/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.profile;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Wecom
 */
public class SetailsController implements Initializable {

    @FXML
    private JFXButton OK;
    @FXML
    private Text fullName;
    @FXML
    private Text email;
    @FXML
    private Text phone;
    @FXML
    private Text dob;
    @FXML
    private Text bvn;
    @FXML
    private Text password;
    @FXML
    private Text status;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void handleOK(ActionEvent event) {
        ((Stage) OK.getScene().getWindow()).close();
    }

    public void setFullName(String name) {
        fullName.setText(name);
    }

    public void setEmail(String newEmail) {
        email.setText(newEmail);
    }

    public void setPhone(String phoneNum) {
        phone.setText(phoneNum);
    }

    public void setDob(String pDob) {
        dob.setText(pDob);
    }

    public void setBvn(String pBvn) {
        bvn.setText(pBvn);
    }

    public void setPassword(String pPassword) {
        password.setText(pPassword);
    }

    public void setStatus(String pStatus) {
        status.setText(pStatus);
    }
}
