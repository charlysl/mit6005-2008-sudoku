package sudoku.sat;

import sudoku.formula.Bool;
import sudoku.formula.Env;
import sudoku.formula.Var;
import sudoku.list.Cons;
import sudoku.list.EmptyList;
import sudoku.list.List;
import sudoku.set.Set;

public class SatFormula {

	private List<Clause> clauses = new EmptyList<Clause>();
	
	public Env solve(){
		return solve(new Env(), vars());
	}
	
//	private static long depth;
//	private static long nreduce;
	
	public Env solve(Env env, Set<Var> vars) {
		
//		System.out.println("sat: " + this);
//		System.out.println("env: " + env);
//		System.out.println("var: " + vars);
		
//		long start;
//		start = System.nanoTime();
		SatFormula reduced = reduce(env);
//		System.out.println(depth + " reduced#" + nreduce++ + " vars:" + vars.size() 
//			+ " clauses:" + clauses.size() + " sec:" + (System.nanoTime() - start)/1e9);

//		System.out.println("RED: " + reduced);		
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		if (reduced == null) {
			// env is a solution
			return env;
		}
		
		if (reduced.isEmpty()) {
			// env can't be a solution
			return null;
		}
		
		// Get the smallest clause and its vars.
		// The reason for doing this is that we want to pick
		// one of the vars in the smallest clause, hoping that
		// this will lead to a faster solution. After reduction,
		// none of the vars in this clause will still be in the clause.
		Clause minClause = reduced.min();
		Literal pickedLiteral = minClause.pickLiteral();
		
		Var pickedVar = pickedLiteral.getVar();
//		System.out.println("picked: " + pickedVar);
		
		// split
		
//		depth++;
		
		Env env2 = reduced.solve(env.put(pickedVar, pickedLiteral.neg()), vars.remove(pickedVar));
		if (env2 == null) {
			env2 = reduced.solve(env.put(pickedVar, pickedLiteral.bool()), vars.remove(pickedVar));
		}

//		depth--;
		
		return env2;
	}
	
	boolean isEmpty() {
		return clauses.size() == 0;
	}

	SatFormula reduce(Env env) {
		
		SatFormula sat = null;
		
		for (Clause clause : this.clauses) {
			
			clause = clause.reduce(env);
			
			if (clause == null) {
				
				continue;
				
			} else if (clause.isEmpty()) {
				
				sat = new SatFormula();
				
				break;
				
			} else {
				
				if (sat == null) {
					sat = new SatFormula();
				}
				sat = sat.add(clause);
				
			}
			
		}
		
		return sat;
				
	}


	public SatFormula add(Clause clause) {
		
		SatFormula sat = new SatFormula();
		sat.clauses = new Cons<Clause>(clause,clauses);
		return sat;
		
	}
	
	public Set<Var> vars() {
		
		Set<Var> vars = new Set<Var>();
		
		for (Clause clause : clauses) {
			
			vars = vars.addAll(clause.vars());
			
		}
		
		return vars;
		
	}
	
	@Override
	public String toString() {
		
		StringBuilder buf = new StringBuilder();
		
		for (Clause clause : clauses) {
			
			buf.append(clause.toString());
			
		}
		
		return buf.toString();
	}

	//	
	//	FACADE
	//	
	
	/**
	 * @return New sat with added empty clause
	 */
	public SatFormula clause() {
		SatFormula sat = new SatFormula();
		sat.clauses = new Cons<Clause>(new Clause(), clauses);
		return sat;
	}

	public SatFormula pos(String name) {
		return lit( new PosLiteral(name) );
	}

	/**
	 * Adds literal to the first clause
	 * 
	 * @param literal
	 * @return New sat
	 */
	private SatFormula lit(Literal literal) {
		SatFormula sat = new SatFormula();
		Cons<Clause> cons = (Cons<Clause>) clauses;
		Clause clause = cons.getFirst().add(literal);
		sat.clauses  = new Cons<Clause>(clause, cons.getRest());
		return sat;
	}

	public SatFormula neg(String name) {
		return lit( new NegLiteral(name) );
	}

	Clause min() {

		int minSize = Integer.MAX_VALUE;
		Clause m = null;
		
		for (Clause clause : clauses) {
			if (clause.size() < minSize) {
				m = clause;
				minSize = m.size();
			}
		}
		
		if (minSize > 2) {
			System.out.println("minSize: " + minSize);
		}
		
		return m;
	}

	Bool chooseBool(Var var, Clause clause) {
		return null;
	}
	
}
