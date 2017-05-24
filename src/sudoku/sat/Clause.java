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
		// It is better not to create a new clause at the beginning.
		// The reason is that:
		// a) profiling has shown that creating new clauses has 
		//		a big cpu and memory impact, so I expect this will improve performance
		//		and, eventually, allow the 9x9 sudoku to terminate. 
		// b) it is never necessary to create a new clause if any of the literals
		//		evaluates to true, because in this case null is returned.
		// c) it is only necessary to create a new clause if
		// 		at least one literal evaluated to false.
		// d) if all literals evaluate to undefined then this clause can be returned,
		//		no need to create a new clause.	
		Clause clause = this;	// the new clause (if any)
		
		// An array to keep track of undefined literals.
		//
		// It allows me to defer creating a new clause, if at all necessary, 
		// until after iterating over all literals.
		//
		// Set element to 1 if literal at that index is undefined, otherwise set to 0.
		//
		// Of course, introducing this additional resource has a performance impact,
		// but the expectation is that it will compensate by reducing the suspected
		// bigger, and, as profiling has shown, significant impact of creating a new 
		// clause before iterating over the literals.
		//
		// To somehow mitigate this impact, the array is not created until non true 
		// literal is found. The reason is that it is possible that a true literal was
		// found before the first non true literal.
		//
		// One way or another, there is a need to keep track of the undefined literals. 
		// There might be more efficient ways, like using bit granularity instead of bytes,
		// but implementing these more complex means would be early optimization at
		// this stage.
		//
		// The array must be local to the method, as making it a field would break
		// inmutability.
		//
		// IT WORKED!!! 9x9 hard sudokus terminate with correct solution in ~15s!!!
		byte[] undefs = null;
		
		// index to undefs
		int undefsIdx = 0;
		// undefined literals counter
		int nundefs = 0;
		
		for (Literal lit : literals) {
			
			Bool b = lit.eval(env);
			
			
			if (b.equals(Bool.FALSE) || b.equals(Bool.UNDEFINED)) {

				if (undefs == null) {
					// create a byte array the same size as this clause
					undefs = new byte[size()];
				}
				
			}
			
			
			if (b.equals(Bool.UNDEFINED)) {
				
				undefs[undefsIdx] = (byte) 1;
				nundefs++;
				
			} else if (b.equals(Bool.FALSE)) {

				undefs[undefsIdx] = (byte) 0;
				
			} else {

				// the clause is true, so it can be removed
				clause = null;				
				break;
				
			}
			
			if (++undefsIdx == Integer.MAX_VALUE) {
				// to prevent undetected integer overflow
				throw new IllegalStateException("The clause has more than " + Integer.MAX_VALUE + " literals.");
			}
			
		}
				
		if (clause != null && undefs != null && nundefs != size()) {
			
			// Copy undefined literals to a new clause.
			// The net effect is to remove false literals
			// from this clause.
			clause = new Clause();
			undefsIdx = 0;
			for (Literal l : literals) {
				if (undefs[undefsIdx] == (byte) 1) {
					clause = clause.add(l);
				}
				undefsIdx++;
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
