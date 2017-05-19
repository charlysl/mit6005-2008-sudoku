package sudoku.formula;

import java.util.Iterator;

import sudoku.set.Set;

public class Env implements Iterable<Var> {

	Set<Var> trues; // contains all vars that are true
	Set<Var> falses; // constains all vars that are false
	
	private Env(Set<Var> trues, Set<Var> falses) {
		this.trues = trues;
		this.falses = falses;
	}
	
	public Env() {
		this(new Set<Var>(), new Set<Var>());
	}
	
	public Env put(Var var, Bool b) {
		
		if ((b.equals(Bool.TRUE) && trues.contains(var)) 
			|| (b.equals(Bool.FALSE) && falses.contains(var))) {
			// var had already been set to b
			return this;
		}

		return	b.equals(Bool.TRUE) 
				? new Env(trues.add(var), 	falses.remove(var))
				: new Env(trues.remove(var), falses.add(var));
	}

	public Bool get(Var var) {
		if (trues.contains(var)) {
			return Bool.TRUE;
		} else if (falses.contains(var)) {
			return Bool.FALSE;
		} else {
			// This is the whole point of defining Bool
			// instead of just using boolean, to be able 
			// to return UNDEFINED if Var not put.
			return Bool.UNDEFINED;
		}
	}

	@Override
	public String toString() {
		return "TRUE" + trues.toString() + " " + "FALSE" + falses.toString();
	}
	
	public int size() {
		return trues.size() + falses.size();
	}

	@Override
	public Iterator<Var> iterator() {
		return new Iterator<Var>() {
			
			// first iterate over trues, and then over falses
			Iterator<Var> truesIter = trues.iterator();
			Iterator<Var> falsesIter = falses.iterator();
			boolean isTrues = true;
			
			@Override
			public boolean hasNext() {
				
				// check if truesIter has been exhausted
				if (isTrues && !truesIter.hasNext()) {
					// switch to falsesIter
					isTrues = false;
				}
				
				return isTrues	? truesIter.hasNext()
								: falsesIter.hasNext();
				
			}
			
			@Override
			public Var next() {
				return isTrues	? truesIter.next()
								: falsesIter.next();
			}
			
			@Override
			public void remove() {
				// empty
			}
		};
	}
}
