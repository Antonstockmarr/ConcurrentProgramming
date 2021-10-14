#define N 2

#define V(S) atomic{S = S + 1}
#define P(S) atomic{S > 0 -> S = S - 1}


int upSem = 1;
int downSem = 1;

int up = 0;
int down = 0;

int upIncrit = 0;
int downIncrit = 0;
int inEnter = 0;
int inLeave = 0;
int carsInCrit = 0;
int totalCars = 0;

/* Up processes */
active [N] proctype UP()
{
	do
	::	/* First statement is a dummy to allow a label at start */
		skip;

enter:
		atomic{
		P(upSem);
		inEnter++;
		assert(inEnter == 1);
		totalCars++;
		if
			:: up == 0 -> P(downSem)
			:: else -> skip
		fi;
		up = up + 1;
		V(upSem);
		carsInCrit++;
		inEnter--;
		}

crit:	/* Critical section */
		upIncrit++;
		assert(downIncrit == 0);
		upIncrit--;

leave:
		atomic{
		carsInCrit--;
		inLeave++;
		assert(inLeave == 1);
		up = up - 1;
		if
			:: up == 0 -> V(downSem)
			:: else -> skip
		fi;
		inLeave--;
		}

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
		atomic{
		P(downSem);
		inEnter++;
		assert(inEnter == 1);
		totalCars++;
		if
			:: down == 0 -> P(upSem)
			:: else -> skip
		fi;
		down = down + 1;
		V(downSem);
		carsInCrit++;
		inEnter--;
		}

crit:	/* Critical section */
		downIncrit++;
		assert(upIncrit == 0);
		downIncrit--;

leave:
		atomic{
		carsInCrit--;
		inLeave++;
		assert(inLeave == 1);
		down = down - 1;
		if
			:: down == 0 -> V(upSem)
			:: else -> skip
		fi;
		inLeave--;
		}

		/* Non-critical setion (may or may not terminate) */
		do :: true -> skip :: break od

	od;
}
