package sudoku.formula;

import sudoku.set.Set;

public abstract class Formula {

	public Env solve() {
		return solve(new Env(), vars());
	}
	
	private Env solve(Env env, Set<Var> variables) {
		
//		System.out.println("env: " + env);
//		System.out.println("vars: " + variables);

		Bool result = eval(env);
//		System.out.println("result: " + result);
		
		if (result.equals(Bool.TRUE)) {
			return env;
		} else if (result.equals(Bool.FALSE)) {
			
//			if (variables.size() > 1) {
//				System.out.println("env: " + env);
//				System.out.println("vars: " + variables);
//				System.out.println("result: " + result);
//			}
			
			return null;
		}
		
		// result is Bool.UNDEFINED, so we need to put
		// at least one more Var
		
		// backtracking:
		// pick one variable and assign it a value;
		// if no solution, then assign it the negated value
		Var v = variables.pick();
		Set<Var> reducedVars = variables.remove(v);
		Env solution = solve(env.put(v,Bool.FALSE), reducedVars);
		if (solution == null) {
			return solve(env.put(v,Bool.TRUE), reducedVars);
		} else {
			return solution;
		}
	}
	
	public abstract Set<Var> vars();
	abstract Bool eval(Env env);
	abstract String buildString(String str);
	
	public static Formula var(String string) {
		return new Var(string);
	}

	public Formula and(Formula f) {
		return new And(this,f);
	}

	public Formula or(Formula f) {
		return new Or(this,f);
	}

	public Formula not() {
		return new Not(this);
	}
}
