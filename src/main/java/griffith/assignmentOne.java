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
	//TODO order the variables here so that they make sense
	static int total_menu_choices = 4;
	static Scanner scanner = new Scanner(System.in);
	static String[] available_attacks = { "Basic attack", "Special attack" };
	static int basic_attacks_needed = 3;

	final static boolean COUNT_METER = true;
	final static boolean NO_METER = false;

	final static boolean PLAYER = true;
	final static boolean NON_PLAYER = false;

	static String[] fighter_options = {"Aatrox", "Kayn", "Kayle", "Pantheon"};

	static File save_dir = new File(System.getProperty("user.home"), ".theground"); //we create a directory in the user's home folder
	static boolean result = save_dir.mkdir(); //we then save that directory
	static File file = new File(save_dir,"player_statistics.dat"); //we create player_statistics.dat in that same folder. this way the program supports multi-platform creation of player statistics
	//if we were to use user.dir instead, then the program would launch from wherever it's saved and not the user's home folder making player_statistics potentially move around. which would be unwanted/annoying behaviour

	static PlayerStatistics stats = new PlayerStatistics();

	final static int LOSE = 0;
	final static int WIN = 1;
	final static int DRAW = 2;

	static int combat_outcome;
	static int random_bot;
	static int random_player_fighter;

	public static void basicAttack(Fighter attacker, Fighter defender, boolean count, boolean player) {
		int dmg_done =  (attacker.getAtk() / defender.getDef());
		defender.setHp(defender.getHp() - dmg_done);
		if (count) {
			attacker.setBasicAttackCount(attacker.getBasicAttacksCount() + 1);
			attacker.setBasicAttackCountForStatistics(attacker.getBasicAttacksCountForStatistics() + 1);
		}
		if(player){
			attacker.setBasicAttackDamageDone(attacker.getBasicAttackDamageDone() + dmg_done);
		}
		if(!player){
			//TODO add bot stats
		}
	}

	public static void specialAttack(Fighter attacker, Fighter defender, boolean player) { 
	int dmg_done = 0;
		if (attacker.getChampion().equals("Aatrox")) { 
			dmg_done = (attacker.getAtk() / defender.getDef()) * 3; //aatrox special attack deals 6 dmg
			defender.setHp(defender.getHp() - dmg_done );
			attacker.setHp(attacker.getHp() + dmg_done); //aatrox heals 6 hp per special attack
		} else if (attacker.getChampion().equals("Kayle")) {
			dmg_done = (attacker.getAtk() / (defender.getDef() / 2));
			defender.setHp(defender.getHp() - dmg_done);

		//Patheon and Kayn just affect enemy stats and then basic attack. Which is why they're not contributing to the specialAttackDamage Counter
		} else if (attacker.getChampion().equals("Kayn")) {
			try{
				defender.setDef(defender.getDef() - 3); // maybe modify how much defence they lose later on. 
				basicAttack(attacker, defender, NO_METER, NON_PLAYER); 
			}
			catch(ArithmeticException e){
				defender.setDef(-1); //this isn't the best way to handle this since the defender loses 1 armor for no reason
			}	

		} else if (attacker.getChampion().equals("Pantheon")) {
			attacker.setAtk(attacker.getAtk() + 5); // maybe modify how much attack they gain later on
			basicAttack(attacker, defender, NO_METER, NON_PLAYER);
		}
		//TODO think about NON_PLAYER here

		if(player){
			attacker.setSpecialAttackDamageDone(attacker.getSpecialAttackDamageDone() + dmg_done);
		}

		if(!player){
			//TODO add bot stats
		}

		// continue
	}
	public static int combatLogic(String[] fighter_options, int fighter_choice, int random_bot) {
		int turn = 1;
		int player_won = 0;
		Fighter player_figther = new Fighter(100, 10, 5, fighter_options[fighter_choice]);
		Fighter bot_figther = new Fighter(100, 10, 5, fighter_options[random_bot]);
		do {
			System.out.println(String.format("\nTurn %s", (turn)));
			// ask player what attack they wanna use: basic, special etc
			System.out.println("Choose attack type!");
			for (int i = 0; i < available_attacks.length; i++) {
				System.out.print(String.format("%d.%s ", (i + 1), available_attacks[i]));
			}
			// System.out.println("1.Basic Attack, 2.Special Attack"); // TODO make this into ASCII later
			int attack_choice;
			try {
				attack_choice = scanner.nextInt();
			} catch (InputMismatchException e) {
				System.out.println("\nPlease use numbers for the attack choice!");
				scanner.nextLine();
				continue;
			}
			scanner.nextLine();
			if (attack_choice > 0 && attack_choice <= 2) { // TODO adjust attack_choice range once we got all the attack types
				switch (attack_choice) { 
					case 1 -> {
						basicAttack(player_figther, bot_figther, COUNT_METER, PLAYER);
						break;
					}
					case 2 -> {
						if (player_figther.getBasicAttacksCount() < basic_attacks_needed) {
							System.out
									.println(String.format("\nPlease use %s more basic attacks!\n",
											(basic_attacks_needed
													- player_figther.getBasicAttacksCount())));
							continue;
						} else {
							specialAttack(player_figther, bot_figther, PLAYER);
							player_figther.setBasicAttackCount(player_figther.getBasicAttacksCount() - basic_attacks_needed); // special attacks consume the basic-attack meter
						}
						break;
					}
				}				
				System.out.println("Bot basic attacks count is: " + bot_figther.getBasicAttacksCount());

				if(bot_figther.getBasicAttacksCount() == basic_attacks_needed){
					specialAttack(bot_figther, player_figther, NON_PLAYER);
					bot_figther.setBasicAttackCount(bot_figther.getBasicAttacksCount() - basic_attacks_needed); // special attacks consume the basic attack meter
					System.out.println("Bot Special Attacked!");
				}
				else if (bot_figther.getBasicAttacksCount() != basic_attacks_needed) {
					basicAttack(bot_figther, player_figther, COUNT_METER, NON_PLAYER);
					System.out.println("Bot basic attacked!");
				} 
			} else {
				System.out.println("Please choose a viable attack!");
				continue;
			}

			
			turn++; // increase turn count by 1
			
			// print out updated player and bot stats
			System.out.println(String.format("Player Stats: Hp: %s, Atk: %s, Def: %s",
					player_figther.getHp(), player_figther.getAtk(), player_figther.getDef()));
			System.out.println(String.format("Bot Stats: Hp: %s, Atk: %s, Def: %s\n",
					bot_figther.getHp(), bot_figther.getAtk(), bot_figther.getDef()));

			if(player_figther.getHp() <= 0 && bot_figther.getHp() <= 0){
				player_won = DRAW;
			}
			else if (player_figther.getHp() <= 0) {
				player_won = LOSE;
			} else if (bot_figther.getHp() <= 0) {
				player_won = WIN;
			}

		} while (player_figther.getHp() > 0 && bot_figther.getHp() > 0); // keep going until either player or bot wins
		stats.addStats(player_figther, bot_figther); //update combat stats after round

		//update games played/won/drawn
		stats.games_played++;
		if(player_won == WIN){
			stats.games_won++;
		}
		else if(player_won == LOSE){
			stats.games_lost++;
		}
		else if(player_won == DRAW){
			stats.games_drawn++;
		}

		saveStats(); //update player statistics 

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
	
	public static int randomFighter(){ // making random_fighter into static wouldn't be able to change each time, the player wishes to re-fight in random mode, hence why this method exists
		int random_fighter = new Random().nextInt(fighter_options.length); // num range: 1-3
		return random_fighter;
	}

	public static void saveStats(){

		try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
			//File fileFromPath = player_stats.toFile();
			oos.writeObject(stats);
			//oos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void loadStats(){
		if (!file.exists() || file.length() == 0) { //if a file doesn't exist or if it's empty we create new stats
			stats = new PlayerStatistics();
			return;//once new stats are created we immediately exit this method as to not trigger EOFException
			}
			try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {//the program tries reading the header immediately from the file, since serialized files start with a header and empty files wouldn't have one, we get a EOFException. hence why we have the above if()
				stats = (PlayerStatistics) ois.readObject(); //ois.readObject() returns an object and we cast it to PlayerStatistics
			} 
			
			 catch (IOException | ClassNotFoundException e) {
				stats = new PlayerStatistics();
				e.printStackTrace();
			} 
		}

	public static String loadAscii(String filepath){
		try (InputStream input_stream = assignmentOne.class.getResourceAsStream(String.format("/ascii/%s.txt", filepath))) {
		String result = null;
			if(input_stream != null){
				result = new BufferedReader(new InputStreamReader(input_stream)).lines().collect(Collectors.joining("\n"));
			}
		return result;
		}
		catch (IOException e) {
			return null;
		}
	}

	public static void main(String[] args) {
		loadStats(); //load player statistics at game start
		while (true) {
			System.out.println(loadAscii("logo"));
			System.out.println(loadAscii("menu"));
			System.out.print(String.format("Choose options (from 1 to %s): ", total_menu_choices)); 
			int menu_choice;
			try {
				menu_choice = scanner.nextInt(); 
				if (menu_choice > total_menu_choices || menu_choice < 0){
					System.out.println("Choice out of range!\n");
				}																
			} catch (InputMismatchException e) {
				System.out.println("Please use numbers for the menu choice!");
				scanner.nextLine();
				continue;
			}
			scanner.nextLine();

			switch (menu_choice) {
				case 1 -> {
					// story mode
					// TODO to unlock Aatrox, they must beat Story Mode
				}

				case 2 -> {
					// free to play
					System.out.println("Choose a character(C) or play a random(R) one?");

					String random_choice_or_not = scanner.nextLine().replaceAll("\\s+", "").toUpperCase();
					while (true) { 
									
						if (random_choice_or_not.equals("C")) {
							System.out.println("You can choose from: \n");
							for (int i = 0; i < fighter_options.length; i++) {
								System.out.println((i + 1) + "." + fighter_options[i]);
								System.out.println(loadAscii(fighter_options[i])+"\n");
							}

							System.out.println("Make your choice! \n");
							int fighter_choice;

							try {
								fighter_choice = scanner.nextInt() - 1;
								System.out.println(
										String.format("This is the value of fighter choice: %s", fighter_choice));
							} catch (InputMismatchException e) {
								System.out.println("\nPlease use numbers for the fighter choice!");
								scanner.nextLine();
								continue;
							}

							random_bot = randomFighter(); //we assign a random value generated by randomFighter(). if we don't the program will print out e.g "Bot fighter is: Kayn" while the actual bot fighter might be "Aatrox" due to how I've made randomFighter() work.
							try {
								System.out.println(String.format("Player fighter: %s \nBot fighter: %s", fighter_options[fighter_choice], fighter_options[random_bot])); 
							} catch (ArrayIndexOutOfBoundsException e) {
								System.out.println("Choice out of range!");
								scanner.nextLine();
								continue;
							}
							scanner.nextLine();
							
							combat_outcome = combatLogic(fighter_options, fighter_choice, random_bot); //after the fight is done, we assign a value to combat_outcome and use it for the following conditional checks

							if (combat_outcome == WIN) { 
								System.out.println("Player won!");
							} else if(combat_outcome == LOSE) {
								System.out.println("Bot won!");
							}
							else if (combat_outcome == DRAW){
								System.out.println("It's a Draw!");
							}

							// TODO after combat message + "fight again" logic should be a method since both game modes will need it
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
							System.out.println(String.format("Player fighter: %s \nBot fighter: %s", fighter_options[random_player_fighter], fighter_options[random_bot])); 

							combat_outcome = combatLogic(fighter_options, random_player_fighter, random_bot);

							if (combat_outcome == WIN) { 
								System.out.println("Player won!");
							} else if(combat_outcome == LOSE) {
								System.out.println("Bot won!");
							}
							else if (combat_outcome == DRAW){
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
							// prints out error message and restarts the main loop
							System.out.println("Please enter either C or R!\n");
							break;
						}
					}
				}

				case 3 -> {
					//maybe transfer code into method
					System.out.println("Basic damage done: " + stats.basic_attack_dmg_done);
					System.out.println("Special damage done: " + stats.special_attack_dmg_done);
					System.out.println("Basic attacks: " + stats.basic_attack_counter);
					System.out.println("Games played: " + stats.games_played);
					System.out.println("Games won: " + stats.games_won);
					System.out.println("Games lost: " + stats.games_lost);
					System.out.println("Games drawn: " + stats.games_drawn);
				}

				case 4 -> {
					return;
				}
			}
		}
	}
}