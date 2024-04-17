package diplrad.helpers;

import diplrad.constants.LogMessages;
import diplrad.models.blockchain.Block;
import diplrad.models.blockchain.VotingBlockChain;
import java.util.List;

public class VoteMocker {

    public static void generateRandomVotes(VotingBlockChain blockchain)
    {
        List<String> candidates = blockchain.getCandidates();
        Block block = new Block(getRandomCandidate(candidates), blockchain.getLastBlockHash());
        blockchain.mineBlock(block);
        System.out.printf((LogMessages.voteAddedMessage) + "%n", block.getData());
    }

    private static String getRandomCandidate(List<String> candidates) {
        int index = (int) (Math.random() * candidates.size());
        return candidates.get(index);
    }

}
