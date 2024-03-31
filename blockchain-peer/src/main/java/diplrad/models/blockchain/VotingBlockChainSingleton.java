package diplrad.models.blockchain;

import java.util.List;

public class VotingBlockChainSingleton {

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

    public static void setInstance(VotingBlockChain instance) {
        INSTANCE = instance;
    }

}
