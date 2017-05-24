package sudoku.formula;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import sudoku.list.Cons;
import sudoku.list.EmptyList;
import sudoku.list.List;

public class Env implements Iterable<Var> {

	// This rep is much much faster than List<Var>, which
	// was creating a bottleneck because of contains, so much
	// that profiling showed it hogging about 90% of the time.
	private Map<Var,Bool> cache = new HashMap<Var,Bool>();
	
	public Env put(Var var, Bool b) {

		Env env = new Env();
		
		// populate new env cache
		for (Var v : cache.keySet()) {
			env.cache.put(v, cache.get(v));
		}
		
		// put var in the new cache
		env.cache.put(var, b);
		
		return env;
		
	}

//	static int nget;
//	static int hits;
	
	public Bool get(Var var) {
		
		Bool result;
		
		result = cache.get(var);
//		nget++;
		if  (result != null) {
//			hits++;
//			System.out.println("In cache: " + var + "(" + result + ") " + hits + "/" + nget);
			return result;
		} else {
			return Bool.UNDEFINED;
		}
		
	}
	
	@Override
	public String toString() {

		StringBuilder trues = new StringBuilder();
		StringBuilder falses = new StringBuilder();
		
		StringBuilder builder;
		
		for (Var v : cache.keySet()) {
			if (cache.get(v).equals(Bool.TRUE)) {
				builder = trues;
			} else {
				builder = falses;
			}
			
			builder.append(v.toString());
			
		}
		
		trues.insert(0, "TRUE[");
		trues.append("] FALSE[");
		trues.append(falses);
		trues.append("]");
		
		return trues.toString();
		
	}
	
	public int size() {
		return cache.size();
	}

	@Override
	public Iterator<Var> iterator() {
		return cache.keySet().iterator();

	}
}
