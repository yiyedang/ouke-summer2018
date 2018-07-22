import java.util.ArrayList;
import java.util.Scanner;

public class Hospital {

	private static Scanner stdIn = new Scanner(System.in);
	private static final int RANDOM_STRING_LENGTH = 6; // max length of random strings in bulk en/deQ

	public static void main(String[] args) {

		System.out.println("CS 106B Hospital Patient Check-in System");
		System.out.println("========================================");

		while (true) {
			System.out.print("A)rrayList, L)inkedList, H)eap?");
			String choice = stdIn.nextLine().toUpperCase().trim();
			if (choice.equals("A")) {
				ArrayListPatientQueue pq = new ArrayListPatientQueue();
				test(pq);
				break;
			} else if (choice.equals("L")) {
				LinkedListPatientQueue pq = new LinkedListPatientQueue();
				test(pq);
				break;
			} else if (choice.equals("H")) {
				HeapPatientQueue pq = new HeapPatientQueue();
				test(pq);
				break;
			}
		}

		System.out.println();
		System.out.println("Exiting.");
	}

	/**
	 * Prompts the user to perform tests on any type of queue. Each method of the
	 * queue has a corresponding letter or symbol to type that will call that method
	 * on the queue and display the results as appropriate.
	 */
	private static void test(PatientQueue queue) {
		while (true) {
			System.out.println();
			;
			System.out.println("Current patient queue:");
			System.out.println(queue);
			if (queue.isEmpty()) {
				System.out.println(" (empty)");
			} else {
				System.out.println(" (not empty)");
			}

			System.out.print("N)ew, F)ront, U)pgrade, P)rocess, B)ulk, C)lear, Q)uit?");
			String choice = stdIn.nextLine().toUpperCase().trim();
			if (choice.equals("") || choice.equals("Q")) {
				break;
			} else if (choice.equals("U")) {
				System.out.print("Name? ");
				String value = stdIn.nextLine().trim();
				System.out.print("New priority? ");
				int newPriority = Integer.parseInt(stdIn.nextLine().trim());
				queue.upgradePatient(value, newPriority);
			} else if (choice.equals("C")) {
				queue.clear();
			} else if (choice.equals("P")) {
				String value = queue.processPatient();
				System.out.println("Processing patient: \"" + value + "\"");
			} else if (choice.equals("N")) {
				System.out.print("Name? ");
				String value = stdIn.nextLine().trim();
				System.out.print("Priority? ");
				int priority = Integer.parseInt(stdIn.nextLine().trim());
				queue.newPatient(value, priority);
			} else if (choice.equals("F")) {
				String name = queue.frontName();
				int pri = queue.frontPriority();
				System.out.println("Front of line is \"" + name + "\" with priority " + pri);
			} else if (choice.equals("B")) {
				System.out.print("How many patients? ");
				int count = Integer.parseInt(stdIn.nextLine().trim());
				System.out.print("N)ew or P)rocess: ");
				String choice2 = stdIn.nextLine().toUpperCase().trim();
				if (choice2.equals("N")) {
					bulkEnqueue(queue, count);
				} else if (choice2.equals("P")) {
					bulkDequeue(queue, count);
				}
			} else if (choice.equals("TNG")) {
				easterEgg();
			}
		}
	}

	/*
	 * Enqueues the given number of patients into the queue, either in random,
	 * ascending, or descending order. Helpful for bulk testing.
	 */
	private static void bulkEnqueue(PatientQueue queue, int count) {
		System.out.print("R)andom, A)scending, D)escending? ");
		String choice2 = stdIn.nextLine().toUpperCase().trim();
		if (choice2.equals("R")) {
			for (int i = 0; i < count; i++) {
				String value = randomString(5);
				int priority = randomInteger(1, count);
				queue.newPatient(value, priority);
				System.out.println("New patient \"" + value + "\" with priority " + priority);
			}
		} else if (choice2.equals("A") || choice2.equals("D")) {
			ArrayList<String> toAdd = new ArrayList<String>();
			for (int i = 0; i < count; i++) {
				toAdd.add(randomString(RANDOM_STRING_LENGTH));
			}
			if (choice2.equals("A")) {
				for (int i = 0; i < toAdd.size(); i++) {
					String value = toAdd.get(i);
					int priority = i + 1;
					queue.newPatient(value, priority);
					System.out.println("New patient \"" + value + "\" with priority " + priority);
				}
			} else {
				for (int i = toAdd.size() - 1; i >= 0; i--) {
					String value = toAdd.get(i);
					int priority = i + 1;
					queue.newPatient(value, priority);
					System.out.println("New patient \"" + value + "\" with priority " + priority);
				}
			}
		}
	}

	/**
	 * Dequeues the given number of patients from the queue. Helpful for bulk
	 * testing.
	 */
	private static void bulkDequeue(PatientQueue queue, int count) {
		for (int i = 1; i <= count; i++) {
			String value = queue.processPatient();
			System.out.println("#" + i + ", processing patient: \"" + value + "\"");
		}
	}

	/**
	 * Returns a randomly generated string of letters up to the given length.
	 */
	private static String randomString(int maxLength) {
		int length = randomInteger(1, maxLength);
		String out = "";
		for (int i = 0; i < length; i++) {
			char ch = (char) ('a' + randomInteger(0, 25));
			out += ch;
		}
		return out;
	}

	/**
	 * returns a random integer between low and high, inclusive.
	 */
	private static int randomInteger(int low, int high) {
		return (int) (low + Math.random() * (high + 1));
	}

	/*
	 * All the following is written and created by Marty Stepp, a lecturer at
	 * Stanford University.
	 * 
	 * This assignment is about a queue, so here is a silly hidden function that
	 * prints some ASCII art and text about the character Q from the TV show,
	 * "Star Trek: The Next Generation".
	 *
	 * Does it have very much to do with this assignment? No. Did that stop me from
	 * adding it? NO.
	 *
	 * Total number of students who have mentioned to me that they found this: like
	 * 5.
	 *
	 * If you haven't seen ST:TNG, well, I don't know what to tell you. Go watch it.
	 *
	 * Text courtesy of: http://en.memory-alpha.org/wiki/Q ASCII art courtesy of:
	 * http://www.chris.com/ascii/index.php?art=television/star%20trek
	 */
	static void easterEgg() {
		System.out.println("                                _____..---========+*+==========---.._____");
		System.out.println("   ______________________ __,-='=====____  =================== _____=====`=");
		System.out.println("  (._____________________I__) - _-=_/    `---------=+=--------'");
		System.out.println("      /      /__...---===='---+---_'");
		System.out.println("     '------'---.___ -  _ =   _.-'    *    *    *   *");
		System.out.println("                    `--------'");
		System.out.println();
		System.out.println("                 _____.-----._____");
		System.out.println("    ___----~~~~~~. ... ..... ... .~~~~~~----___");
		System.out.println(" =================================================");
		System.out.println("    ~~~-----......._____________.......-----~~~");
		System.out.println("     (____)          \\   |   /          (____)");
		System.out.println("       ||           _/   |   \\_           ||");
		System.out.println("        \\\\_______--~  //~~~\\\\  ~--_______// ");
		System.out.println("         `~~~~---__   \\\\___//   __---~~~~'     ");
		System.out.println("                   ~~-_______-~~");
		System.out.println();
		System.out.println("                  xxxXRRRMMMMMMMMMMMMMMMxxx,.");
		System.out.println("              xXXRRRRRXXXVVXVVXXXXXXXRRRRRMMMRx,");
		System.out.println("            xXRRXRVVVVVVVVVVVVVVVXXXXXRXXRRRMMMMMRx.");
		System.out.println("          xXRXXXVVVVVVVVVVVVVVVVXXXXVXXXXXXRRRRRMMMMMxx.");
		System.out.println("        xXRRXXVVVVVttVtVVVVVVVVVtVXVVVVXXXXXRRRRRRRMMMMMXx");
		System.out.println("      xXXRXXVVVVVtVttttttVtttttttttVXXXVXXXRXXRRRRRRRMMMMMMXx");
		System.out.println("     XRXRXVXXVVVVttVtttVttVttttttVVVVXXXXXXXXXRRRRRRRMMMMMMMMVx");
		System.out.println("    XRXXRXVXXVVVVtVtttttVtttttittVVVXXVXVXXXRXRRRRRMRRMMMMMMMMMX,");
		System.out.println("   XRRRMRXRXXXVVVXVVtttittttttttttVVVVXXVXXXXXXRRRRRMRMMMMMMMMMMM,");
		System.out.println("   XXXRRRRRXXXXXXVVtttttttttttttttttVtVXVXXXXXXXRRRRRMMMMMMMMMMMMM,");
		System.out.println("   XXXXRXRXRXXVXXVtVtVVttttttttttttVtttVXXXXXXXRRRRRMMMMMMMMMMMMMMMR");
		System.out.println("   VVXXXVRVVXVVXVVVtttititiitttttttttttVVXXXXXXRRRRRMRMMMMMMMMMMMMMMV");
		System.out.println("   VttVVVXRXVVXtVVVtttii|iiiiiiittttttttitXXXRRRRRRRRRRMMMMMMMMMMMMMM");
		System.out.println("   tiRVVXRVXVVVVVit|ii||iii|||||iiiiiitiitXXXXXXXXRRRRRRMMMMMMMMMMMMM");
		System.out.println("    +iVtXVttiiii|ii|+i+|||||i||||||||itiiitVXXVXXXRRRRRRRRMMMMMMRMMMX");
		System.out.println("    `+itV|++|tttt|i|+||=+i|i|iiii|iiiiiiiitiVtti+++++|itttRRRRRMVXVit");
		System.out.println("     +iXV+iVt+,tVit|+=i|||||iiiiitiiiiiiii|+||itttti+=++|+iVXVRV:,|t");
		System.out.println("     +iXtiXRXXi+Vt|i||+|++itititttttttti|iiiiitVt:.:+++|+++iXRMMXXMR");
		System.out.println("     :iRtiXtiV||iVVt||||++ttittttttttttttttXXVXXRXRXXXtittt|iXRMMXRM");
		System.out.println("      :|t|iVtXV+=+Xtti+|++itiiititittttVttXXXXXXXRRRXVtVVtttttRRMMMM|");
		System.out.println("        +iiiitttt||i+++||+++|iiiiiiiiitVVVXXRXXXRRRRMXVVVVttVVVXRMMMV");
		System.out.println("         :itti|iVttt|+|++|++|||iiiiiiiittVVXRRRMMMMMMRVtitittiVXRRMMMV");
		System.out.println("           `i|iitVtXt+=||++++|++++|||+++iiiVVXVRXRRRV+=|tttttttiRRRMMM|");
		System.out.println("             i+++|+==++++++++++++++|||||||||itVVVViitt|+,,+,,=,+|itVX'");
		System.out.println("              |+++++.,||+|++++=+++++++|+|||||iitt||i||ii||||||itXt|");
		System.out.println("              t||+++,.=i+|+||+++++++++++++|i|ittiiii|iiitttttXVXRX|");
		System.out.println("              :||+++++.+++++++++|++|++++++|||iii||+:,:.-+:+|iViVXV");
		System.out.println("              iii||+++=.,+=,=,==++++++++++|||itttt|itiittXRXXXitV'");
		System.out.println("             ;tttii||++,.,,,.,,,,,=++++++++++|iittti|iiiiVXXXXXXV");
		System.out.println("            tVtttiii||++++=,,.  . ,,,=+++++++|itiiiiiii||||itttVt");
		System.out.println("           tVVttiiiii||||++++==,. ..,.,+++=++iiiiiitttttVVXXRRXXV");
		System.out.println("        ..ttVVttitttii||i|||||+|+=,.    .,,,,==+iittVVVXRRMXRRRV");
		System.out.println("...'''ittitttttitVttttiiiiii|ii|++++=+=..... ,.,,||+itiVVXXVXV");
		System.out.println("      ,|iitiiitttttttiiiii||ii||||||||+++++,.i|itVt+,,=,==.........");
		System.out.println("        ,|itiiiVtVtiii||iiiiii|||||||++||||tt|VXXRX|  ....  ..     ' ' '.");
		System.out.println("          ,,i|ii||i||+|i|i|iiiiiiii||||ittRVVXRXRMX+, .  ...   .         ,");
		System.out.println("    .       .,+|++|||||ii|i|iiiitttVVttXVVXVXRRRRXt+. .....  . .       ,. .");
		System.out.println("  . .          ,,++|||||||i|iiitVVVXXXXVXXVXXRRRV+=,.....  ....  ..       ..");
		System.out.println("                  .,,++|||i|iittXXXXRMViRXXXXRVt+=, ..    ...... .        ..");
		System.out.println("                   ,XX+.=+++iitVVXXXRXVtXXVRRV++=,..... .,, .              .");
		System.out.println("            ....       +XX+|i,,||tXRRRXVXti|+++,,. .,,. . . .. .      . ....");
		System.out.println("  . .          .      ..  ..........++,,..,...,.... ..             .. ...");
		System.out.println();
		System.out.println("Q was a highly powerful entity from a race of omnipotent, godlike \n"
				+ "beings also known as the Q. Q appeared to the crews of several \n"
				+ "Starfleet vessels and outposts during the 2360s and 2370s. \n"
				+ "All command level officers in Starfleet are briefed on his \n"
				+ "existence. One such briefing was attended by Benjamin Sisko \n"
				+ "in 2367. He typically appears as a Humanoid male (though \n"
				+ "he can take on other forms if he wishes), almost \n"
				+ "always dressed in the uniform of a Starfleet captain.");
		System.out.println("In every appearance, he demonstrated superior capabilities, but \n"
				+ "also a mindset that seemed quite unlike what Federation scientists \n"
				+ "expected for such a powerful being. He has been described, in \n"
				+ "turn, as 'obnoxious,' 'interfering,' and a 'pest'. However, \n"
				+ "underneath his acerbic attitude, there seemed to be a hidden \n"
				+ "agenda to Q's visits that often had the best interests of \n" + "Humanity at their core.");
		System.out.println();
		System.out.println("\"Worf... Eat any good books lately?\" -- Q");
	}
}
