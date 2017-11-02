package server;

import client.Client;
import client.Id;
import client.Pair;
import client.Sender;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.LinkedList;

public class Server implements Runnable {

    private DatagramSocket socket;
    private byte[] buf = new byte[66560];
    private final int PORT = 50000;

    private HashMap<Id, Client> clientHashMap = new HashMap<>();
    private LinkedList<Client> clients = new LinkedList<>();

    public Server() {
        try {
            socket = new DatagramSocket(PORT);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    private byte[] deleteFirstAndSecondBytes(DatagramPacket datagramPacket) {
        byte[] ans = new byte[datagramPacket.getLength() - 2];
        System.arraycopy(datagramPacket.getData(), 2, ans, 0, datagramPacket.getLength() - 2);
        return ans;
    }

    @Override
    public void run() {
        System.out.println("Server was started");

        // 1 byte - type
        // 2 byte - number
        // rest - data

        while(true) {
            try {
                DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length);
                socket.receive(datagramPacket);

                Id id = new Id(datagramPacket.getAddress(), datagramPacket.getPort());

                if(clientHashMap.get(id) == null) {
                    System.out.println("Client #" + clients.size() + " was connected");
                    Client client = new Client(id, clients.size(), datagramPacket.getAddress(), datagramPacket.getPort());
                    clientHashMap.put(id, client);
                    respond(client);
                    clients.add(client);
                    continue;
                }

                Integer number = (int) datagramPacket.getData()[1];

                System.out.println("To " + number + " client packet with length " + datagramPacket.getLength());

                datagramPacket = new DatagramPacket(deleteFirstAndSecondBytes(datagramPacket), datagramPacket.getLength() - 2, datagramPacket.getAddress(), datagramPacket.getPort());

                // TODO: if(number < clients.size()) {}

                Client client = clients.get(number);
                Pair<DatagramPacket, Client> toSend = new Pair<>(datagramPacket, client);

                Thread thread = new Thread(new Sender(toSend, socket));
                thread.setPriority(Thread.MIN_PRIORITY);
                thread.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void respond(Client client) throws IOException {
        DatagramPacket packet = new DatagramPacket(new byte[]{Byte.valueOf(String.valueOf(client.getNumber()))}, 1, client.getInetAddress(), client.getPORT());
        socket.send(packet);
    }
}
