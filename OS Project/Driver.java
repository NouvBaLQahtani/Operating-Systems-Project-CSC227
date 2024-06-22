import java.io.*;
import java.util.*;
class Driver {
    
    private List<PCB> Q1;
    private List<PCB> Q2;
    private static List<PCB> processes = new ArrayList();
    private static String ordChart = "";

    int clTime = 0; int quantum = 3; 
    int Counter = 0; // used to track the number of time units the current process has been executing.
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
       
        List<PCB> Q1 = new ArrayList<>();
        List<PCB> Q2 = new ArrayList<>();

        Driver scheduler = new Driver(Q1, Q2);

        int choice;
        System.out.println("this is a process scheduling simulation program.") ; 

        do {
        	System.out.println("What do you want to do next: ");
            System.out.println("Menu:");
            System.out.println("1. Enter process details");
            System.out.println("2. Execute scheduling algorithm and Generate a Report ");
            System.out.println("3. Exit the program");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    enterProcessDetails(scanner, Q1, Q2);
                    break;
                case 2:
                	  if (processes.isEmpty()) {
                          System.out.println("you didn't enter any processes Details");
                      } else {
                          scheduler = new Driver(Q1, Q2);
                          scheduler.executeSchedulingAlgorithms();
                          Display();
                          WriteOnFile();
                      }
                  
                    break;
                case 3:
                	  System.out.println("Thank You!");
                    break; 
                default:
                    System.out.println("Invalid choice! Please choose 1, 2, or 3 ");
            }
        } while (choice != 3);
    }
    
    private static void enterProcessDetails(Scanner scanner, List<PCB> Q1, List<PCB> Q2) {
        System.out.print("Enter the number of processes: ");
        int numProcesses = scanner.nextInt();
        scanner.nextLine();

        for (int i = 0; i < numProcesses; i++) {
            System.out.println("Enter details for Process " + (i + 1) + ":");

            System.out.print("Process ID: ");
            String id = scanner.nextLine();

            System.out.print("Arrival Time: ");
            int arrivalTime = scanner.nextInt();
            scanner.nextLine();

            System.out.print("CPU Burst Time: ");
            int cpuBurst = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Priority (1 for Q1, 2 for Q2): ");
            int priority = scanner.nextInt();
            scanner.nextLine();

            PCB process = new PCB(id, arrivalTime, cpuBurst, priority);

            if (priority == 1) {
                Q1.add(process);
            } else if (priority == 2) {
                Q2.add(process);
            } else {
                System.out.println("Invalid priority. Process not added.");
            }
            processes.add(process); 
        }

        System.out.println("Process details entered successfully.");
    }

    public Driver(List<PCB> Q1, List<PCB> Q2) {
        this.Q1 = Q1;
        this.Q2 = Q2;

        sortByArrivalTime(this.Q1);
        sortByArrivalTime(this.Q2);
    }

    public void executeSchedulingAlgorithms() {
        PCB excutingProcess = null;
        List<PCB> rQ1 = new ArrayList<>();
        List<PCB> rQ2 = new ArrayList<>();

        clTime = 0;
        Counter = 0;
        ordChart = "";

        for (PCB process : Q1) {
            process.exceutionTime = 0;
            process.StartTime = 0;
            process.TerminationTime = 0;
        }

        for (PCB process : Q2) {
            process.exceutionTime = 0;
            process.StartTime = 0;
            process.TerminationTime = 0;
        }

        while (!Q1.isEmpty() || !Q2.isEmpty() || !rQ1.isEmpty() || !rQ2.isEmpty() || excutingProcess != null) {
       
// It moves processes from Q1 to rQ1 as long as their arrival time is less than or equal to the value of clTime.
            while (!Q1.isEmpty() && Q1.get(0).ArrivalTime <= clTime) {
                PCB process = Q1.remove(0);
                rQ1.add(process);
            }
//transfers processes from Q2 to rQ2 based on their arrival time. It also ensures that the rQ2 list is sorted based on the burst time of the processes.(SJF).
            while (!Q2.isEmpty() && Q2.get(0).ArrivalTime <= clTime) {
                PCB process = Q2.remove(0);
                rQ2.add(process);
                sortByBurstTime(rQ2);
            }
//it means that there is no currently executing process.
            if (excutingProcess == null) {
                PCB process = null;
                if (!rQ1.isEmpty()) {
                    process = rQ1.remove(0);
                } else if (!rQ2.isEmpty()) {
                    process = rQ2.remove(0);
                }
//If process is not null, it means that a process has been selected to execute.
                if (process != null)
                    excutingProcess = execute(process);

                /*handles the selection and execution of processes based on their priority and time slice expiration. 
                 * It chooses processes from the priority queues and assigns them to the excutingProcess variable for execution.*/
            } else {
                if (excutingProcess.priority == 1 && !rQ1.isEmpty() && Counter == quantum) {
                    rQ1.add(excutingProcess);
                    excutingProcess = execute(rQ1.remove(0));

                } else if (excutingProcess.priority == 2 && !rQ1.isEmpty()) {
                    rQ2.add(excutingProcess);
                    sortByBurstTime(rQ2);
                    excutingProcess = execute(rQ1.remove(0));
                }
            }
//indicate one unit of time 
            clTime++;

            
            /*this code snippet increments the execution time and counter for the executing process. 
             * It checks if the process has completed its CPU burst, 
             * performs termination if necessary,*/
            if (excutingProcess != null) {
                excutingProcess.exceutionTime++;
                Counter++;
                //If the exceutionTime is equal to the CPUBurst, it means that the process has completed its execution.
                if (excutingProcess.exceutionTime == excutingProcess.CPUBurst) {
                    terminate(excutingProcess);
                    excutingProcess = null;
                }
            }
        }
    }
/*updates the counter, adds the process ID to the order chart,
 *  sets the start time of the process if it's the beginning of execution,
 *   and returns the modified process object.*/
    private PCB execute(PCB process) {
        Counter = 0;
        ordChart += process.ProcessID + " | ";

        if (process.exceutionTime == 0)
            process.StartTime = clTime;

        return process;
    }
/*sets the termination time of the process, 
 * calculates the waiting time, 
 * response time, and turnaround time for the process based on the provided formulas. */
    private void terminate(PCB process) {
        process.TerminationTime = clTime;
        process.WaitingTime = process.TerminationTime - process.ArrivalTime - process.CPUBurst;
        process.ResponseTime = process.StartTime - process.ArrivalTime;
        process.TurnAroundTime = process.TerminationTime - process.ArrivalTime;
    }

    public static String getOrdChart() {
        return ordChart;
    }
//comparator = compares the CPUBurst property of the PCB objects
    private void sortByBurstTime(List<PCB> array) {
        Collections.sort(array, Comparator.comparingInt(a -> a.CPUBurst));
    }

    private void sortByArrivalTime(List<PCB> array) {
        Collections.sort(array, Comparator.comparingInt(a -> a.ArrivalTime));
    }

   /*displays the details of each process, the scheduling order chart,
    *  and the scheduling criteria including the average turnaround time, 
    *  average waiting time, and average response time.*/

    public static void Display() {
        System.out.println("Processes Details:");
        System.out.println("*******************");
        for (PCB process : processes) {
            System.out.println(process);
            System.out.println(""); 
            }

        System.out.println();
        System.out.println("Scheduling order chart: | " + Driver.getOrdChart());
        System.out.println();

        int size = processes.size();
        double FinalTurnAround = 0, FinalWait = 0, FinalResponse = 0;

        for (PCB process : processes) {
            FinalWait += process.WaitingTime;
            FinalTurnAround += process.TurnAroundTime;
            FinalResponse += process.ResponseTime;
        }

        System.out.println("Processes Scheduling Criteria:");
        System.out.println("**************************************");
        System.out.printf("Average Turnaround Time : %.3f \n", FinalTurnAround / size);
        System.out.printf("Average Waiting Time    : %.3f \n", FinalWait / size);
        System.out.printf("Average Response Time   : %.3f \n", FinalResponse / size);
        System.out.println();
    }
    
//method writes the process details, scheduling order chart, 
    //and scheduling criteria to a file named "Report.txt" using a PrintWriter object.
    public static void WriteOnFile() {
        try {
            PrintWriter rep = new PrintWriter("Report.txt");

            rep.println("Processes Details:");
            rep.println("*******************");
            for (PCB process : processes){
                rep.println(process);
                System.out.println(""); 
                 }

            rep.println();
            rep.println("Scheduling order chart:| " + Driver.getOrdChart());
            rep.println();

            int size = processes.size();
            double FinalTurnAround = 0, FinalWait = 0, FinalResponse = 0;

            for (PCB process : processes) {
                FinalWait += process.WaitingTime;
                FinalTurnAround += process.TurnAroundTime;
                FinalResponse += process.ResponseTime;
            }

            rep.println("Processes Scheduling Criteria:");
            rep.println("**************************************");
            rep.printf("Average Turnaround Time : %.3f \n", FinalTurnAround / size);
            rep.printf("Average Waiting Time    : %.3f \n", FinalWait / size);
            rep.printf("Average Response Time   : %.3f \n", FinalResponse / size);
            rep.println();

            rep.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}