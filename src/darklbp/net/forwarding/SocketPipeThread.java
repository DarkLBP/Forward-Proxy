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
                byte[] buffer = new byte[8192];
                int read;
                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
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
