package griffith;

public class Fighter {
    private int hp;
    private int atk;
    private int def;
    private String champion;

    private int basic_attack_counter; //to be consumed for special attacks
    private int basic_attack_counter_for_statistics; // for player statistics since we consume basic attacks for special attacks and don't get the accurate count for player stats
    private int basic_attack_dmg_done; //amount of basic attack damage the player has done
    private int special_attack_dmg_done; //amount of special attack damage the player has done

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

    public int getBasicAttackDamageDone(){
        return basic_attack_dmg_done;
    }

    public int getSpecialAttackDamageDone(){
        return special_attack_dmg_done;
    }

    public int getBasicAttacksCountForStatistics(){
        return basic_attack_counter_for_statistics;
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

    public void setBasicAttackDamageDone(int basic_attack_dmg_done){
        this.basic_attack_dmg_done = basic_attack_dmg_done;
    }

    public void setSpecialAttackDamageDone(int special_attack_dmg_done){
        this.special_attack_dmg_done = special_attack_dmg_done;
    }

    public void setBasicAttackCountForStatistics(int basic_attack_counter_for_statistics){
        this.basic_attack_counter_for_statistics = basic_attack_counter_for_statistics;
    }
    //might need a method for clearing out previous champ choices in case the player wants to player more than one game
}
