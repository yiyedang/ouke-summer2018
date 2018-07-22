/**
 * This class implements the patient queue using linkedList.
 * For LinkedListPatientQueue, you cannot use java.util.LinkedList directly.
 * You should treat LinkedListPatientQueue as a special LinkedList and make it from scratch.
 */

public class LinkedListPatientQueue extends PatientQueue{
	
	private PatientNode head;
	public LinkedListPatientQueue() {
		head = null;
	}
	
	@Override
	/**
	 * Remove all elements from the patient queue, freeing memory for all nodes that are removed.
	 */
	public void clear() {
		head = null;
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
		return head.name;
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
		return head.priority;
	}

	@Override
	/**
	 * Return true if the patient queue does not contain any elements and
	 * false if it does contain at least one patient.
	 */
	public boolean isEmpty() {
		return head == null;
	}

	@Override
	/**
	 * Enqueue the given person into the patient queue with the given priority
	 */
	public void newPatient(String name, int priority) {
		PatientNode current = head;
		PatientNode newPatient = new PatientNode(name, priority);
		if(isEmpty()) {
			head = newPatient;
		}
		else {
			//if the front has lower priority than the new Patient
			if(head.priority > newPatient.priority) {
				//switch front patient with the new one
				newPatient.next = head;
				head = newPatient;
			} else {
				//go through all patients in the queue, stop until a low priority is found
				while(current.next != null && current.priority <= newPatient.priority) {
					current = current.next;
				}
				//insert the new patient
				newPatient.next = current.next;
				current.next = newPatient;
			}
		}
	}

	@Override
	/**
	 * Dequeue the patient with the most urgent priority from the queue, 
	 * and you return their name as a string. Throw a string exception if the
	 * queue does not contain any patients.
	 */
	public String processPatient() {
		if(isEmpty()) {
			return "The queue does not contain any patients. ";
		}
		String tmpName = head.name;
		head = head.next;
		return tmpName;
	}

	@Override
	/**
	 * Modify the priority of a given existing patient in the queue. The intent is to
	 * change the patient's priority to be more urgent (i.e., a smaller integer) than 
	 * its current value. Throw a string exception If the given patient is present in the
	 * queue and already has a more urgent priority than the given new priority, 
	 * or if the given patient is not already in the queue. Alter the priority of the 
	 * highest priority person with that name that was placed into the queue 
	 * if the given patient name occurs multiple times in the queue.
	 */
	public void upgradePatient(String name, int newPriority) {
		boolean present = false;
		PatientNode current = head;
		PatientNode p = null;
		if(!isEmpty()) {
			//go through the linked list
			while(current.next != null) {
				//if the patient is present
				if(current.name.equals(name)) {
					present = true;
					p = current;
					break;
				}
				current = current.next;
			}
		}
		
		if(!present) {
			System.out.println("The patient is not in the queue. ");
		} else {
			if(p.priority < newPriority) {
				System.out.println("The given patient is present in the queue and "
						+ "already has a more urgent priority than the given new priority");
			} else {
				p.priority = newPriority;
			}
		}
		
	}
	
	/**
	 * Print out the patient queue in front-to-back order.
	 */
	public String toString() {
		String str = "{";
		PatientNode current = head;
		if(!isEmpty()) {	
			while(current.next != null) {
				str += current.priority + ":" + current.name + ",";
				current = current.next;
			}
			str+=current.priority + ":" + current.name;
		}
		str += "}";
		return str;
		
	}
	
	/**
	 * Inner class for representing a single node of the linked list.
	 */
	private class PatientNode{
		private String name;
		private int priority;
		private PatientNode next;
		
		public PatientNode(String name, int priority){
			this.name = name;
			this.priority = priority;
			this.next = null;
		}
	}
}


