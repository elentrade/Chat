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

    public ClientHandler(Server server, Socket socket) {
            try {
                this.server = server;
                this.socket = socket;
                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());
                //выделеление обработки исходящего и входящего потока клиента в отдельный от графики поток
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                        while (true) {
                            String   str = in.readUTF();
                            //System.out.println("Клиент "+str);
                            //out.writeUTF("Echo "+str);
                            //рассылка всем клиентам исходящего сообщения
                            //не понятно - str здесь входящее сообщение и оно же передается в исходящий поток????
                            server.broadcast(str);
                            if (str.equals("/end")) {
                                System.out.println("Клиент вышел из чата");
                                break;
                            }
                        }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        finally {
                            System.out.println("Связь с сервером потеряна");
                               try {
                                   socket.close();
                                    } catch (IOException e) {
                                      e.printStackTrace();
                                     }
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
