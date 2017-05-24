package sudoku.sat;

import sudoku.formula.Bool;

public class NegLiteral extends Literal {

	public NegLiteral(String name) {
		super(name);
	}

	@Override
	public String toString() {
		return "!" + super.toString();
	}

	@Override
	protected Bool getBool() {
		return Bool.FALSE;
	}
}
