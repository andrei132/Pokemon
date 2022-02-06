package Items.AllItems;

import Items.Item;

public class ChristmasTree extends Item {
    public ChristmasTree() {
        super(0,3,0,1,0);
    }

    @Override
    public String toString() {
        return "ChristmasTree{Attack +3, Defence +1}";
    }

}
