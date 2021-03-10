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
            this.socket = new Socket(IP_ADRESS, PORT);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            new Thread(() -> {
                try {
                    while (true) {
                        String str = in.readUTF();
                        if (!str.equals("/end")) {
                            txt_area.appendText("Клиент " + str + "\n");
                            continue;
                        }
                        return;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    System.out.println("Мы отключились от сервера/controller");
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//это отработает перед блоком Initializable
    public void send(ActionEvent actionEvent) {
        try {
            //в выходной поток отправить текст, затем очистить поле и вернуть на него фокус
            this.out.writeUTF(txt_field.getText());
            this.txt_field.clear();
            this.txt_field.requestFocus();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
