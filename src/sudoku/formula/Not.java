package sudoku.formula;

import sudoku.set.Set;

public class Not extends Formula {

	private Formula formula;
	
	public Not(Formula formula) {
		this.formula = formula;
	}
	
	public Formula getFormula() {
		return formula;
	}

	@Override
	public Set<Var> vars() {
		return formula.vars();
	}

	@Override
	public Bool eval(Env env) {
		return formula.eval(env).not();
	}

	@Override
	String buildString(String str) {
		return str + "'" + formula;
	}

	@Override
	public String toString() {
		return buildString("");
	}
}
