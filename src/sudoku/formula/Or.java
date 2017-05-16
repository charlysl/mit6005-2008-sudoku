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
	public boolean eval(Env env) {
		return first.eval(env) || second.eval(env);
	}

}
