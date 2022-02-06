package Items;

import Items.AllItems.*;
import org.jetbrains.annotations.NotNull;

public class ItemFactory {

    private static ItemFactory instance = null;

    private ItemFactory(){}

    /**
     * Give an new instance of ItemFactory if it don't exist, else give old instance
     * @return ItemFactory's instance
     **/
    public static ItemFactory giveItemFactory(){
        if(instance == null) instance = new ItemFactory();
        return instance;
    }

    /**
     * Give an item from all possible items
     * @param itemType Item type to give
     * @return Item
     **/
    public Item giveItem(@NotNull ItemType itemType){
        switch (itemType){
            case Cape: return new Cape();
            case Vest: return new Vest();
            case Wand: return new Wand();
            case Sword: return new Sword();
            case Shield: return new Shield();
            case Vitamins: return new Vitamins();
            case ChristmasTree: return new ChristmasTree();
            default: return null;
        }
    }

}
