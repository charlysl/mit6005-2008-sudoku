package sudoku.sat;

import sudoku.formula.Bool;
import sudoku.formula.Env;
import sudoku.formula.Var;
import sudoku.set.Set;

public abstract class Literal extends SatFormula {

	private Var var;
	
	protected Literal(String name) {
		var = Var.makeVar(name);
	}
	
	public Var getVar() {
		return var;
	}

	public Bool eval(Env env) {
		
		Bool b = env.get(getVar());
		
		if (b.equals(Bool.UNDEFINED)) {
			return Bool.UNDEFINED;
		}
		
		if (b.equals(bool())) {
			return Bool.TRUE;
		} else {
			return Bool.FALSE;
		}
		
	}
	
	@Override
	public String toString() {
		return var.toString();
	}

	public Bool bool() {
		return getBool();
	}

	protected abstract Bool getBool();

	public Bool neg() {
		return bool().not();
	}
}
