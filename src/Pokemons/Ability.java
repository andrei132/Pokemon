package Pokemons;

public class Ability {

    private Integer damage;
    private Boolean stun;
    private Boolean dodge;
    private Integer coolDown;

    public Ability(Integer damage, Boolean stun, Boolean dodge, Integer coolDown) {
        this.damage = damage;
        this.stun = stun;
        this.dodge = dodge;
        this.coolDown = coolDown;
    }

    public Integer getDamage() {
        return damage;
    }

    public void setDamage(Integer damage) {
        this.damage = damage;
    }

    public Boolean getStun() {
        return stun;
    }

    public void setStun(Boolean stun) {
        this.stun = stun;
    }

    public Boolean getDodge() {
        return dodge;
    }

    public void setDodge(Boolean dodge) {
        this.dodge = dodge;
    }

    public Integer getCoolDown() {
        return coolDown;
    }

    public void setCoolDown(Integer coolDown) {
        this.coolDown = coolDown;
    }

    @Override
    public String toString () {
        return "Ability{" +
                "damage=" + damage +
                ", stun=" + stun +
                ", dodge=" + dodge +
                ", coolDown=" + coolDown +
                '}';
    }
}
