package diplrad;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import diplrad.constants.Constants;
import diplrad.exceptions.HttpException;
import diplrad.exceptions.IpException;
import diplrad.exceptions.ParseException;
import diplrad.helpers.VoteMocker;
import diplrad.http.HttpSender;
import diplrad.models.blockchain.VotingBlockChainSingleton;
import diplrad.models.peer.Peer;
import diplrad.models.peer.PeerRequest;
import diplrad.models.peer.PeersSingleton;
import diplrad.tcp.blockchain.BlockChainTcpClient;
import diplrad.tcp.TcpServer;

import java.io.IOException;

import static diplrad.helpers.IpHelper.getOwnIpAddress;

public class PeerMain {

    private static Gson gson = new GsonBuilder().create();

    public static void main(String[] args) {

        try {

            TcpServer.TcpServerThread tcpServerThread = new TcpServer.TcpServerThread();
            tcpServerThread.start();
            System.out.println("TCP server started");

            HttpSender httpSender = new HttpSender();
            PeerRequest ownPeerRequest = new PeerRequest(getOwnIpAddress().getHostAddress(), Constants.TCP_SERVER_PORT);
            Peer ownPeer = httpSender.registerPeer(ownPeerRequest);
            PeersSingleton.createInstance(httpSender.getPeers(ownPeer));
            System.out.println("Registered peer");

            for (Peer peer : PeersSingleton.getInstance()) {
                try {
                    BlockChainTcpClient client = new BlockChainTcpClient();
                    client.startConnection(peer.getIpAddress(), peer.getPort());
                    client.sendBlockchainRequest(gson, ownPeer);
                    client.stopConnection();
                } catch (IOException e) {
                    System.out.println("TCP client encountered an error");
                    System.exit(1);
                }
            }
            System.out.println("Sent blockchain requests and updated current blockchain: " + gson.toJson(VotingBlockChainSingleton.getInstance()));

        } catch (IpException | ParseException | HttpException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }

        // this is vote mocker part, used only for testing purposes

        try {

            for (int i = 0; i < 5; i++) {

                Thread.sleep((long)(Math.random() * 10000));

                VoteMocker.generateRandomVotes(VotingBlockChainSingleton.getInstance());

                for (Peer peer : PeersSingleton.getInstance()) {
                    try {
                        BlockChainTcpClient client = new BlockChainTcpClient();
                        client.startConnection(peer.getIpAddress(), peer.getPort());
                        client.sendBlockchain(gson);
                        client.stopConnection();
                    } catch (IOException e) {
                        System.out.println("TCP client encountered an error");
                        System.exit(1);
                    }
                }

            }

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

}
