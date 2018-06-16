/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestaoginasiofx.views;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.Stage;

/**
 *
 * @author Rui
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML private Button btIniciar;
   /* @FXML private ProgressBar pb ;
    @FXML private ProgressIndicator pi;*/
    
    @FXML
    private void iniciar(ActionEvent event) {
        Stage stage =(Stage) ((Node)event.getSource()).getScene().getWindow();
        FXMLLoader loader= new FXMLLoader(getClass().getResource("FXMLIniciarSessao.fxml"));
        try{
            Parent parent =loader.load();
            Scene scene= new Scene(parent);
            stage.setScene(scene);
            stage.show();
        }catch(IOException e){
            System.out.println("Erro abrir Panel");
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
       /* Task task = taskWorker(10);
        pb.progressProperty().bind(task.progressProperty());
        pi.progressProperty().bind(task.progressProperty());*/
    }  
    
     private Task taskWorker(int seconds){
        return new Task(){
            @Override
            protected Object call() throws Exception {
                for(int i=0; i<seconds; i++){
                    updateProgress(i, seconds);
                    Thread.sleep(1000);
                }
                return true;
            };
            
        };
    }
    
}
