package sudoku.formula;

import sudoku.set.Set;

public class Var extends Formula {

	private String name;
	
	public Var(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	@Override
	public Set<Var> vars() {
		return new Set<Var>(this);
	}

	@Override
	public boolean eval(Env env) {
		return env.get(this);
	}

	@Override
	public String toString() {
		return name;
	}
}
