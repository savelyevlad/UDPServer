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
    private HashMap<Id, Client> clientsStreamers = new HashMap<>();
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

    private byte[] deleteFirstByte(DatagramPacket datagramPacket) {
        byte[] ans = new byte[datagramPacket.getLength() - 1];
        System.arraycopy(datagramPacket.getData(), 1, ans, 0, datagramPacket.getLength() - 1);
        return ans;
    }

    @Override
    public void run() {
        System.out.println("Server was started");

        // 0 byte - type
        // types:
        // -1 -> start recording
        // -2 -> end recording
        // -3 -> joined sharing
        // -4 -> stopped joining sharing

        while(true) {
            try {
                DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length);
                socket.receive(datagramPacket);

                System.out.println("Something was received. Size = " + datagramPacket.getLength());

                if(datagramPacket.getData()[0] != 14 && datagramPacket.getData()[1] != 88) {
                    continue;
                }

                datagramPacket.setData(deleteFirstAndSecondBytes(datagramPacket));

                System.out.println("first byte = " + datagramPacket.getData()[0]);

                Id id = new Id(datagramPacket.getAddress(), datagramPacket.getPort());

                if(clientHashMap.get(id) == null) {
                    System.out.println("Client #" + clients.size() + " was connected");
                    Client client = new Client(id, clients.size(), datagramPacket.getAddress(), datagramPacket.getPort());
                    clientHashMap.put(id, client);
                    respond(client);
                    clients.add(client);
                    continue;
                }

                if(datagramPacket.getData()[0] == -1) {
                    if(clientsStreamers.get(id) == null) {
                        clientsStreamers.put(id, clientHashMap.get(id));
                        Client client = clientsStreamers.get(id);
                        socket.send(new DatagramPacket(new byte[] {client.getNumber().byteValue()}, 1, client.getInetAddress(), client.getPORT()));
                    }
                    else {
                        datagramPacket = new DatagramPacket(deleteFirstByte(datagramPacket), datagramPacket.getLength() - 1, datagramPacket.getAddress(), datagramPacket.getPort());
                        for(Client client : clientHashMap.get(id).getListenets()) {
                            Pair<DatagramPacket, Client> toSend = new Pair<>(datagramPacket, client);
                            Thread thread = new Thread(new Sender(toSend, socket));
                            thread.setPriority(Thread.MIN_PRIORITY);
                            thread.start();
                        }
                    }
                }
                else if(datagramPacket.getData()[0] == -2) {
                    // TODO: breaking translation
                }
                else if(datagramPacket.getData()[0] == -3) {
                    int i = datagramPacket.getData()[1];
                    System.out.println("Client #" + clientHashMap.get(id).getNumber() + " joined client #" + i);
                    clients.get(i).getListenets().add(clientHashMap.get(id));
                }
                else if(datagramPacket.getData()[0] == -4) {
                    // TODO: stopped joining sharing
                }

//                Integer number = (int) datagramPacket.getData()[0];

//                System.out.println("To " + number + " client packet with length " + datagramPacket.getLength());
//                System.out.println(Arrays.toString(datagramPacket.getData()));

//                System.out.println("length: " + datagramPacket.getLength() + " gl:" + datagramPacket.getData().length);
//                datagramPacket = new DatagramPacket(deleteFirstByte(datagramPacket), datagramPacket.getLength() - 1, datagramPacket.getAddress(), datagramPacket.getPort());

//                Client client = clients.get(number);
//                Pair<DatagramPacket, Client> toSend = new Pair<>(datagramPacket, client);

//                System.out.println("To " + toSend.getSecond().getNumber() + " client packet with length " + datagramPacket.getData().length + " " + (counter++));

//                Thread thread = new Thread(new Sender(toSend, socket));
//                thread.setPriority(Thread.MIN_PRIORITY);
//                thread.start();
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
