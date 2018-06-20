/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestaoginasiofx;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

/**
 *
 * @author Rui
 */
public class Notificacao {
    public static void successNotification(String title, String text){
        Image ima = new Image("/gestaoginasiofx/picture/success.png");
        Notifications not = Notifications.create()
                .title(title)
                .text(text)
                .graphic(new ImageView(ima))
                .hideAfter(Duration.seconds(6))
                .position(Pos.BOTTOM_CENTER)
                .onAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
               
            }
        });
        not.show();
    }
    
    public static void errorNotification(String title, String text){
        Image ima = new Image("/gestaoginasiofx/picture/error.png");
        Notifications not = Notifications.create()
                .title(title)
                .text(text)
                .graphic(new ImageView(ima))
                .hideAfter(Duration.seconds(15))
                .position(Pos.BOTTOM_CENTER)
                .onAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
               
            }
        });
        not.show();
    }
    
    public static void filtrosNotification(String title, String text){
        Image ima = new Image("/gestaoginasiofx/picture/success.png");
        Notifications not = Notifications.create()
                .title(title)
                .text(text)
                .graphic(new ImageView(ima))
                .hideAfter(Duration.seconds(1))
                .position(Pos.BOTTOM_CENTER)
                .onAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
               
            }
        });
        not.show();
    }
    
}
