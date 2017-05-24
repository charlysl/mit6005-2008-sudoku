package sudoku.sat;

import sudoku.formula.Bool;
import sudoku.formula.Env;
import sudoku.formula.Var;
import sudoku.list.Cons;
import sudoku.list.EmptyList;
import sudoku.list.List;
import sudoku.set.Set;

public class Clause {

	List<Literal> literals = new EmptyList<>();
	
	public Set<Var> vars() {
		
		Set<Var> vars = new Set<Var>();
		
		for (Literal lit : literals) {
			vars = vars.add(lit.getVar());
		}
		
		return vars;
	}
	
	public Clause reduce(Env env) {
		
		Clause clause = new Clause();
		
		for (Literal lit : literals) {
			
			Bool b = lit.eval(env);
			
			if (b.equals(Bool.UNDEFINED)) {
				
				clause = clause.add(lit);
				
			} else if (b.equals(Bool.TRUE)) {
				
				clause = null;
				
				break;
				
			} else if (b.equals(Bool.FALSE)) {
				
				continue;
				
			}
		}
		
//		System.out.println(clause);
		
		return clause;

	}

	public Clause add(Literal lit) {
		Clause clause = new Clause();
		clause.literals = new Cons<Literal>(lit, literals);
		return clause;
	}

	public boolean isEmpty() {
		return literals.size() == 0;
	}
	
	@Override
	public String toString() {
		
		StringBuilder buf = new StringBuilder();
				
		buf.append('{');
		
		for (Literal lit : literals) {
			buf.append(lit.toString());
		}
		
		buf.append('}');
		
		return buf.toString();
	}

	public int size() {
		return literals.size();
	}

	Literal pickLiteral() {
		return ((Cons<Literal>) literals).getFirst();
	}

}
