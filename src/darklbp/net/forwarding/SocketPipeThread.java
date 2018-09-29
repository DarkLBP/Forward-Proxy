package darklbp.net.forwarding;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SocketPipeThread extends Thread {
    private Socket clientSocket;
    private Socket forwardSocket;

    SocketPipeThread(Socket clientSocket, Socket forwardSocket) {
        this.clientSocket = clientSocket;
        this.forwardSocket = forwardSocket;
    }

    private void pipe(InputStream in, OutputStream out) {
        new Thread(() -> {
            try {
                int read;
                while ((read = in.read()) != -1) {
                    out.write(read);
                }
            } catch (IOException ex) {
                interrupt();
            }
        }).start();
    }

    @Override
    public void run() {
        try {
            pipe(forwardSocket.getInputStream(), clientSocket.getOutputStream());
            pipe(clientSocket.getInputStream(), forwardSocket.getOutputStream());
        } catch (IOException ex) {
            super.interrupt();
        }
    }
}
