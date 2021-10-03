#define N 8

#define P(upSem) atomic{do ::upSem -> ; ::!upSem -> upSem = true; break od} 

int up;  /* Request to enter flags */
int down;     /* Entry granted flags    */

bool upSem;
bool downSem;
 
active [N] proctype P()
{
	do
	::	/* First statement is a dummy to allow a label at start */
		skip; 

entry:	
		
		/*await*/ ok[_pid] ->

crit:	/* Critical section */
		incrit++;
		assert(incrit == 1);
		incrit--;
  	
exit: 
		ok[_pid] = false;

		/* Non-critical setion (may or may not terminate) */
		do :: true -> skip :: break od

	od;
}

active proctype Coordinator()
{
}