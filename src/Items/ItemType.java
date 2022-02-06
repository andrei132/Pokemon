package Items;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public enum ItemType {
    Cape("Cape"),
    ChristmasTree("ChristmasTree"),
    Shield("Shield"),
    Sword("Sword"),
    Vest("Vest"),
    Vitamins("Vitamins"),
    Wand("Wand"),
    ItemNonExistent("ItemNonExistent");

    private final String label;

    ItemType(String label) {
        this.label = label;
    }

    @Contract(pure = true)
    public static ItemType toEnum(@NotNull String label){
        switch (label){
            case "Cape" -> {return Cape;}
            case "ChristmasTree" -> {return ChristmasTree;}
            case "Shield" -> {return Shield;}
            case "Sword" -> {return Sword;}
            case "Vest" -> {return Vest;}
            case "Vitamins" -> {return Vitamins;}
            case "Wand" -> {return Wand;}
            default -> {return ItemNonExistent;}
        }
    }

}
