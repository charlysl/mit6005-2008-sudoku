package sudoku.latinsquares;

import org.junit.Assert;
import org.junit.Test;

import sudoku.formula.Bool;
import sudoku.formula.Env;
import sudoku.formula.Var;
import sudoku.sat.SatFormula;

public class LatinSquaresSatTests {

	@Test 
	public void testAtLeast() {
		LatinSquaresSat l = new LatinSquaresSat(2);
		SatFormula sat;
		
		sat = new SatFormula();
		sat = l.getAtLeastFormula(sat, 0, true);
		Assert.assertEquals("{C110C100}{C010C000}", sat.toString());
		
		sat = new SatFormula();
		sat = l.getAtLeastFormula(sat, 1, false);
		Assert.assertEquals("{C111C011}{C101C001}", sat.toString());
	}
	
	@Test
	public void testAtMost() {
		LatinSquaresSat l = new LatinSquaresSat(2);
		SatFormula sat;
		
		sat = new SatFormula();
		sat = l.getAtMostFormula(sat, 0, true);
		Assert.assertEquals("{Zc110Zc100}{C110!Zc110}{!C100!Zc110}{!C110!Zc100}{C100!Zc100}{Zc010Zc000}{C010!Zc010}{!C000!Zc010}{!C010!Zc000}{C000!Zc000}"
							, sat.toString());
	}

	@Test
	public void testCellAtMost() {
		LatinSquaresSat l = new LatinSquaresSat(2);
		SatFormula sat;
		
		sat = new SatFormula();
		sat = l.getCellAtMostFormula(sat);
		Assert.assertEquals("{ZC111ZC110}{C111!ZC111}{!C110!ZC111}{!C111!ZC110}{C110!ZC110}{ZC101ZC100}{C101!ZC101}{!C100!ZC101}{!C101!ZC100}{C100!ZC100}{ZC011ZC010}{C011!ZC011}{!C010!ZC011}{!C011!ZC010}{C010!ZC010}{ZC001ZC000}{C001!ZC001}{!C000!ZC001}{!C001!ZC000}{C000!ZC000}"
							, sat.toString());
	}

	@Test
	public void testEnvFromSolution2x2() {
		LatinSquaresSat l = new LatinSquaresSat(2);
		Env env = l.envFromSolution("2.\n.2");
//		System.out.println(env);
		env = l.solve(env);
//		System.out.println(env);
//		System.out.println(l.interpretResult(env));
		
		// assert that solution is "20\n02"
		Assert.assertNotNull(env);
		Assert.assertEquals(Bool.TRUE, env.get(Var.makeVar("C001")));
		Assert.assertEquals(Bool.TRUE, env.get(Var.makeVar("C100")));
		Assert.assertEquals(Bool.TRUE, env.get(Var.makeVar("C010")));
		Assert.assertEquals(Bool.TRUE, env.get(Var.makeVar("C111")));
	}

//	@Test
	public void testEnvFromSolution6x6() {
		LatinSquaresSat l = new LatinSquaresSat(6);
		Env env = new Env();
		env = l.envFromSolution(
				"..2.1.\n" +
				"...6.2\n" +
				"2...6.\n" +
				".4...3\n" +
				"6.3...\n" +
				".5.3..\n"
				);
//		System.out.println(env);
		long start = System.nanoTime();
		env = l.solve(env);
		System.out.println("ms: " + (System.nanoTime() - start)/1e6);
//		System.out.println(env);
		System.out.println(l.interpretResult(env));
	}		
}
