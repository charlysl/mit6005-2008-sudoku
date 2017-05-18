package sudoku.test.formula;

import org.junit.Assert;
import org.junit.Test;

import sudoku.formula.Bool;
import sudoku.formula.Env;
import sudoku.formula.Var;

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
}
