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
	public boolean eval(Env env) {
		return !formula.eval(env);
	}

}
