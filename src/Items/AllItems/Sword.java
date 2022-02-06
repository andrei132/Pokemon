package Items.AllItems;

import Items.Item;

public class Sword extends Item {
    public Sword() {
        super(0,3,0,0,0);
    }

    @Override
    public String toString() {
        return "Sword{Attack +3}";
    }
}
