package griffith;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.ObjectStreamClass;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.Random;

public class assignmentOne {

	// static int random_fighter = new Random().nextInt(1,5);
	static Scanner scanner = new Scanner(System.in);
	static String[] available_attacks = { "Basic attack", "Special attack" };
	static int basic_attacks_needed = 3;
	final static boolean COUNT_METER = true;
	final static boolean NO_METER = false;

	// maybe let the computer to play the same character as the player and in that
	// case just delete this
	public static String[] removeChosenCharacter(String[] array, int remove_index) {
		/*
		 * if (array == null || remove_index < 0 || remove_index >= array.length){
		 * return array;
		 * } idk what this si for check later
		 */

		String updated_fighter_list[] = new String[array.length - 1];

		// Copy the elements except the index
		// from original array to the other array
		for (int i = 0, k = 0; i < array.length; i++) {
			if (i == remove_index)
				continue;

			updated_fighter_list[k++] = array[i];
		}
		return updated_fighter_list;
	}

	public static void basicAttack(Fighter attacker, Fighter defender, boolean count) {
		defender.setHp(defender.getHp() - (attacker.getAtk() / defender.getDef()));
		if (count) {
			attacker.setBasicAttackCount(attacker.getBasicAttacksCount() + 1);
		}
	}

	public static void specialAttack(Fighter attacker, Fighter defender) {
		if (attacker.getChampion().equals("Aatrox")) { // ask prof if we can use enums
			defender.setHp(defender.getHp() - (attacker.getAtk() / defender.getDef()) * 2);
			attacker.setHp(attacker.getHp() + 15);
		} else if (attacker.getChampion().equals("Kayle")) {
			defender.setHp(defender.getHp() - (attacker.getAtk() / (defender.getDef() / 2)));

		} else if (attacker.getChampion().equals("Kayn")) {
			defender.setDef(defender.getDef() - 1); // maybe modify how much defence they lose later on. TODO also fix
													// ArithmeticException :3
			basicAttack(attacker, defender, NO_METER);

		} else if (attacker.getChampion().equals("Pantheon")) {
			attacker.setAtk(attacker.getAtk() + 5); // maybe modify how much attack they gain later on
			basicAttack(attacker, defender, NO_METER);
		}

		// continue
	}

	public static boolean combatLogic(String[] fighter_options, int fighter_choice, int random_fighter) {
		int turn = 1;
		boolean player_won = false;
		Fighter player_figther = new Fighter(100, 500, 5, fighter_options[fighter_choice]);
		Fighter bot_figther = new Fighter(100, 10, 5, fighter_options[random_fighter]);
		do {
			System.out.println(String.format("\nTurn %s", (turn)));
			// ask player what attack they wanna use: basic, special etc
			System.out.println("Choose attack type!");
			for (int i = 0; i < available_attacks.length; i++) {
				System.out.print(String.format("%d.%s ", (i + 1), available_attacks[i]));
			}
			// System.out.println("1.Basic Attack, 2.Special Attack"); // TODO make this
			// into ASCII later
			int attack_choice;
			try {
				attack_choice = scanner.nextInt();
			} catch (InputMismatchException e) {
				System.out.println("\nPlease use numbers for the attack choice!");
				scanner.nextLine();
				continue;
			}
			scanner.nextLine();
			if (attack_choice > 0 && attack_choice <= 2) { // TODO adjust attack_choice range once
															// we
															// got all the attack types
				switch (attack_choice) { // player attacks
					case 1 -> {
						basicAttack(player_figther, bot_figther, COUNT_METER);
						System.out.println(
								"basic attack count is: " + player_figther.getBasicAttacksCount()); // remove
																									// after
																									// testing
																									// is
																									// complete
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
							specialAttack(player_figther, bot_figther);
							player_figther.setBasicAttackCount(
									player_figther.getBasicAttacksCount() - basic_attacks_needed); // special
																									// attacks
																									// consume
																									// the
																									// basic-attack
																									// meter
							// System.out.println("basic attack count value after special attack is:
							// " +
							// player_figther.getBasicAttacksCount());
						}
						break;
					}
				}
				int random_attack = new Random().nextInt(1, 3); // TODO adjust range once all
																// attacks
																// have been added. num range 1-2.
																// consider moving it to main while
																// loop
				switch (random_attack) {
					case 1 -> {
						basicAttack(bot_figther, player_figther, COUNT_METER);
						break;
					}
					case 2 -> {
						if (bot_figther.getBasicAttacksCount() != basic_attacks_needed) {
							basicAttack(bot_figther, player_figther, COUNT_METER);
						} else {
							specialAttack(bot_figther, player_figther);
							bot_figther.setBasicAttackCount(
									bot_figther.getBasicAttacksCount() - basic_attacks_needed); // special
																								// attacks
																								// consume
																								// the
																								// basic-attack
																								// meter
						}
						break;
					}
				}
				System.out.println(
						String.format("Bot chose to use %s!\n",
								available_attacks[random_attack - 1]));
			} else {
				System.out.println("Please choose a viable attack!");
				continue;
			}
			// increase turn count by 1
			turn++;
			// print out updated player and bot stats
			System.out.println(String.format("Player Stats: Hp: %s, Atk: %s, Def: %s",
					player_figther.getHp(), player_figther.getAtk(), player_figther.getDef()));
			System.out.println(String.format("Bot Stats: Hp: %s, Atk: %s, Def: %s\n",
					bot_figther.getHp(), bot_figther.getAtk(), bot_figther.getDef()));

			if (player_figther.getHp() <= 0) {
				player_won = false; // idk maybe edit this or player_won initialization
			} else if (bot_figther.getHp() <= 0) {
				player_won = true;
			}

		} while (player_figther.getHp() > 0 && bot_figther.getHp() > 0); // keep going until either player or bot wins
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
	
	public static void main(String[] args) {

		String[] fighter_options = { "Aatrox", "Kayn", "Kayle", "Pantheon" }; // TODO ASCII art of champions + their
																				// names in borders
		while (true) {

			int random_fighter = new Random().nextInt(1, 4); // num range: 1-3

			/*
			 * File myObj = new File("src/files/menu.txt");
			 * 
			 * try (Scanner myReader = new Scanner(myObj)) {
			 * while (myReader.hasNextLine()) {
			 * String data = myReader.nextLine();
			 * System.out.println(data);
			 * }
			 * } 3
			 * catch (FileNotFoundException e) {
			 * System.out.println("An error occurred.");
			 * e.printStackTrace();
			 * }
			 */

			System.out.println("Choose options from 1 to 5: "); // TODO maybe make the 1-5, not static
			int menu_choice;

			try {
				menu_choice = scanner.nextInt(); // TODO. players are able to enter an out of range number and the
													// program prompts them to enter a choice again, which is fine but
													// maybe consider giving players an error message
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
					while (true) { // in case the user uses a character that's not a number for the character
									// choise, we'll keep asking them to do so
						if (random_choice_or_not.equals("C")) {
							System.out.println("You can choose from: \n");
							for (int i = 0; i < fighter_options.length; i++) {
								System.out.println((i + 1) + "." + fighter_options[i]);
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

							try {
								System.out.println(String.format("Player fighter: %s \nBot fighter: %s",
										fighter_options[fighter_choice], fighter_options[random_fighter]));
							} catch (ArrayIndexOutOfBoundsException e) {
								System.out.println("Choice out of range!");
								scanner.nextLine();
								continue;
							}
							scanner.nextLine();

							if (combatLogic(fighter_options, fighter_choice, random_fighter)) { 
								System.out.println("Player won!");
							} else {
								System.out.println("Bot won!");
							}

							String fight_on;
							System.out.print("\nWould you like to continue fighting(r) or click any other key to go back to menu? ");
							fight_on = scanner.nextLine().replaceAll("\\s+", "").toLowerCase();

							if (continue_fight(fight_on)) {
								continue;
							} else {
								break;
							}
						}

						else if (random_choice_or_not.equals("R")) {
							// TODO write logic
							System.out.println(fighter_options[random_fighter]);
							// maybe add a break;
						}

						else {
							// prints out error message and restarts the main loop
							System.out.println("Please enter either C or R!\n");
							break;
						}
					}
				}

				case 3 -> {
					// settings
				}

				case 4 -> {
					// player statistics
				}

				case 5 -> {
					return;
				}
			}

			// return;
		}
	}

}
