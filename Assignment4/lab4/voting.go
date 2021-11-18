package main

import (
	"fmt"
	"math/rand"
	"time"
)

type Votes struct{ a, b int }

func station(out chan<- Votes) {
	for i := 0; i < 10; i++ {
		time.Sleep(time.Duration(rand.Intn(2000)) * time.Millisecond)
		aVotes := rand.Intn(100)
		out <- Votes{aVotes, 100 - aVotes}
	}
	close(out)
}

func collector(in1, in2 <-chan Votes, out chan<- Votes) {
	var tally Votes
	var v Votes
	var ok bool
	for {
		v, ok = <-in1
		if !ok {
			break
		}
		tally.a += v.a
		tally.b += v.b
	}
	for {
		v, ok = <-in2
		if !ok {
			break
		}
		tally.a += v.a
		tally.b += v.b
	}
	out <- tally
	fmt.Println("Current collector tally:", tally)
	close(out)
}

func main() {
	rand.Seed(time.Now().UnixNano())
	var stationChannels [8]chan Votes
	var i int
	for i = range stationChannels {
		stationChannels[i] = make(chan Votes)
	}
	for i = range stationChannels {
		go station(stationChannels[i])
	}

	var collectorFirstLevelChannels [4]chan Votes
	for i = range collectorFirstLevelChannels {
		collectorFirstLevelChannels[i] = make(chan Votes)
	}
	for i = range collectorFirstLevelChannels {
		go collector(stationChannels[2*i], stationChannels[2*i+1], collectorFirstLevelChannels[i])
	}

	var collectorSecondLevelChannels [2]chan Votes
	for i = range collectorSecondLevelChannels {
		collectorSecondLevelChannels[i] = make(chan Votes)
	}
	for i = range collectorSecondLevelChannels {
		go collector(collectorFirstLevelChannels[2*i], collectorFirstLevelChannels[2*i+1], collectorSecondLevelChannels[i])
	}

	var endChannel chan Votes = make(chan Votes)
	go collector(collectorSecondLevelChannels[0], collectorSecondLevelChannels[1], endChannel)

	var tally Votes
	for {
		var v, ok = <-endChannel
		if !ok {
			break
		}
		tally.a += v.a
		tally.b += v.b
		fmt.Println("Current tally:", tally)
	}

	tot := tally.a + tally.b

	if tot != 1000 {
		fmt.Println("Tally issue:", tot)
	}

	var winner string
	switch {
	case tally.a > tally.b:
		winner = "A"
	case tally.a < tally.b:
		winner = "B"
	default:
		winner = "undetermined"
	}
	fmt.Printf("All votes counted. And the winner is: %s\n", winner)
	if winner == "B" {
		fmt.Println("A: This must be FRAUD!!!")
	}
}
