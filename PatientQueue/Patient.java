
public class Patient implements Comparable<Patient>{

	/* instance variables */
	private String name;           // store patient's name
	private int priority;          // store patient's priority, the smaller the higher the priority

	/**
	 * Construct a patient node with a patient's name and priority.
	 * @param name
	 * @param priority
	 */
	public Patient(String name, int priority) {
		this.setName(name);
		this.setPriority(priority);
	}
	
	/**
	 * Get the name of the patient.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name of the patient.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the priority of the patient.
	 */
	public int getPriority() {
		return priority;
	}

	/**
	 * Set the priority of the patient.
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	/**
	 * A String representation of a patient
	 */
	public String toString() {
		return priority + ":" + name;
	}
	
	/**
	 * This method indicates the calculation of priority of patients.
	 * The smaller the priority value, the higher the priority.
	 */
	public int compareTo(Patient other) {
		if(this.priority < other.priority) {
			return 1;
		}else if(this.priority > other.priority) {
			return -1;
		}else {
			return 0;
		}
	}
}
