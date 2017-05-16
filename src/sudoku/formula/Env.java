package sudoku.formula;

import sudoku.set.Set;

public class Env {

	Set<Var> trues; // contains all vars that are true
	Set<Var> falses; // constains all vars that are false
	
	private Env(Set<Var> trues, Set<Var> falses) {
		this.trues = trues;
		this.falses = falses;
	}
	
	public Env() {
		this(new Set<Var>(), new Set<Var>());
	}
	
	public Env put(Var var, boolean b) {
		
		if ((b && trues.contains(var)) || (!b && falses.contains(var))) {
			// var had already been set to b
			return this;
		}

		return	b	? new Env(trues.add(var), 	falses.remove(var))
					: new Env(trues.remove(var), falses.add(var));
	}

	public boolean get(Var var) {
		if (trues.contains(var)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return "TRUE" + trues.toString() + " " + "FALSE" + falses.toString();
	}
}
