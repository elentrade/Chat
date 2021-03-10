package server;
//обработчик, который в отдельном потоке обрабатывает входящие и исходящие данные с каждого подключившегося клиента
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {
    private Server server;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public ClientHandler(final Server server, final Socket socket) {
            try {
                this.server = server;
                this.socket = socket;
                this.in = new DataInputStream(socket.getInputStream());
                this.out = new DataOutputStream(socket.getOutputStream());
                //выделеление обработки исходящего и входящего потока клиента в отдельный от графики поток
                new Thread(()-> {
                    try {
                        while (true) {
                            String str = ClientHandler.this.in.readUTF();
                            //рассылка всем клиентам исходящего сообщения
                            //не понятно - str здесь входящее сообщение и оно же передается в исходящий поток????
                            if (!str.equals("/end")) {
                                server.broadcast(str);
                                continue;
                            }
                            return;
                        }
                                //System.out.println("Клиент отключился"); //кажись это лишнее
                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                //удаление данного клиента из списка. ссылка this не работает в runnable, поэтому поток запускаем через лямбду
                                server.unsubscribe(this);
                                System.out.println("Связь с сервером потеряна/handler");
                                try {
                                    socket.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                }).start();
            }
            catch (IOException e){
                e.printStackTrace();
            }
    }
    //метод для передачи исходящей информации от клиента
    void sendMsg(String msg){
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
