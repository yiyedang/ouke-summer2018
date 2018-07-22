import java.util.ArrayList;

/**
 *  This class implements the patient queue using arryList.
 * For ArrayListPatientQueue, you can use java.util.ArrayList directly.
 * This will help save you a lot of time in implementing an ArrayList from scratch.
 */
public class ArrayListPatientQueue extends PatientQueue{
	private ArrayList<Patient> queue;
	
	public ArrayListPatientQueue() {
		this.queue = new ArrayList<Patient>();
	}
	
	@Override
	/**
	 * Remove all elements from the patient queue, freeing memory for all nodes that are removed.
	 */
	public void clear() {
		queue.clear();
	}

	@Override
	/**
	 * Return the name of the most urgent patient without removing it or altering 
	 * the state of the queue (i.e., peek name). 
	 * Throw a string exception if the queue does not contain any patients.
	 */
	public String frontName() {
		if(isEmpty()) {
			return "The queue does not contain any pateints. ";
		}
		return findFront().getName();
	}

	@Override
	/**
	 * Return the integer priority of the most urgent patient without removing it or
	 * altering the state of the queue (i.e., peek name). 
	 * Throw a string exception if the queue does not contain any patients.
	 */
	public int frontPriority() {
		if(isEmpty()) {
			System.out.println("The queue does not contain any pateints. ");
			return -1;
		}
		return findFront().getPriority();
	}

	@Override
	/**
	 * Return true if the patient queue does not contain any elements and
	 * false if it does contain at least one patient.
	 */
	public boolean isEmpty() {
		return queue.isEmpty();
	}

	@Override
	/**
	 * Enqueue the given person into the patient queue with the given priority
	 */
	public void newPatient(String name, int priority) {
		queue.add(new Patient(name, priority));	
	}

	@Override
	/**
	 * Dequeue the patient with the most urgent priority from the queue, 
	 * and you return their name as a string. Throw a string exception if the
	 * queue does not contain any patients.
	 */
	public String processPatient() {
		if(isEmpty()) {
			return "The queue does not contain any pateints. ";
		}
		
		Patient front = findFront();
		queue.remove(front);
		return front.getName();
	}

	@Override
	/**
	 * Modify the priority of a given existing patient in the queue. The intent is to
	 * change the patient's priority to be more urgent (i.e., a smaller integer) than 
	 * its current value. Throw a string exception if the given patient is present in the
	 * queue and already has a more urgent priority than the given new priority, 
	 * or if the given patient is not already in the queue. Alter the priority of the 
	 * highest priority person with that name that was placed into the queue 
	 * if the given patient name occurs multiple times in the queue.
	 */
	public void upgradePatient(String name, int newPriority) {
		boolean present = false;
		// an arrayList storing possible patients to be updated
		ArrayList<Patient> pledge = new ArrayList<Patient>();
		if(!isEmpty()) {
			//go through the array list
			for(int i = 0; i < queue.size(); i++) {
				//if the patient is present
				if(queue.get(i).getName().equals(name)) {
					present = true;
					pledge.add(queue.get(i));
				}
			}
		}
		
		if(!present) {
			System.out.println("The patient is not in the queue. ");
		} else {
			Patient p = pledge.get(0);
			//go through the possible ones
			for(int i = 0; i < pledge.size(); i++) {
				//get the one with the highest priority
				if(pledge.get(i).compareTo(p) > 0) {
					p = pledge.get(i);
				}
			}
			if(p.getPriority() < newPriority) {
				System.out.println("The given patient is present in the queue and "
						+ "already has a more urgent priority than the given new priority");
			} else {
				p.setPriority(newPriority);
			}
		}
		
	}
	
	/**
	 * Print out the patient queue in front-to-back order.
	 */
	public String toString() {
		String str = "{";
		if(!isEmpty()) {
			for(int i = 0; i < queue.size() - 1; i++) {
				Patient p = queue.get(i);
				str += p.getPriority() + ":" + p.getName() + ",";
			}
			str += queue.get(queue.size() - 1).getPriority() + ":" + queue.get(queue.size() - 1).getName();
		}
		str += "}";
		return str;
		
	}
	
	/**
	 * Search for the front patient.
	 * @return
	 */
	private Patient findFront() {
		Patient front = queue.get(0);
		for(int i = 0; i < queue.size(); i++) {
			if(queue.get(i).compareTo(front) > 0) {
				front = queue.get(i);
			}
		}
		return front;
		
	}
}
