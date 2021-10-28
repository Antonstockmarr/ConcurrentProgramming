#define N 2

#define V(S) atomic{S = S + 1}
#define P(S) atomic{S > 0 -> S = S - 1}


int upSem = 1;
int downSem = 1;
int enterSem = 1;

int up = 0;
int down = 0;

int upIncrit = 0;
int downIncrit = 0;


/* Up processes */
active [N] proctype UP()
{
	do
	::	/* First statement is a dummy to allow a label at start */
		skip;

enter:
		P(enterSem);
		P(upSem);
		if
			:: up == 0 -> P(downSem)
			:: else -> skip
		fi;
		up = up + 1;
		V(upSem);
		V(enterSem)

crit:	/* Critical section */
		upIncrit++;
		assert(downIncrit == 0);
		upIncrit--;

leave:
		P(upSem);
		up = up - 1;
		if
			:: up == 0 -> V(downSem)
			:: else -> skip
		fi;
		V(upSem);

		/* Non-critical setion (may or may not terminate) */
		do :: true -> skip :: break od

	od;
}


/* Down processes */
active [N] proctype DOWN()
{
	do
	::	/* First statement is a dummy to allow a label at start */
		skip;

enter:
		P(enterSem);
		P(downSem);
		if
			:: down == 0 -> P(upSem)
			:: else -> skip
		fi;
		down = down + 1;
		V(downSem);
		V(enterSem);

crit:	/* Critical section */
		downIncrit++;
		assert(upIncrit == 0);
		downIncrit--;

leave:
		P(downSem);
		down = down - 1;
		if
			:: down == 0 -> V(upSem)
			:: else -> skip
		fi;
		V(downSem);

		/* Non-critical setion (may or may not terminate) */
		do :: true -> skip :: break od

	od;
}
