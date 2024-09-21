import org.example.Block;

import java.util.List;

public class Chain {
    private List<Block> _blocks;

    public Chain(List<Block> blocks) {
        _blocks = blocks;
    }

    public void addBlock(Block block) {
        _blocks.add(block);
    }

//    public void
}
