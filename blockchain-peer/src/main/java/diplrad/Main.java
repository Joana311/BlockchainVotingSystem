package diplrad;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {

    public static void main(String[] args) throws NoSuchAlgorithmException, KeyManagementException {

        PeerRequest peerRequest = new PeerRequest(InetAddress.getLoopbackAddress(), 8080);

        HttpSender httpSender = new HttpSender();
        Peer peer = httpSender.registerPeer(peerRequest);

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

    private static String getRandomCandidate(List<String> candidates) {
        int index = (int) (Math.random() * candidates.size());
        return candidates.get(index);
    }

}
