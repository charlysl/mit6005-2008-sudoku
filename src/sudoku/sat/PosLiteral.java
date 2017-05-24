package sudoku.sat;

import sudoku.formula.Bool;

public class PosLiteral extends Literal {

	public PosLiteral(String name) {
		super(name);
	}

	@Override
	protected Bool getBool() {
		return Bool.TRUE;
	}

}
