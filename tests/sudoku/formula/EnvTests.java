package sudoku.formula;

import org.junit.Assert;
import org.junit.Test;

import sudoku.formula.Bool;
import sudoku.formula.Env;
import sudoku.formula.Var;
import sudoku.set.Set;

public class EnvTests {
	
	@Test
	public void testPutAndGet() {
		Env env = new Env();
		Var var = Var.makeVar("a");

		env = env.put(var,Bool.TRUE);
		Assert.assertEquals(Bool.TRUE, env.get(var));

		env = env.put(var,Bool.TRUE);
		Assert.assertEquals(Bool.TRUE, env.get(var));

		// this Env implementation assumes
		// that a Var is put only once
//		env = env.put(var,Bool.FALSE);
//		Assert.assertEquals(Bool.TRUE, env.get(var));

		Var var2 = Var.makeVar("b");

		env = env.put(var2,Bool.FALSE);
		Assert.assertEquals(Bool.FALSE, env.get(var2));

		env = env.put(var2,Bool.TRUE);
		Assert.assertEquals(Bool.TRUE, env.get(var2));

//		env = env.put(var,Bool.FALSE);
//		Assert.assertEquals(Bool.TRUE, env.get(var));
		
		Assert.assertEquals(Bool.UNDEFINED, env.get(Var.makeVar("c")));
	}
	
	@Test
	public void testIterable() {
		Env env = new Env();
		for (Var v : env) {
		}
		
		Var v = Var.makeVar("a");
		env = env.put(v, Bool.TRUE);
		int c = 0;
		for (Var e : env) {
			Assert.assertEquals(v, e);
			c++;
		}
		Assert.assertEquals(1, c);
		
		env = env.put(v, Bool.FALSE);
		c = 0;
		for (Var e : env) {
			Assert.assertEquals(v, e);
			c++;
		}
		// this Env implementation assumes
		// that a Var is put only once
//		Assert.assertEquals(2, c);
	}	
}
