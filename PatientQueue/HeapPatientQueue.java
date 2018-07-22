
/**
 * This class implements the patient queue using heap.
 * @author yiyedang
 *
 */
public class HeapPatientQueue extends PatientQueue{
	private Patient[] queue;
	private int size;
	private int capacity;
	
	public HeapPatientQueue() {
		capacity = 10;
		size = 0;
		queue = new Patient[capacity];
	}
	
	@Override
	/**
	 * Remove all elements from the patient queue, freeing memory for all nodes that are removed.
	 */
	public void clear() {
		queue = new Patient[10];
		capacity = 10;
		size = 0;
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
		return queue[1].getName();
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
		return queue[1].getPriority();
	}

	@Override
	/**
	 * Return true if the patient queue does not contain any elements and
	 * false if it does contain at least one patient.
	 */
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	/**
	 * Enqueue the given person into the patient queue with the given priority
	 */
	public void newPatient(String name, int priority) {
		 // if the heap is full
		if(size == queue.length) {
			Patient[] biggerQueue = new Patient[queue.length *2];
			//copying the content of the original array to the bigger array
			for(int i = 1; i < queue.length; i++) {
				biggerQueue[i] = queue[i]; 
			}
			//expand the heap
			queue = biggerQueue;
		}
		
		size++;
		queue[size] = new Patient(name, priority);
		int current = size;
		traceUp(current);
		
	}

	private void traceUp(int current) {
		// while the current element is not the root
		while(current != 1) {
			//if there is a tie in priority between current and its parent
			if(queue[current].compareTo(queue[current/2]) == 0) {
				//if the the current element comes earlier in the alphabet
				if(queue[current].getName().compareTo(queue[current/2].getName()) < 0) {
					// swap it with its parent
					Patient tmp = queue[current/2];
					queue[current/2] = queue[current];
					queue[current] = tmp;
				}
			} 
			//if the current element has higher priority than its parent
			if(queue[current].compareTo(queue[current/2]) > 0){
				// swap the current element with its parent
				Patient tmp = queue[current/2];
				queue[current/2] = queue[current];
				queue[current] = tmp;
			}
			current = current / 2;
		}
	}
	
	private void traceDown(int current) {
		//while current element is not a leaf
		while(current <= size/2) {
			int urgentChild = findUrgentChild(current);
			//if current has lower priority than the more urgent child
			if(queue[current].compareTo(queue[urgentChild]) < 0) {
				//swap with the child
				Patient tmp = queue[current];
				queue[current] = queue[urgentChild];
				queue[urgentChild] = tmp;
			}
			//if current has equal priority to the more urgent child
			if(queue[current].compareTo(queue[urgentChild]) == 0) {
				//if the child comes earlier in the alphabet
				if(queue[current].getName().compareTo(queue[urgentChild].getName()) > 0) {
					//swap with the child
					Patient tmp = queue[current];
					queue[current] = queue[urgentChild];
					queue[urgentChild] = tmp;
				}
			}
			current = current * 2;
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
		} else {
			// return root and swap the last leaf to root
			String value = queue[1].getName();
			queue[1] = queue[size];
			queue[size] = null;
			size--;
			int current = 1;
			traceDown(current);
			return value;
		}
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
		int mark = 0;
		Patient p = null;
		if(!isEmpty()) {
			//go through the array list
			for(int i = 1; i <= size; i++) {
				//if the patient is present
				if(queue[i].getName().equals(name)) {
					present = true;
					p = queue[i];
					mark = i;
				}
			}
		}		
		
		if(!present) {
			System.out.println("The patient is not in the queue. ");
		} else {
			if(p.getPriority() < newPriority) {
				System.out.println("The given patient is present in the queue and "
						+ "already has a more urgent priority than the given new priority");
			} else {
				p.setPriority(newPriority);
				if(mark != 1 && queue[mark].compareTo(queue[mark / 2]) >= 0) {
					traceUp(mark);
				} else if(mark <= size /2 && queue[mark].compareTo(queue[findUrgentChild(mark)]) <= 0) {
					traceDown(mark);
				}
			}
		}
	}
	
	private int findUrgentChild(int current) {
		//if there is no right child
		if(current * 2 + 1 > size) {
			return  current * 2;
		} else {
			//if the left child has higher priority
			if(queue[current * 2].compareTo(queue[current * 2 + 1]) > 0) {
				return current * 2;
			}
			//else if the right child has higher priority
			else if(queue[current * 2].compareTo(queue[current * 2 + 1]) < 0){
				return current * 2 + 1;
			}
			//else if there is a tie for priority between children
			else {
				//if the right child comes earlier in the alphabet
				if(queue[current * 2].getName().compareTo(queue[current * 2 + 1].getName()) > 0){
					return current * 2 + 1;
				} else {
					return current * 2;
				}
			}
		}
	}
	/**
	 * Print out the patient queue in front-to-back order.
	 */
	public String toString() {
		String str = "{";
		if(!isEmpty()) {	
			Patient[] tmp = new Patient[queue.length]; // a copy of the original queue
			for(int i = 1; i <= size; i ++) {
				tmp[i] = queue[i];
			}
			
			int current = 1;
			//while current element is not a leaf
			while(current <= size/2) {
				int urgentChild = findUrgentChild(current);
				//if the right child is more urgent
				if(urgentChild == current * 2 + 1) {
					//swap it with the left child to print it first
					Patient p = tmp[urgentChild];
					tmp[urgentChild] = tmp[urgentChild - 1];
					tmp[urgentChild - 1] = p;
				}
				current = current * 2;
			}
			
			for(int i = 1; i < size; i++) {
				str += tmp[i].getPriority() + ":" + tmp[i].getName() + ",";
			}
			str += tmp[size].getPriority() + ":" + tmp[size].getName();
			
		}
		str += "}";
		return str;
	}
}
