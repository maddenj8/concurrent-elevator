# CA4006 Assignment 1

A git repository for CA4006 Concurrent and Distributed Programming assignment 1. The following is the problem specification for the project.

You have been hired by an airport to build an elevator controller in the 10 floor terminal building which is used by people going from floor to floor (some of whom have trolleys with baggage). The elevator(s) can be represented using threads. Each person boarding or leaving the elevator is also represented by a thread. You must implement the methods called by the arriving person (e.g. a method ArrivingGoingFromTo(int atFloor, int toFloor) should wake up an elevator (if necessary) and tell it which floor to go to). The elevator speed is fast but not instantaneous, taking only 100 ticks to go from one floor to the adjacent one. A vanilla option would be to assume the following:

* There is only one elevator and it has a maximum weight capacity; obviously this capacity is affected by the trolleys (as a first approximation you can take these to be of fixed weight, say equal to that of two people).  When the weight of people and trolleys in the lift exceeds the maximum weight, the lift will display an error and remain at the floor until sufficient people (and their trolleys) exit. 
* The elevator can measure the details (weight, ID, arrival floor, destination floor etc) of the people when they enter and does not allow more than a specified weight in the elevator.
* There is only one person entering the elevator at a floor at a time.
* The elevator ascends one floor at a time without skipping floors.
* When not in service, the elevator waits at the last floor it visited and 'sleeps'.

You have to come up with a way of storing the requests to the elevator e.g. if a person is accessing the elevator at the 3rd floor and some body is trying to get access from the 5th floor then latter person has to wait before he could place his request. In other words (s)he has to be put in a queue.  Also if the elevator is going upwards all the requests for the destinations downwards should not be accepted until it starts its downward motion and vice versa.

## Instructions

Assuming the current working directory is the project directory enter the following commands into the command line.

```
javac *.java
java Controller
```