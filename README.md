logicBackwardChaining
=====================

Logic inference using backward chaining

input file pattern including query, number of clauses, and clauses (and facts):
Example of input.txt:

Works(Alice,Aidagency)

6

Works(x,Aidagency)&HasTraveled(x,Timbuktu)=>Diagnosis(x,Infected)

Diagnosis(x,Fever)=>HasSymptom(John,Fever)

HasSymptom(x,Fever)&HasTraveled(x,Timbuktu)=>Works(Alice,Aidagency)

Works(John,Aidagency)

Diagnosis(John,Fever)

HasTraveled(John,Timbuktu)


The agent should write result as TRUE or FALSE in output.txt file
For this example, the program will print TRUE.
