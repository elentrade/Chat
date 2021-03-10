package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Callable;


public class Server {
final static int PORT = 8189;
List <ClientHandler> clients  = new Vector<>();;
ServerSocket server = null;
Socket socket = null;
    public Server() {
        try {
            server = new ServerSocket(PORT);
            System.out.println("Сервер запущен, сокет для подключения выдлен");
            //создание в цикле сокетов для клиентов на стороне сервера
            while (true){
                socket = server.accept();
                System.out.println("Клиент подключился");
                //добавляем подключившегося клиента в список
                //clients.add(new ClientHandler(this, socket));// заменили на добавление через метод
                subscribe(new ClientHandler(this, socket));
            }
        }
        catch (IOException e){
           e.printStackTrace();
        }
        finally {
            try {
                server.close();
            } catch (IOException e) {
                System.out.println("server fail");
                e.printStackTrace();
            }
        }
    }
    //метод отправки каждому клиенту из списка Vektor исходящего сообщения клиента
    public void broadcast(String msg){
        for (ClientHandler client:clients)
             {
            client.sendMsg(msg);
        }
    }
    //добавление клиента в список
    public void subscribe(ClientHandler clientHandler){
        clients.add(clientHandler);
    }
    //удаление клиента из списка при выходе клиента с сервера
    public void unsubscribe(ClientHandler clientHandler){
        clients.remove(clientHandler);
    }
}
