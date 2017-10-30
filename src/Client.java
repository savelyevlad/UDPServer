import java.net.DatagramPacket;
import java.util.LinkedList;

public class Client implements Runnable {

    public LinkedList<DatagramPacket> getDatagramPackets() {
        return datagramPackets;
    }

    private LinkedList<DatagramPacket> datagramPackets = new LinkedList<>();

    public int getCount() {
        return count;
    }

    private int count = 0;

    @Override
    public void run() {
        while (true) {
//            System.out.println(count + " " + datagramPackets.size());
            if (count != datagramPackets.size()) {
                ++count;
                System.out.println("kek");
            }
        }
    }
}
