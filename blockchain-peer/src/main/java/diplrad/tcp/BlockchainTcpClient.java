package diplrad.tcp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import diplrad.constants.Constants;
import diplrad.models.Block;
import diplrad.models.Peer;
import diplrad.models.VotingBlockChain;

import java.io.IOException;
import java.util.List;

public class BlockchainTcpClient extends TcpClient {

    private Gson gson;

    public BlockchainTcpClient() {
        gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
    }

    public void sendBlockchainRequest() throws IOException {
        sendMessage(Constants.BLOCKCHAIN_REQUEST);
    }

    public void sendBlockchain(VotingBlockChain blockchain) throws IOException {
        String json = gson.toJson(blockchain);
        sendMessage(json);
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        TcpClient client = new TcpClient();
        client.startConnection("127.0.0.1", Constants.TCP_SERVER_PORT);

        System.out.println("Client started");

        List<String> candidates = List.of("Candidate1", "Candidate2", "Candidate3");
        VotingBlockChain blockchain = VotingBlockChain.createInstance(candidates);
        Block firstBlock = new Block("Candidate1", blockchain.getLastBlockHash());
        blockchain.mineBlock(firstBlock);
        Block secondBlock = new Block("Candidate2", blockchain.getLastBlock().getHash());
        blockchain.mineBlock(secondBlock);

        System.out.println("Blockchain built");

        Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
        String json = gson.toJson(blockchain);

        while (true){
            String output = client.sendMessage(json);
            System.out.println("Message sent");
            Thread.sleep(10000);
        }

    }

}
