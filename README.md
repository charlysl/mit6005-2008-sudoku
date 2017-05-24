# mit6005-2008-sudoku
MIT Course 6.005 Fall 2008 - Sudoku problem 

This is my solution to: https://ocw.mit.edu/courses/electrical-engineering-and-computer-science/6-005-elements-of-software-construction-fall-2008/assignments/MIT6_005f08_explore02.pdf

This is a non-trivial exercise to practice functional design and programming, in this case in a non functional
language like Java, and it turns out it is perfectly feasible if you are careful. As a result fo this project, 
the Interpreter design pattern became burned in my mind (and derivative patterns, like Visitor, etc ...).

Also, the very principled approach of beginning the design of a new program in the functional paradigm 
by first describing data types based on grammars, and then directly applying design patterns to seamlessly
synthesize the code has been enlightning.

In this project, first, as an experiment, I tried to express the Sudoku (actually, the simpler Latin Square)
as an And|Or|Not|Var formula, which wouldn't even terminate for a 2x2 Latin Square, but this futile attempt
had the benefit of me getting a firm handle on the Interpreter pattern.

Then I expressed the Latin Square as a SAT formula with unit propagation, and the performance
improvement was dramatic: even an 8x8 Latin Square terminated (in a few minutes though).

Although I performed multiple optimizations based on design and code analysis, and on profiling
using VisualVM, these were the most instructive:

1) Changed the rep of the Env class. It was based on an immutable Set (that maintained the 
  tipical invariants at great performance cost), which in turn was based on an immutable List.
  Instead, I decided to just use a simple cache based on java.util.Map, while at the same time
  still respecting Env object immutability. The performance improvemnt was enourmous, with
  4x4 Sudokus now terminating, correctly, but a new bottleneck emerged:
  
2) Profiling showed that reformulating clauses (reducing) in a backtracking step was often very slow,
  taking a few seconds.
  Also, such huge ammounts of Clause instances were being created that even with ever increasing
  -Xm changes, the VM would eventually run out of memory. I decided that the reason was that I was
  needlesly creating a new Clause for every Clause reduction, which I realized wasn't at all necessary.
  Carefully modifying the reduce method in Clause caused even hard 9x9 Sudokus to correctly terminate
  in a few seconds, out of the box, and finally solving the exercise.
  
