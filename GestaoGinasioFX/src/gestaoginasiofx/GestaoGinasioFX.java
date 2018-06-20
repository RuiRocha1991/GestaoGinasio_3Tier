/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestaoginasiofx;

import hibernate.HibernateUtil;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

/**
 *
 * @author Rui
 */
public class GestaoGinasioFX extends Application {
    
     @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("views/FXMLDocument.fxml"));
        Image image= new Image("/gestaoginasiofx/picture/Icon.png");
        stage.setTitle("GYM ESTG - Getão do seu Ginásio");
        Scene scene = new Scene(root);
        stage.getIcons().add(image);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop(){
        StandardServiceRegistryBuilder.destroy(HibernateUtil.getSessionFactory().getSessionFactoryOptions().getServiceRegistry());
        Platform.exit();
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
