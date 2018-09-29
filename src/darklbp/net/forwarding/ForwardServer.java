package darklbp.net.forwarding;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

class ForwardServer {
    private String listenHost;
    private String forwardHost;
    private int listenPort;
    private int forwardPort;

    ForwardServer(String listenHost, String forwardHost) throws Exception {
        String[] listenData = listenHost.split(":");
        String[] forwardData = forwardHost.split(":");
        if (listenData.length != 2 || forwardData.length != 2) {
            throw new Exception("Invalid hosts format");
        }
        this.listenHost = listenData[0];
        this.listenPort = Integer.parseInt(listenData[1]);
        this.forwardHost = forwardData[0];
        this.forwardPort = Integer.parseInt(forwardData[1]);
    }

    void startServer() throws IOException {
        InetAddress address = InetAddress.getByName(this.listenHost);
        ServerSocket serverSocket = new ServerSocket(this.listenPort, 0, address);
        while (true) {
            Socket clientSocket = serverSocket.accept();
            Socket forwardSocket = new Socket(this.forwardHost, this.forwardPort);
            System.out.println("Incoming connection from " + clientSocket.getRemoteSocketAddress());
            new SocketPipeThread(clientSocket, forwardSocket).start();
        }
    }
}
