package Items.AllItems;

import Items.Item;

public class Vitamins extends Item {
    public Vitamins() {
        super(2,2,2,0,0);
    }

    @Override
    public String toString() {
        return "Vitamins{HP +2, Attack +2, Special Attack +2}";
    }

}
