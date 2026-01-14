//Zhulien Mobin - 3191549
package griffith;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.InputMismatchException;
import java.util.Random;

public class assignmentOne {
	static Scanner scanner = new Scanner(System.in);
	static PlayerStatistics stats = new PlayerStatistics();
	static String[] available_attacks = { "Basic attack", "Special attack" };
	static String[] fighter_options = { "Aatrox", "Kayn", "Kayle", "Pantheon" };
	static int total_menu_choices = 3;
	static int basic_attacks_needed = 3;
	static int combat_outcome;
	static int random_bot;
	static int random_player_fighter;
	final static int LOSE = 0;
	final static int WIN = 1;
	final static int DRAW = 2;
	final static boolean COUNT_METER = true;
	final static boolean NO_METER = false;
	final static boolean PLAYER = true;
	final static boolean NON_PLAYER = false;

	static File save_dir = new File(System.getProperty("user.home"), ".theground"); // we create a directory in the user's home folder
	static boolean result = save_dir.mkdir(); // we then save that directory
	static File file = new File(save_dir, "player_statistics.dat"); // we create player_statistics.dat in that same folder. this way the program supports multi-platform creation of player statistics if we were to use user.dir instead, then the program would launch from wherever it's saved and not the user's home folder making player_statistics potentially move around. which would be unwanted/annoying behaviour

	public static void basicAttack(Fighter attacker, Fighter defender, boolean count, boolean player) {// calculates defender hp after each basic attackand keepstrack of some combat stats
		int dmg_done = (attacker.getAtk() / defender.getDef());
		int dmg_blocked = 0;
		defender.setHp(defender.getHp() - dmg_done);

		//these conditionals exist to avoid the method adding bot stats to players stats
		if (count) {
			attacker.setBasicAttackCount(attacker.getBasicAttacksCount() + 1);
			attacker.setBasicAttackCountForStatistics(attacker.getBasicAttacksCountForStatistics() + 1);
		}
		if (player) {
			attacker.setBasicAttackDamageDone(attacker.getBasicAttackDamageDone() + dmg_done);
		} else if (!player) { // if the player is the defender
			dmg_blocked = (attacker.getAtk() - dmg_done);
			defender.setBasicAttackDamageBlocked(defender.getBasicAttackDamageBlocked() + dmg_blocked);
		}
	}

	public static void specialAttack(Fighter attacker, Fighter defender, boolean count, boolean player) { //after 3 basic attacks Fighter objects can launch a special attack affecting their and enemy stats.
		int dmg_done = 0;
		int dmg_blocked = 0;
		int hp_healed = 0;
		if (attacker.getChampion().equals("Aatrox")) {
			dmg_done = (attacker.getAtk() / defender.getDef()) * 2;
			dmg_blocked = (attacker.getAtk() - dmg_done);
			hp_healed = dmg_done;
			defender.setHp(defender.getHp() - dmg_done);
			attacker.setHp(attacker.getHp() + hp_healed);
		} else if (attacker.getChampion().equals("Kayle")) {
			dmg_done = (attacker.getAtk() / (defender.getDef() / 2));
			dmg_blocked = (attacker.getAtk() - dmg_done);
			defender.setHp(defender.getHp() - dmg_done);
		} else if (attacker.getChampion().equals("Kayn")) {
			defender.setDef(defender.getDef() - 5);
			if (defender.getDef() < 1) {
				defender.setDef(1);
			}

			dmg_done = (attacker.getAtk() / defender.getDef());
			dmg_blocked = (attacker.getAtk() - dmg_done);
			defender.setHp(defender.getHp() - dmg_done);

		} else if (attacker.getChampion().equals("Pantheon")) {
			attacker.setAtk(attacker.getAtk() + 5);
			dmg_done = (attacker.getAtk() / defender.getDef());
			dmg_blocked = (attacker.getAtk() - dmg_done);
			defender.setHp(defender.getHp() - dmg_done);
		}

		//these conditionals exist to avoid the method adding bot stats to players stats
		if (count) {
			attacker.setSpecialAttacksCount(attacker.getSpecialAttacksCount() + 1);
		}
		if (player) {
			attacker.setSpecialAttackDamageDone(attacker.getSpecialAttackDamageDone() + dmg_done);
			attacker.setHpHealed(attacker.getHpHealed() + hp_healed);
			System.out.println("Total hp healed is: " + attacker.getHpHealed());
		}
		else if (!player) {// if the player is the defender
			defender.setSpecialAttackDamageBlocked(defender.getSpecialAttackDamageBlocked() + dmg_blocked);
		}
	}

	public static int combatLogic(String[] fighter_options, int fighter_choice, int random_bot) {//TODO add crit and dodge chances to make combat more dynamic. Criting would increase basic attack counter by 1 while dodging would stop the basic attack counter from going up
		int turn = 1;
		int player_won = 0;
		Fighter player_figther = new Fighter(100, 105, 30, fighter_options[fighter_choice]);
		Fighter bot_figther = new Fighter(100, 105, 30, fighter_options[random_bot]);
		do {
			System.out.println(String.format("\nTurn %s", (turn)));
			System.out.println("Choose attack type!");
			for (int i = 0; i < available_attacks.length; i++) {
				System.out.print(String.format("%d.%s ", (i + 1), available_attacks[i]));
			}

			int attack_choice;
			try {
				attack_choice = scanner.nextInt();
			} catch (InputMismatchException e) {
				System.out.println("\nPlease use numbers for the attack choice!");
				scanner.nextLine();
				continue;
			}
			scanner.nextLine();
			if (attack_choice > 0 && attack_choice <= 2) { //TODO adjust attack_choice range once we got all the attack types
				switch (attack_choice) {
					case 1 -> {
						basicAttack(player_figther, bot_figther, COUNT_METER, PLAYER);
						break;
					}
					case 2 -> {
						if (player_figther.getBasicAttacksCount() < basic_attacks_needed) {
							System.out.println(String.format("\nPlease use %s more basic attacks!\n",(basic_attacks_needed- player_figther.getBasicAttacksCount())));
							continue;
						} else {
							specialAttack(player_figther, bot_figther, COUNT_METER, PLAYER);
							player_figther.setBasicAttackCount(player_figther.getBasicAttacksCount() - basic_attacks_needed); 																																																																									
						}
						break;
					}
				}

				if (bot_figther.getBasicAttacksCount() == basic_attacks_needed) {
					specialAttack(bot_figther, player_figther, NO_METER, NON_PLAYER);
					bot_figther.setBasicAttackCount(bot_figther.getBasicAttacksCount() - basic_attacks_needed); 
					System.out.println("\nBot Special Attacked!\n");
				} else if (bot_figther.getBasicAttacksCount() != basic_attacks_needed) {
					basicAttack(bot_figther, player_figther, COUNT_METER, NON_PLAYER);
					System.out.println("\nBot basic attacked!\n");
				}
			} else {
				System.out.println("\nPlease choose a viable attack!");
				continue;
			}

			turn++; 

			System.out.println(String.format("Player Stats: Hp: %s, Atk: %s, Def: %s",player_figther.getHp(), player_figther.getAtk(), player_figther.getDef()));
			System.out.println(String.format("Bot Stats: Hp: %s, Atk: %s, Def: %s\n",bot_figther.getHp(), bot_figther.getAtk(), bot_figther.getDef()));
			
			if (player_figther.getHp() <= 0 && bot_figther.getHp() <= 0) {
				player_won = DRAW;
			} else if (player_figther.getHp() <= 0) {
				player_won = LOSE;
			} else if (bot_figther.getHp() <= 0) {
				player_won = WIN;
			}

		} while (player_figther.getHp() > 0 && bot_figther.getHp() > 0);
		stats.addStats(player_figther, bot_figther);


		// stats.games_played++;
		if (player_won == WIN) {
			stats.games_won++;
		} else if (player_won == LOSE) {
			stats.games_lost++;
		} else if (player_won == DRAW) {
			stats.games_drawn++;
		}

		saveStats(); 

		return player_won;
	}

	public static boolean continue_fight(String choice) {
		boolean rerun;
		if (choice.equals("r")) {
			rerun = true;
		} else {
			rerun = false;
		}

		return rerun;
	}

	public static int randomFighter() { // if we were to make random_fighter into a static variable instead, it wouldn't be able to change it's value each time it's called
		int random_fighter = new Random().nextInt(fighter_options.length); // num range: 1-3
		return random_fighter;
	}

	public static void saveStats() {

		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
			oos.writeObject(stats);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void loadStats() {
		if (!file.exists() || file.length() == 0) { // if a file doesn't exist or if it's empty we create new stats
			stats = new PlayerStatistics();
			return;// once new stats are created we immediately exit this method as to not trigger EOFException
		}
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {//since serialized files start with a header and empty files wouldn't have one, we get a EOFException
			stats = (PlayerStatistics) ois.readObject(); // ois.readObject() returns an object and we cast it to PlayerStatistics
		}

		catch (IOException | ClassNotFoundException e) {
			stats = new PlayerStatistics();
			e.printStackTrace();
		}
	}

	public static String loadAscii(String filepath) { //reading ascii from resources seems better as to not cluter code (https://www.baeldung.com/java-getresourceasstream-vs-fileinputstream)
		try (InputStream input_stream = assignmentOne.class.getResourceAsStream(String.format("/ascii/%s.txt", filepath))) {
			String result = null;
			if (input_stream != null) {
				result = new BufferedReader(new InputStreamReader(input_stream)).lines().collect(Collectors.joining("\n"));
			}
			return result;
		} catch (IOException e) {
			return null;
		}
	}

	public static int fighterChoice(){//lists out all the fighter options and returns the user's pick
		System.out.println("\nYou can choose from: \n");
		for (int i = 0; i < fighter_options.length; i++) {
			System.out.println((i + 1) + "." + fighter_options[i]);
			System.out.println(loadAscii(fighter_options[i]) + "\n");
		}
		System.out.println("Make your choice! \n");
		int fighter_choice = scanner.nextInt() - 1;

		return fighter_choice;
	}

	public static void main(String[] args) {
		loadStats();

		while (true) {
			System.out.println(loadAscii("logo"));
			System.out.println(loadAscii("menu"));
			System.out.print(String.format("Choose options (from 1 to %s): ", total_menu_choices));
			int menu_choice;
			try {
				menu_choice = scanner.nextInt();
				if (menu_choice > total_menu_choices || menu_choice < 0) {
					System.out.println("Choice out of range!\n");
				}
			} catch (InputMismatchException e) {
				System.out.println("Please use numbers for the menu choice!");
				scanner.nextLine();
				continue;
			}
			scanner.nextLine();

			switch (menu_choice) {
				/*case 1 -> {//TODO add a story mode which when beaten, would unlock Aatrox.

				}*/

				case 1 -> {
					System.out.print("\nChoose a character(C), play a random(R) one or choose for the enemy(K)? ");

					String random_choice_or_not = scanner.nextLine().replaceAll("\\s+", "").toUpperCase();
					while (true) {

						if (random_choice_or_not.equals("C")) {
							int player_figther;
							try {
								System.out.print("Please choose YOUR fighter! \n");
								player_figther = fighterChoice();
							} catch (InputMismatchException e) {
								System.out.println("\nPlease use numbers for the fighter choice!\n");
								scanner.nextLine();
								continue;
							}

							random_bot = randomFighter(); // we assign a random value generated by randomFighter() to random_bot. if we don't the program will print out e.g "Bot fighter is: Kayn" while the actual bot fighter might be "Aatrox" due to how I've made randomFighter() work.
							try {
								System.out.println(String.format("Player fighter: %s \nBot fighter: %s",fighter_options[player_figther], fighter_options[random_bot]));
							} catch (ArrayIndexOutOfBoundsException e) {
								System.out.println("Choice out of range!");
								scanner.nextLine();
								continue;
							}
							scanner.nextLine();

							combat_outcome = combatLogic(fighter_options, player_figther, random_bot); // initially combatLogic() returned a boolean, but since can have more than 2 end of round states making it return an int makes more sense

							if (combat_outcome == WIN) {
								System.out.println("Player won!");
							} else if (combat_outcome == LOSE) {
								System.out.println("Bot won!");
							} else if (combat_outcome == DRAW) {
								System.out.println("It's a Draw!");
							}

							//The "would you like to fight again?" logic could maybe be made into a method
							System.out.print("\nWould you like to continue fighting(r) or click any other key to go back to menu? ");
							String fight_on = scanner.nextLine().replaceAll("\\s+", "").toLowerCase();

							if (continue_fight(fight_on)) {
								continue;
							} else {
								break;
							}
						}

						else if (random_choice_or_not.equals("R")) {
							random_bot = randomFighter();
							random_player_fighter = randomFighter();
							System.out.println(String.format("Player fighter: %s \nBot fighter: %s",fighter_options[random_player_fighter], fighter_options[random_bot]));

							combat_outcome = combatLogic(fighter_options, random_player_fighter, random_bot);

							if (combat_outcome == WIN) {
								System.out.println("Player won!");
							} else if (combat_outcome == LOSE) {
								System.out.println("Bot won!");
							} else if (combat_outcome == DRAW) {
								System.out.println("It's a Draw!");
							}

							System.out.print("\nWould you like to continue fighting(r) or click any other key to go back to menu? ");
							String fight_on = scanner.nextLine().replaceAll("\\s+", "").toLowerCase();

							if (continue_fight(fight_on)) {
								continue;
							} else {
								break;
							}
						}
						else if(random_choice_or_not.equals("K")){
							int player_figther;
							int bot_fighter;
							
							try{
								System.out.print("Please choose YOUR fighter! \n");
								player_figther = fighterChoice();

								System.out.print("Please choose ENEMY fighter! \n");
								bot_fighter = fighterChoice();
							}
							catch(InputMismatchException e){
								System.out.println("\nPlease use numbers for the fighter choice!\n");
								scanner.nextLine();
								continue;
							}

							System.out.println(String.format("Player fighter: %s \nBot fighter: %s",fighter_options[player_figther], fighter_options[bot_fighter]));
							combat_outcome = combatLogic(fighter_options, player_figther, bot_fighter);
							if (combat_outcome == WIN) {
								System.out.println("Player won!");
							} else if (combat_outcome == LOSE) {
								System.out.println("Bot won!");
							} else if (combat_outcome == DRAW) {
								System.out.println("It's a Draw!");
							}
							System.out.print("\nWould you like to continue fighting(r) or click any other key to go back to menu? ");
							String fight_on = scanner.nextLine().replaceAll("\\s+", "").toLowerCase();
							if (continue_fight(fight_on)) {
								continue;
							} else {
								break;
							}
						}

						else {
							System.out.println("\nPlease enter either C, R or K!\n");
							break;
						}
					}
				}

				case 2 -> {
					// all this printing could maybe be made into a method instead or a loop
					System.out.println("Basic damage done: " + stats.basic_attack_dmg_done);
					System.out.println("Special damage done: " + stats.special_attack_dmg_done);
					System.out.println("Total damage done: " + (stats.basic_attack_dmg_done + stats.special_attack_dmg_done));
					System.out.println("Basic attacks: " + stats.basic_attack_counter);
					System.out.println("Special attacks: " + stats.special_attack_counter);
					System.out.println("Total attacks: " + (stats.basic_attack_counter + stats.special_attack_counter));
					System.out.println("Basic attacks damage blocked: " + stats.basic_attack_dmg_blocked);
					System.out.println("Special attacks damage blocked: " + stats.special_attack_dmg_blocked);
					System.out.println("Total damage blocked: "+ (stats.basic_attack_dmg_blocked + stats.special_attack_dmg_blocked));
					System.out.println("Games won: " + stats.games_won);
					System.out.println("Games lost: " + stats.games_lost);
					System.out.println("Games drawn: " + stats.games_drawn);
					System.out.println("Total games played: " + (stats.games_won + stats.games_lost + stats.games_drawn));
					System.out.println("Health points healed: " + stats.hp_healed);
				}

				case 3 -> {
					return;
				}
			}
		}
	}
}