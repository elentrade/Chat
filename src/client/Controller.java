package client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.lang.Thread;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
// отработка команд в блоке Initializable после отрисовки всех графических объектов
public class Controller implements Initializable {
    @FXML
    public TextArea txt_area;
    @FXML
    public TextField txt_field;
    private final String IP_ADRESS = "localhost";
    private final int PORT = 8189;
    private Socket socket;
    DataOutputStream out;
    DataInputStream in;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //создание сокета клиента на стороне клиента, инициализация входящего и выходящего потоков
        try {
             socket = new Socket(IP_ADRESS, PORT);
            in = new DataInputStream(socket.getInputStream());
             out = new DataOutputStream(socket.getOutputStream());
             new Thread(new Runnable() {
                 @Override
                 public void run() {
                     try {
                     while (true) {
                        // String str = null;
                         String str = in.readUTF();
                         txt_area.appendText("Клиент " + str+"\n");
                         if (str.equals("/end")) {
                             System.out.println("Клиент вышел из чата***");
                             break;
                         }
                     }
                         } catch (IOException e) {
                             e.printStackTrace();
                         }
                        finally {
                             try {
                                 socket.close();
                             } catch (IOException e) {
                                 e.printStackTrace();
                             }
                             System.out.println("Мы отключились от сервера");
                         }
                     }

             }).start();


            }
         catch (IOException e) {
            e.printStackTrace();
        }

    }

//это отработает перед блоком Initializable
    public void send(ActionEvent actionEvent) {
        try {
            //в выходной поток отправить текст, затем очистить поле и вернуть на него фокус
            out.writeUTF(txt_field.getText());
            txt_field.clear();
            txt_field.requestFocus();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
