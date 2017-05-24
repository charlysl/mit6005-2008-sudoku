package sudoku.formula;

import sudoku.set.Set;

public class And extends Formula {

	private Formula first;
	private Formula second;
	
	public And(Formula first, Formula second) {
		this.first = first;
		this.second = second;
	}

	public Formula getFirst() {
		return first;
	}
	
	public Formula getSecond() {
		return second;
	}
	
	@Override
	public Set<Var> vars() {
		return first.vars().addAll(second.vars());
	}

	@Override
	public Bool eval(Env env) {
		
		Bool result2 = second.eval(env);
		if (result2.equals(Bool.FALSE)) {
			return Bool.FALSE;
		} else if (result2.equals(Bool.UNDEFINED)) {
			return Bool.UNDEFINED;
		}
		
		return first.eval(env).and(result2);
	}

	@Override
	String buildString(String str) {
		
		if (!str.isEmpty()) {
			str += "}";
		}
		
		String firstStr = getFirst().buildString(str);
		
		if (firstStr.charAt(0) == '{') {
			str = firstStr;
		} else {
			str += "{" + firstStr + "}";
		}
		
		return getSecond().buildString(str + "{") + "}";
		
	}
	
	@Override
	public String toString() {
//		System.out.println(getFirst());
//		System.out.println(getSecond());
		
		return buildString("");
	}

}
