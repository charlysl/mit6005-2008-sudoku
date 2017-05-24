package sudoku.formula;

import org.junit.Assert;
import org.junit.Test;

import sudoku.formula.Bool;
import sudoku.formula.Env;
import sudoku.formula.Var;
import sudoku.set.Set;

public class VarTests {

	@Test
	public void testConstructor() {
		Var f = Var.makeVar("a");
		Assert.assertEquals("a", f.getName());
	}
	
	@Test
	public void testVars() {
		Var f = Var.makeVar("a");
		Set<Var> vars = f.vars();
		
		// check that f is in vars
		Assert.assertTrue(vars.contains(f));
	}
	
	@Test 
	public void testEval() {
		Var f = Var.makeVar("a");
		
		Env env = new Env().put(f, Bool.TRUE);
		Assert.assertEquals(Bool.TRUE, f.eval(env));
		
		env = new Env().put(f, Bool.FALSE);
		Assert.assertEquals(Bool.FALSE, f.eval(env));

		env = new Env();
		Assert.assertEquals(Bool.UNDEFINED, f.eval(env));
	}
	
	@Test
	public void testSolveVarFormula() {
		Var f = Var.makeVar("a");
		Env env = f.solve();
		Assert.assertEquals(Bool.TRUE, env.get(f));
		
		
	}
	
	@Test
	public void testEquals() {
		Var a = Var.makeVar("A");
		Assert.assertEquals(a, a);
		
		Var b = Var.makeVar("A");
		Assert.assertEquals(a, b);
		Assert.assertEquals(b, a);
		
		Var c = Var.makeVar("C");
		Assert.assertNotEquals(a, c);
		Assert.assertNotEquals(c, a);
	}

	@Test
	public void testHashCode() {
		Var a = Var.makeVar("A");
		Assert.assertEquals(a.hashCode(), "A".hashCode());
		
		Var b = Var.makeVar("A");
		Assert.assertEquals(a.hashCode(), b.hashCode());
		Assert.assertEquals(b.hashCode(), a.hashCode());
		
		Var c = Var.makeVar("C");
		Assert.assertNotEquals(a.hashCode(), c.hashCode());
		Assert.assertNotEquals(c.hashCode(), a.hashCode());
	}
	
	@Test
	public void testToString() {
		Assert.assertEquals("a", Var.makeVar("a").toString());
	}
}
