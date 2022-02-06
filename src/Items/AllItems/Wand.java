package Items.AllItems;

import Items.Item;

public class Wand extends Item {
    public Wand() {
        super(0,0,3,0,0);
    }

    @Override
    public String toString() {
        return "Wand{Special Attack +3}";
    }

}
