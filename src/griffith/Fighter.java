package griffith;

public class Fighter {
    //TODO make some private variables in Fighter to keep track of player player statistics
    private int hp;
    private int atk;
    private int def;
    private String champion;
    private int basic_attack_counter;

    public Fighter(int hp, int atk, int def, String champion){
        this.hp = hp;
        this.atk = atk;
        this.def = def;
        this.champion = champion;
    }

    //getters
    public int getHp(){
        return hp;
    }

    public int getAtk(){
        return atk;
    }

    public int getDef(){
        return def;
    }

    public String getChampion(){
        return champion;
    }

    public int getBasicAttacksCount() {
        return basic_attack_counter;
    }

    //setters
    public void setHp(int hp){
        this.hp = hp;
    }

    public void setAtk(int atk){
        this.atk = atk;
    }

    public void setDef(int def){
        this.def = def;
    }

    public void setChampion(String champion){ //maybe won't need
        this.champion = champion;
    }

    public void setBasicAttackCount(int basic_attack_counter){
        this.basic_attack_counter = basic_attack_counter;
    }
    //might need a method for clearing out previous champ choices in case the player wants to player more than one game
}
