package Items.AllItems;

import Items.Item;

public class Vest extends Item {
    public Vest() {
        super(10,0,0,0,0);
    }

    @Override
    public String toString() {
        return "Vest{HP +10}";
    }

}
