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
	public Bool eval(Env env) {
		return env.get(this);
	}

	@Override
	public String toString() {
		return name;
	}
	
	/**
	 * {@link sudoku.Set#contains(T) Set.contains(T t)} relies on {@link #equals(Object) equals},
	 * and, indirectly, so do {@link sudoku.formula.Env Env} and several
	 * test methods.
	 * 
	 * Given that Var is an immutable object, and its value is then just like a 
	 * math value, it has no identity other that it's value; that is,
	 * the {@link #name name} field. So, two different Var objects
	 * are equal only if their names are equal.
	 */
	@Override
	public boolean equals(Object o) {
		
		if (!(o instanceof Var)) {
			return false;
		}
		
		return getName().equals(((Var) o).getName());
		
	}

	@Override
	public int hashCode() {
		return getName().hashCode();
	}
	
	@Override
	String buildString(String str) {
		return str + toString();
	}
}
