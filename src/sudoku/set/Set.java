package sudoku.set;

import sudoku.list.Cons;
import sudoku.list.EmptyList;
import sudoku.list.List;

public class Set<T> {
	private List<T> rep;
	private Cons<T> cons; // null only if empty
	
	public Set(T t) {
		rep = new Cons<T>(t, new EmptyList<T>());
		setCons();
	}

	private Set(List<T> list) {
		rep = list;
		setCons();
	}
	
	private Set(T t, Set<T> set) {
		rep = new Cons<T>(t,set.rep);
		setCons();
	}

	public Set() {
		rep = new EmptyList<>();
		setCons();
	}

	private void setCons() {
		// invariant
		if (rep instanceof EmptyList) {
			cons = null;
		} else {
			cons = (Cons<T>) rep;
		}		
	}

	public boolean isEmpty() {		
		return cons == null;
	}

	public T pick() {
		// There are no conditions on exactly what element is returned
		
		// return null if empty set
		if (isEmpty()) {
			return null;
		}
		
		// Lets pick the first element in the set for simplicity
		return ((Cons<T>) rep).getFirst();
	}

	public Set<T> remove(T t) {
		
		// return this if empty
		if (isEmpty()) {
			return this;
		}
		
		T first = cons.getFirst();
		Set<T> s = new Set<T>(cons.getRest());
		
		if (t.equals(first)) {
			return s;
		} else {
			return new Set<T>(new Cons<T>(first, s.remove(t).rep));
		}
		
	}

	public Set<T> add(T t) {
		if (contains(t)) {
			return this;
		}
		
		return new Set<T>(t, this);
	}

	public boolean contains(T t) {
		if (isEmpty()) {
			return false;
		}
		
		if (cons.getFirst().equals(t)) {
			return true;
		}
		
		return new Set<T>(cons.getRest()).contains(t);
	}
	
	@Override
	public String toString() {
		return rep.toString();
	}

	public Set<T> addAll(Set<T> set2) {
		
		if (set2.isEmpty()) {
			return this;
		}
		
		T first = set2.cons.getFirst();
		return add(first).addAll(set2.remove(first));
	}
}
