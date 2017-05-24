package sudoku;
import org.junit.Assert;
import org.junit.Test;

import sudoku.formula.Bool;
import sudoku.formula.Env;
import sudoku.formula.Var;
import sudoku.sat.SatFormula;

public class SudokuTests {

	@Test
	public void testSquareAtMost() {
		Sudoku sudoku = new Sudoku(2);
		SatFormula sat;
		sat = new SatFormula();
		sat = sudoku.getSquaresAtMostFormula(sat,0);
//		System.out.println(sat);
		Assert.assertEquals("{ZS330ZS320ZS230ZS220}{C330!ZS330}{!C320!ZS330}{!C230!ZS330}{!C220!ZS330}{!C330!ZS320}{C320!ZS320}{!C230!ZS320}{!C220!ZS320}{!C330!ZS230}{!C320!ZS230}{C230!ZS230}{!C220!ZS230}{!C330!ZS220}{!C320!ZS220}{!C230!ZS220}{C220!ZS220}{ZS310ZS300ZS210ZS200}{C310!ZS310}{!C300!ZS310}{!C210!ZS310}{!C200!ZS310}{!C310!ZS300}{C300!ZS300}{!C210!ZS300}{!C200!ZS300}{!C310!ZS210}{!C300!ZS210}{C210!ZS210}{!C200!ZS210}{!C310!ZS200}{!C300!ZS200}{!C210!ZS200}{C200!ZS200}{ZS130ZS120ZS030ZS020}{C130!ZS130}{!C120!ZS130}{!C030!ZS130}{!C020!ZS130}{!C130!ZS120}{C120!ZS120}{!C030!ZS120}{!C020!ZS120}{!C130!ZS030}{!C120!ZS030}{C030!ZS030}{!C020!ZS030}{!C130!ZS020}{!C120!ZS020}{!C030!ZS020}{C020!ZS020}{ZS110ZS100ZS010ZS000}{C110!ZS110}{!C100!ZS110}{!C010!ZS110}{!C000!ZS110}{!C110!ZS100}{C100!ZS100}{!C010!ZS100}{!C000!ZS100}{!C110!ZS010}{!C100!ZS010}{C010!ZS010}{!C000!ZS010}{!C110!ZS000}{!C100!ZS000}{!C010!ZS000}{C000!ZS000}"
							, sat.toString());
		
		
	}
	
	@Test
	public void testSudoku4x4() {
		Sudoku sudoku = new Sudoku(2);
		SatFormula sat;
//		Env env;
		sat = new SatFormula();
		sat = sudoku.getFormula();
//		System.out.println(sat);
//		long start = System.nanoTime();
//		env = sat.solve();
		sat.solve();
//		System.out.println("ms: " + (System.nanoTime() - start)/1e6);
//		System.out.println(sudoku.interpretResult(env));
	}

	@Test
	public void testSudoku4x4Partial() {
		Sudoku sudoku = new Sudoku(2);
		Env env = sudoku.envFromSolution(
				".13.\n" + 
				"2...\n" + 
				"...3\n" + 
				".21."
				);
//		System.out.println(env);
//		long start = System.nanoTime();
		env = sudoku.solve(env);
//		System.out.println("ms: " + (System.nanoTime() - start)/1e6);
//		System.out.println(sudoku.interpretResult(env));
		
		Assert.assertNotNull(env);
		Assert.assertEquals(Bool.TRUE, env.get(Var.makeVar("C100")));
		Assert.assertEquals(Bool.TRUE, env.get(Var.makeVar("C202")));
		Assert.assertEquals(Bool.TRUE, env.get(Var.makeVar("C011")));
		Assert.assertEquals(Bool.TRUE, env.get(Var.makeVar("C322")));
		Assert.assertEquals(Bool.TRUE, env.get(Var.makeVar("C131")));
		Assert.assertEquals(Bool.TRUE, env.get(Var.makeVar("C230")));
		
	}

//	@Test
	public void testEasySudoku9x9Partial() {
		// takes ~ 14s
		Sudoku sudoku = new Sudoku(3);
		Env env = sudoku.envFromSolution(
				"79....3..\n" +
				".....69..\n" +
				"8...3..76\n" +
				".....5..2\n" +
				"..54187..\n" +
				"4..7.....\n" +
				"61..9...8\n" +
				"..23.....\n" +
				"..9....54"
		);
		long start = System.nanoTime();
		env = sudoku.solve(env);
		System.out.println("testEasySudoku9x9Partial ms: " 
										+ (System.nanoTime() - start)/1e6);
//		System.out.println(sudoku.interpretResult(env));
		Assert.assertEquals("796854321\n" +
							"243176985\n" +
							"851239476\n" +
							"137965842\n" +
							"925418763\n" +
							"468723519\n" +
							"614597238\n" +
							"582341697\n" +
							"379682154\n", 
							sudoku.interpretResult(env));
	}
	

//	@Test
	public void testHardSudoku9x9Partial() {
		// solved in ~18s
		Sudoku sudoku = new Sudoku(3);
		Env env = sudoku.envFromSolution(
				".........\n" +
				".....3.85\n" +
				"..1.2....\n" +
				"...5.7...\n" +
				"..4...1..\n" +
				".9.......\n" +
				"5......73\n" +
				"..2.1....\n" +
				"....4...9"
		);
		long start = System.nanoTime();
		env = sudoku.solve(env);
		System.out.println("testHardSudoku9x9Partial ms: " 
										+ (System.nanoTime() - start)/1e6);
//		System.out.println(sudoku.interpretResult(env));
		Assert.assertEquals("987654321\n" +
							"246173985\n" +
							"351928746\n" +
							"128537694\n" +
							"634892157\n" +
							"795461832\n" +
							"519286473\n" +
							"472319568\n" +
							"863745219\n", 
							sudoku.interpretResult(env));
	}
	
//	@Test
	public void testMitHardSudoku() {
		// terminates in ~15s
		Sudoku sudoku = new Sudoku(3);
		Env env = sudoku.envFromSolution(
				"9....3..7\n" +
				"8.......1\n" +
				"..32..864\n" +
				".6..27...\n" +
				".81..4...\n" +
				"....3....\n" +
				".....9352\n" +
				".....5...\n" +
				"1.....47."
		);
		long start = System.nanoTime();
		env = sudoku.solve(env);
		System.out.println("testHardSudoku9x9Partial ms: " 
										+ (System.nanoTime() - start)/1e6);
//		System.out.println(sudoku.interpretResult(env));
		Assert.assertEquals("916843527\n" +
							"842756931\n" +
							"753291864\n" +
							"364927185\n" +
							"281564793\n" +
							"597138246\n" +
							"678419352\n" +
							"429375618\n" +
							"135682479\n", 
							sudoku.interpretResult(env));
	}		
}
