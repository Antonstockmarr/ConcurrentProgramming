/*
   DTU 02158 Concurrent Programming
   Mandatory Assignment 4
   Fall 2021
*/

package main

import "fmt"

const N = 500

func odds(out chan<- int, stop <-chan int, terminate chan<- int) {
	var i int = 1
	var done bool = false
	for {
		select {
		case _ = <-stop:
			done = true
		default:
			break
		}
		out <- i*2 + 1
		i = i + 1
		if done {
			break
		}
	}
	fmt.Println("2")
	out <- 0
	close(out)
	for {
		var x int = <-stop
		if x == 0 {
			terminate <- 0
			break
		}
	}
}

func sieve(in <-chan int, out chan<- int) {
	var prime int = <-in
	for {
		var x int = <-in
		if x == 0 {
			break
		}
		if (x % prime) != 0 {
			out <- x
		}
	}
	fmt.Println(prime)
	out <- 0
	close(out)
}

func main() {
	// Declare channels
	var channels [N]chan int
	var terminate chan int = make(chan int)

	// Initialize channels
	for i := 0; i < N; i++ {
		channels[i] = make(chan int)
	}

	fmt.Println("The first", N, "prime numbers are:")

	// Connect/start goroutines
	go odds(channels[0], channels[N-1], terminate)
	for i := 0; i < N-1; i++ {
		go sieve(channels[i], channels[i+1])
	}

	// Await termination
	for {
		var x = <-terminate
		if x == 0 {
			break
		}
	}

	fmt.Println("Done!")
}
