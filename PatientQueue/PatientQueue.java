/**
 * PatientQueue is an abstract that provides many methods to be 
 * implemented by its sub-classes. What needs to be mentioned is
 * that patient with the highest priority actually has the smallest
 * integer number in priority field.
 * @author hanjiedeng
 * @version 2018/05/20
 */
public abstract class PatientQueue {

	/**
	 * Clear the patient queue.
	 */
	public abstract void clear();

	/**
	 * Return the name of the patient with the highest priority.
	 * @return the name of the patient with the highest priority.
	 */
	public abstract String frontName();

	/**
	 * Return the priority of the patient with the highest priority.
	 * @return the priority of the patient with the highest priority.
	 */
	public abstract int frontPriority();

	/**
	 * See if the patient queue is empty.
	 * @return true if the patient queue is empty; false if not.
	 */
	public abstract boolean isEmpty();

	/**
	 * Enqueue a new patient into the patient queue.
	 * @param name the name of the patient
	 * @param priority the priority of the patient
	 */
	public abstract void newPatient(String name, int priority);

	/**
	 * Dequeue a new patient from the patient queue.
	 * @return the name of the patient to be dealt with
	 */
	public abstract String processPatient();

	/**
	 * Change a patient's priority.
	 * @param name the name of the patient
	 * @param newPriority the new priority of the patient
	 */
	public abstract void upgradePatient(String name, int newPriority);
}
