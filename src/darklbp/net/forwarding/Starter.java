package darklbp.net.forwarding;

public class Starter {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Missing arguments");
        } else {
            try {
                String listenHost = args[0];
                String forwardHost = args[1];
                ForwardServer server = new ForwardServer(listenHost, forwardHost);
                System.out.println("Forwarding " + listenHost + " to " + forwardHost);
                server.startServer();
            } catch (Exception ex) {
                System.out.println("Something wrong happened");
                ex.printStackTrace();
            }
        }
    }
}
