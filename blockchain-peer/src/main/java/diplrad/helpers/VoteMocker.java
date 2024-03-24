package diplrad.helpers;

import diplrad.models.Block;
import diplrad.models.VotingBlockChain;
import java.util.List;

public class VoteMocker {

    private static void generateRandomVotes(VotingBlockChain blockchain)
    {
        List<String> candidates = blockchain.getCandidates();

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
