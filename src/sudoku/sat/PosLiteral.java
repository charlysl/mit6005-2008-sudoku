package sudoku.sat;

import sudoku.formula.Bool;
import sudoku.formula.Env;
import sudoku.formula.Var;

public class PosLiteral extends Literal {

	public PosLiteral(String name) {
		super(name);
	}

	@Override
	protected Bool getBool() {
		return Bool.TRUE;
	}

}
