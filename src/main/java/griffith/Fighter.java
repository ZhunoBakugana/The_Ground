//Zhulien Mobin - 3191549
package griffith;

public class Fighter {
    private int hp;
    private int atk;
    private int def;
    private String champion;
    private int basic_attack_counter; 
    private int basic_attack_counter_for_statistics; // since we consume basic attacks for special attacks we wouldn't get the accurate basic attack count otherwise
    private int basic_attack_dmg_done;
    private int special_attack_dmg_done; 
    private int basic_attack_dmg_blocked;
    private int special_attack_dmg_blocked;
    private int special_attack_counter;
    private int hp_healed;

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

    public int getBasicAttackDamageBlocked(){
        return basic_attack_dmg_blocked;
    }

    public int getSpecialAttackDamageBlocked(){
        return special_attack_dmg_blocked;
    }

    public int getSpecialAttacksCount(){
        return special_attack_counter;
    }

    public int getHpHealed(){
        return hp_healed;
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

    /*public void setChampion(String champion){
        this.champion = champion;
    }*/

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

    public void setBasicAttackDamageBlocked(int basic_attack_dmg_blocked){
        this.basic_attack_dmg_blocked = basic_attack_dmg_blocked;
    }

    public void setSpecialAttackDamageBlocked(int special_attack_dmg_blocked){
        this.special_attack_dmg_blocked = special_attack_dmg_blocked;
    }

    public void setSpecialAttacksCount(int special_attack_counter){
        this.special_attack_counter = special_attack_counter;
    }
    
    public void setHpHealed(int hp_healed){
        this.hp_healed = hp_healed;
    }
}