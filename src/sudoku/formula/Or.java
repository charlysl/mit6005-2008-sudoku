package sudoku.formula;

import sudoku.set.Set;

public class Or extends Formula {

	private Formula first;
	private Formula second;
	
	public Or(Formula first, Formula second) {
		this.first = first;
		this.second = second;
	}

	public Formula getFirst() {
		return first;
	}
	
	public Formula getSecond() {
		return second;
	}
	
	@Override
	public Set<Var> vars() {
		return first.vars().addAll(second.vars());
	}

	@Override
	public Bool eval(Env env) {
		return first.eval(env).or(second.eval(env));
	}

	@Override
	public String toString() {
		return "{" + buildString("") + "}";
	}

	@Override
	String buildString(String str) {
		return getSecond().buildString(getFirst().buildString(str));
	}
}
