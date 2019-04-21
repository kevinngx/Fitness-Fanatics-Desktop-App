package sample;

import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

/***************************************************************************************
 *    This code was taken from the tutorial 3B of INFS2605, as provided on the Edsetem website
 *    It is a helper class that is used to switch between different scenes
 ***************************************************************************************/

public class PageSwitcherHelper {

    public void switcher(ActionEvent event, String page) throws IOException {
        System.out.println("Switching pages");
        Parent parent = FXMLLoader.load(getClass().getResource(page));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void switcher(MouseEvent event, String page) throws IOException {
        System.out.println("Switching pages");
        Parent parent = FXMLLoader.load(getClass().getResource(page));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
