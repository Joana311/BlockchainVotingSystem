package diplrad.tcp.blockchain;

import com.google.gson.Gson;
import diplrad.constants.Constants;
import diplrad.models.blockchain.VotingBlockChain;
import diplrad.models.blockchain.VotingBlockChainSingleton;
import diplrad.tcp.TcpClient;

import java.io.IOException;

public class BlockChainTcpClient extends TcpClient {

    public void sendBlockchainRequest(Gson gson) throws IOException {
        String response = sendMessage(Constants.BLOCKCHAIN_REQUEST);
        VotingBlockChain blockchain = gson.fromJson(response, VotingBlockChain.class);
        BlockChainTcpMessageObserver observer = new BlockChainTcpMessageObserver(gson);
        observer.blockChainMessageReceived(blockchain);
    }

    public void sendBlockchain(Gson gson) throws IOException {
        sendMessage(gson.toJson(VotingBlockChainSingleton.getInstance()));
    }

}
