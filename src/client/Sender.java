package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Sender implements Runnable {

    private Pair<DatagramPacket, Client> toSend;

    private DatagramSocket datagramSocket;

    public Sender(Pair<DatagramPacket, Client> toSend, DatagramSocket datagramSocket) {
        this.toSend = toSend;
        this.datagramSocket = datagramSocket;
    }

    @Override
    public void run() {
        try {
            DatagramPacket packet = new DatagramPacket(toSend.getFirst().getData(), toSend.getFirst().getLength(),
                                                       toSend.getSecond().getInetAddress(), toSend.getSecond().getPORT());
            datagramSocket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
