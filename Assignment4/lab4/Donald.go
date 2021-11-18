package main

import "fmt"

func p(name string, ch chan<- int) {

	for i := 0; i < 10; i++ {
		fmt.Printf("I am %s\n", name)
	}
	ch <- 1
}

func main() {
	var finished = make(chan int)

	go p("Huey", finished)
	go p("Dewey", finished)
	go p("Louie", finished)
	go p("Anton", finished)

	for i := 0; i < 4; i++ {
		var x = <-finished
		fmt.Printf("%d\n", x)
	}

}
