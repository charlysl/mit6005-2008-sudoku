package sudoku.formula;

import sudoku.set.Set;

public abstract class Formula {

	public Env solve() {
		return solve(new Env(), vars());
	}
	
	private Env solve(Env env, Set<Var> variables) {
		
		System.out.println("env: " + env);
		System.out.println("vars: " + variables);

		// evaluate the formula when all variables have been assigned
		if (variables.isEmpty()) {
			boolean result = eval(env);
			System.out.println("result: " + result);
			if (result) {
				return env;
			} else {
				return null;
			}
		}
		
		// backtracking:
		// pick one variable and assign it a value;
		// if no solution, then assign it the negated value
		Var v = variables.pick();
		Set<Var> reducedVars = variables.remove(v);
		Env result = solve(env.put(v,false), reducedVars);
		if (result == null) {
			return solve(env.put(v,true), reducedVars);
		} else {
			return result;
		}
	}
	
	abstract Set<Var> vars();
	abstract boolean eval(Env env);
}
