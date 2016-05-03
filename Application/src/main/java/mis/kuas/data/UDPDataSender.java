package mis.kuas.data;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.URL;

/**
 * Created by mingjia on 2016/4/6.
 */
public class UDPDataSender {

    private URL url;

    private int port;

    private InetAddress address;

    private DatagramSocket udpSocket;

    public UDPDataSender(String host, int port) throws IOException {
        this.url = new URL(host);
        this.port = port;
        this.address = InetAddress.getByName(this.url.getHost());
        this.udpSocket = new DatagramSocket(this.port);
    }

    public void send(String result) {
        byte[] sendData = result.getBytes();
        DatagramPacket dp = new DatagramPacket(sendData, sendData.length, address, this.port);
        try {
            this.udpSocket.send(dp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
