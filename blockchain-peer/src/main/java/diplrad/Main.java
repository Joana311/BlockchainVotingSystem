package diplrad;

import diplrad.blockchain.Block;
import diplrad.blockchain.BlockChain;
import diplrad.blockchain.VotingBlockChain;
import diplrad.http.HttpSender;
import diplrad.peer.Peer;
import diplrad.peer.PeerRequest;
import diplrad.tcp.TcpServer;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {

    public static void main(String[] args) {

        TcpServer.TcpServerThread t = new TcpServer.TcpServerThread();
        t.start();

        PeerRequest peerRequest = new PeerRequest(getOwnIpAddress(), Constants.TCP_SERVER_PORT);

        HttpSender httpSender = new HttpSender();
        Peer ownPeer = httpSender.registerPeer(peerRequest);
        List<Peer> peers = httpSender.getPeers();

        List<String> candidates = null;
        List<String> voters = null;
        try {
            candidates = Files.readAllLines(Paths.get("./src/main/resources/candidates.txt"), StandardCharsets.UTF_8);
            voters = Files.readAllLines(Paths.get("./src/main/resources/voters.txt"), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Unable to read from files.");
            System.exit(1);
        }

        Set<String> candidatesUnique = new HashSet<>(candidates);
        Set<String> votersUnique = new HashSet<>(voters);

        if (candidates.size() != candidatesUnique.size() || voters.size() != votersUnique.size()) {
            System.out.println("Duplicate candidates or voters found.");
            System.exit(1);
        }

        BlockChain blockchain = new VotingBlockChain(candidates);

        Block firstVote = new Block(getRandomCandidate(candidates), blockchain.getLastBlockHash());
        blockchain.mineBlock(firstVote);
        Block secondVote = new Block(getRandomCandidate(candidates), blockchain.getLastBlockHash());
        blockchain.mineBlock(secondVote);
        Block thirdVote = new Block(getRandomCandidate(candidates), blockchain.getLastBlockHash());
        blockchain.mineBlock(thirdVote);

        System.out.println("Blockchain is valid: " + blockchain.validate());

    }

    private static InetAddress getOwnIpAddress() {
        try (final DatagramSocket datagramSocket = new DatagramSocket()) {
            datagramSocket.connect(InetAddress.getByName("8.8.8.8"), 12345);
            return datagramSocket.getLocalAddress();
        } catch (SocketException | UnknownHostException e) {
            System.out.println("Unable to fetch own IP address.");
            System.exit(1);
        }
        return null;
    }

    private static String getRandomCandidate(List<String> candidates) {
        int index = (int) (Math.random() * candidates.size());
        return candidates.get(index);
    }

}
