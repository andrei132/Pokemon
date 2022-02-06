package Items.AllItems;

import Items.Item;

public class Shield extends Item {
    public Shield() {
        super(0,0,0,2,2);
    }

    @Override
    public String toString() {
        return "Shield{Defence +2, Special Defence +2}";
    }
}
