#define N 2

#define V(S) atomic{S = S + 1}
#define P(S) atomic{S > 0 -> S = S - 1}

int upSem = 0;
int downSem = 0;
int enterSem = 1;

int up = 0;
int down = 0;
int delayedUp = 0;
int delayedDown = 0;

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
		if
			:: down > 0 -> delayedUp++; V(enterSem); P(upSem)
			:: else -> skip
		fi;
		up = up + 1;
		if
			:: delayedUp > 0 -> delayedUp--; V(upSem)
			:: else -> V(enterSem)
		fi;

crit:	/* Critical section */
		upIncrit++;
		assert(downIncrit == 0);
		upIncrit--;

leave:
		P(enterSem);
		up = up - 1;
		if
			:: up == 0  -> if :: delayedDown > 0 -> delayedDown--; V(downSem)
					:: else -> V(enterSem) fi;
			:: else -> V(enterSem)
		fi;

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
		if
			:: up > 0 -> delayedDown++; V(enterSem); P(downSem)
			:: else -> skip
		fi;
		down = down + 1;
		if
			:: delayedDown > 0 -> delayedDown--; V(downSem)
			:: else -> V(enterSem)
		fi;

crit:	/* Critical section */
		downIncrit++;
		assert(upIncrit == 0);
		downIncrit--;

leave:
		P(enterSem);
		down = down - 1;
		if
			:: down == 0  -> if :: delayedUp > 0 -> delayedUp--; V(upSem)
					:: else -> V(enterSem) fi;
			:: else -> V(enterSem)
		fi;

		/* Non-critical setion (may or may not terminate) */
		do :: true -> skip :: break od

	od;
}


ltl binarySplit_1 { [] (0 <= upSem + downSem + enterSem <= 1)}
