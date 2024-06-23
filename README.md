# Multilevel Queue (MLQ) Scheduling Algorithm
This project is part of the Operating systems course under my bachelor's degree in software engineering

It is an implementation of the Multilevel Queue (MLQ) scheduling algorithm with two queues, Q1 and Q2, where Q1 has a higher priority than Q2. The scheduler follows the Round-Robin (RR) algorithm with a time quantum of 3 ms for processes in Q1 and the non-preemptive Shortest-Job-First (SJF) algorithm for processes in Q2.

## Features

1. Accepts input from the user about the processes in the system, including the total number of processes, their priority, arrival time, and CPU burst time.
2. Implements the Multilevel Queue (MLQ) scheduling algorithm with two queues (Q1 and Q2) and their respective scheduling algorithms (RR for Q1 and SJF for Q2).
3. Displays the scheduling order of the processes, along with detailed information about each process and different scheduling criteria.
4. Writes the output to a file named "Report.txt".

## Usage

1. Clone the repository to your local machine.
2. Compile the source code using a Java compiler.
3. Run the executable.
4. Follow the on-screen instructions to interact with the program.

## Input and Output

### Input
When the user selects option 1, the program will prompt for the following information:
- Total number of processes in the system
- For each process:
  - Process priority (1 or 2)
  - Process arrival time
  - Process CPU burst time


<img width="923" alt="Screenshot 2024-06-23 at 4 14 43 AM" src="https://github.com/NouvBaLQahtani/Operating-Systems-Project-CSC227/assets/106460665/96033501-28ab-47a7-a6f8-07ca9923536b">

### Output
When the user selects option 2, the program will display the following information on the console and in the "Report.txt" file:
- Scheduling order of the processes
- Detailed information about each process (process ID, priority, arrival time, CPU burst time, start and termination time, turnaround time, waiting time, and response time)
- Average turnaround time, average waiting time, and average response time for all processes in the system

<img width="923" alt="Screenshot 2024-06-23 at 4 14 51 AM" src="https://github.com/NouvBaLQahtani/Operating-Systems-Project-CSC227/assets/106460665/a9fd44ff-97f8-484a-b8e0-cdf56c96cad2">

The Report :  

<img width="753" alt="Screenshot 2024-06-23 at 4 16 03 AM" src="https://github.com/NouvBaLQahtani/Operating-Systems-Project-CSC227/assets/106460665/0be0c6a1-fe31-41ea-b4e2-5d71d4c588ae">


## Project Structure

The project consists of two classes:
1. `PCB`: Represents the Process Control Block, which contains information about each process, such as process ID, priority, arrival time, CPU burst time, start and termination time, turnaround time, waiting time, and response time.
2. `Driver`: The main driver class that implements the Multilevel Queue (MLQ) scheduling algorithm. It manages the two queues (Q1 and Q2), schedules the processes, and generates the output.

## Assumptions
- Processes arrive at different times, and the scheduler supports higher-priority processes preempting processes with lower priorities when a new process arrives in the high-priority queue (Q1).
- If a new process arrives at the same time a process releases the CPU, the new process will be added to the ready queue first.

## Future Improvements
- Implement additional scheduling algorithms (e.g., Shortest Remaining Time First, Multilevel Feedback Queue)
- Allow the user to choose the scheduling algorithm
- Provide more detailed visualization of the scheduling process

## Contributing
If you would like to contribute to this project, please follow these steps:
1. Fork the repository
2. Create a new branch for your feature or bug fix
3. Commit your changes
4. Push to the branch
5. Submit a pull request

## Contact

If you have any questions, feedback, or inquiries regarding this project, please feel free to reach out to me through my Email: NouvAlqahtani@gmail.com .
