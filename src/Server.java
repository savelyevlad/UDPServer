import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.LinkedList;

public class Server implements Runnable {

    private DatagramSocket socket;
    private byte[] buf = new byte[16384];
    private final int PORT = 50000;

    private LinkedList<Client> clients = new LinkedList<>();

    public Server() {
        try {
            socket = new DatagramSocket(PORT);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println("Server was started");
        while (true) {
            try {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);

                byte number = buf[0];

                if (clients.size() <= number) {
                    synchronized (this) {
                        Client client = new Client();
                        clients.add(client);
                        client.getDatagramPackets().add(packet);
                        System.out.println("dataG size = " + client.getDatagramPackets().size());
                        Thread thread = new Thread(client);
                        thread.setPriority(Thread.MIN_PRIORITY);
                        thread.start();
                    }
//                    if (clients.size() <= number) {
//                        // TODO: lol
//                    }
                }
                else {
                    clients.get(number).getDatagramPackets().add(packet);
                    System.out.println(clients.get(number).getCount() + " " + clients.get(number).getDatagramPackets().size());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
