package sudoku.set;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import sudoku.list.Cons;
import sudoku.list.EmptyList;
import sudoku.list.List;

public class Set<T> implements Iterable<T> {
	private List<T> rep;
	private Cons<T> cons; // null only if empty
	
	// profiling showed that over 50% of the time was spent in Set#contains
	// caching is an attempt to improve this
//	private Map<T,Boolean> containsCache = new HashMap<T,Boolean>();
	
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
		// First check if result is in cache; if not then get result and cache it.
		//		
		// The cache caches both hits (mapped to true) and misses (mapped to false).
		// This is fine, because Set is immutable, so a hit will always be a hit,
		// and a miss will always be a miss.
		
//		Boolean result = containsCache.get(t);
//		if (result != null) {
//			return result;
//		}

//		Boolean result;
//		
//		if (isEmpty()) {
//			result = Boolean.FALSE;
//		} else if (cons.getFirst().equals(t)) {
//			result = Boolean.TRUE;
//		} else {
//			result = new Set<T>(cons.getRest()).contains(t);			
//		}
		
		// cache result
//		containsCache.put(t, result);
		
//		return result;


		if (isEmpty()) {
			return false;
		} else {
			return contains(t, cons);			
		}
	}
	
	private boolean contains(T t, Cons<T> list) {
		boolean result;
		
		if (list.getFirst().equals(t)) {
			result = true;
		} else {
			List<T> rest = list.getRest();
			
			if (rest instanceof EmptyList<?>) {
				result = false;
			} else {
				result = contains(t, (Cons<T>) rest);				
			}
			
		}
		
		return result;
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
	
	public int size() {
		return rep.size();
	}

	@Override
	public Iterator<T> iterator() {
				
		return new Iterator<T>() {

			// This list is a copy of rep.
			// In each iteration first will be returned,
			// and the list reduced to the rest. 
			// In the last iteration it will be empty.
			List<T> list = rep;

			@Override
			public boolean hasNext() {
				return list instanceof Cons<?>;
			}

			@Override
			public T next() {
				Cons<T> cons = (Cons<T>) list;
				T next = cons.getFirst();
				list = cons.getRest();
				return next;
			}

			@Override
			public void remove() {
				// empty
			}
		};
	}
}
