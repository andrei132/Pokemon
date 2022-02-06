package Items.AllItems;

import Items.Item;

public class Cape extends Item {
    public Cape() {
        super(0,0,0,0,3);
    }

    @Override
    public String toString() {
        return "Cape{Special Defence +3}";
    }
}
