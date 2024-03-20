package diplrad.models;

import diplrad.constants.Constants;

import java.util.HashSet;
import java.util.List;

public class VotingBlockChain extends BlockChain {

    private static VotingBlockChain INSTANCE;

    public static VotingBlockChain createInstance(List<String> candidates) {
        if (INSTANCE != null) {
            System.out.println("VotingBlockChain already created");
            return INSTANCE;
        }
        INSTANCE = new VotingBlockChain(candidates);
        return INSTANCE;
    }

    public static VotingBlockChain getInstance() {
        return INSTANCE;
    }

    private List<String> candidates;

    private VotingBlockChain(List<String> candidates) {
        super();
        this.candidates = candidates;
    }

    public List<String> getCandidates() {
        return candidates;
    }

    @Override
    public boolean validate() {
        boolean isGenesisBlockDataCorrect = getBlocks().get(0).getData().equals(Constants.GENESIS_BLOCK_DATA);
        List<Block> blocksExceptGenesisBlock = getBlocks().subList(1, getBlocks().size());
        boolean areOtherBlockDataCorrect = new HashSet<>(candidates).containsAll(blocksExceptGenesisBlock.stream().map(Block::getData).toList());
        return isGenesisBlockDataCorrect && areOtherBlockDataCorrect && super.validate();
    }

}
