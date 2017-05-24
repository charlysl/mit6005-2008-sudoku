package sudoku.sat;

import org.junit.Assert;
import org.junit.Test;

import sudoku.formula.Bool;
import sudoku.formula.Env;
import sudoku.formula.Var;

public class LiteralTests {

	@Test
	public void testPosLiteralGetVar() {
		Literal plit = new PosLiteral("a");
		Literal nlit = new NegLiteral("a");
		Assert.assertEquals(Var.makeVar("a"), plit.getVar());
		Assert.assertEquals(Var.makeVar("a"), nlit.getVar());
	}	
	
	@Test
	public void testPosLiteralEval() {
		Literal plit = new PosLiteral("a");
		Literal nlit = new NegLiteral("a");
		Env env;
		
		env = new Env();
		Assert.assertEquals(Bool.UNDEFINED, plit.eval(env));
		Assert.assertEquals(Bool.UNDEFINED, nlit.eval(env));
		
		env = env.put(plit.getVar(), Bool.TRUE);
		Assert.assertEquals(Bool.TRUE, plit.eval(env));
		Assert.assertEquals(Bool.FALSE, nlit.eval(env));
		
		env = new Env();
		env = env.put(plit.getVar(), Bool.FALSE);
		Assert.assertEquals(Bool.FALSE, plit.eval(env));
		Assert.assertEquals(Bool.TRUE, nlit.eval(env));
	}
	
	@Test
	public void testToString() {
		Literal plit = new PosLiteral("a");
		Literal nlit = new NegLiteral("a");

		Assert.assertEquals("a", plit.toString());
		Assert.assertEquals("!a", nlit.toString());
	}

	@Test
	public void testBool() {
		Literal plit = new PosLiteral("a");
		Literal nlit = new NegLiteral("a");

		Assert.assertEquals(Bool.TRUE, plit.bool());
		Assert.assertEquals(Bool.FALSE, nlit.bool());		
	}

	@Test
	public void testNeg() {
		Literal plit = new PosLiteral("a");
		Literal nlit = new NegLiteral("a");

		Assert.assertEquals(Bool.FALSE, plit.neg());
		Assert.assertEquals(Bool.TRUE, nlit.neg());		
	}
}
