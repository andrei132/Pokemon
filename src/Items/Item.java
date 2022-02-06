package Items;

public abstract class Item {
    private final int HP;
    private final int attack;
    private final int specialAttack;
    private final int defence;
    private final int specialDefence;

    public abstract String toString();

    public Item(int HP, int attack, int specialAttack, int defence, int specialDefence) {
        this.HP = HP;
        this.attack = attack;
        this.specialAttack = specialAttack;
        this.defence = defence;
        this.specialDefence = specialDefence;
    }

    public int getHP() {
        return HP;
    }

    public int getAttack() {
        return attack;
    }

    public int getSpecialAttack() {
        return specialAttack;
    }

    public int getDefence() {
        return defence;
    }

    public int getSpecialDefence() {
        return specialDefence;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        return getClass() == o.getClass();
    }

}
