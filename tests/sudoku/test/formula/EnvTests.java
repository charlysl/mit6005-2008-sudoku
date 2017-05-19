package sudoku.test.formula;

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
		Var var = new Var("a");

		env = env.put(var,Bool.TRUE);
		Assert.assertEquals(Bool.TRUE, env.get(var));

		env = env.put(var,Bool.TRUE);
		Assert.assertEquals(Bool.TRUE, env.get(var));

		env = env.put(var,Bool.FALSE);
		Assert.assertEquals(Bool.FALSE, env.get(var));

		Var var2 = new Var("b");

		env = env.put(var2,Bool.FALSE);
		Assert.assertEquals(Bool.FALSE, env.get(var2));

		env = env.put(var2,Bool.TRUE);
		Assert.assertEquals(Bool.TRUE, env.get(var2));

		env = env.put(var,Bool.FALSE);
		Assert.assertEquals(Bool.FALSE, env.get(var));
		
		Assert.assertEquals(Bool.UNDEFINED, env.get(new Var("c")));		
	}
	
	@Test
	public void testIterable() {
		Env env = new Env();
		for (Var v : env) {
		}
		
		Var v = new Var("a");
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
		Assert.assertEquals(1, c);
	}	
}
