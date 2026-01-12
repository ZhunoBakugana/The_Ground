package griffith;
import java.io.Serializable;

public class PlayerStatistics implements Serializable{
    //maybe have a *Total*, *Story Mode*, and *Free Play* statistics

    //times played specific champions
    //story mode specific bosses beaten
    //story mode which stages complete
    //story mode times beaten

    public int basic_attack_dmg_done;
    public int special_attack_dmg_done;
    public int basic_attack_counter;
    public int special_attack_counter;
    public int total_dmg_done;
    public int hp_healed;
    public int games_played;
    public int games_won;
    public int games_lost;
    public int games_drawn;
    //do the rest later

    public void addStats(Fighter attacker, Fighter defender){
        basic_attack_dmg_done += attacker.getBasicAttackDamageDone();
        special_attack_dmg_done += attacker.getSpecialAttackDamageDone();
        basic_attack_counter += attacker.getBasicAttacksCountForStatistics();
    } 
}