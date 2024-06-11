# CMPE250 - Data Structures and Algorithms

**Instructor:** H. B. Yılmaz  
**Term:** Fall 21

This repository contains my solutions to the projects assigned as part of the CMPE250 - Data Structures and Algorithms course. Each project has been graded 100 out of 100. You will need Java (JDK 16) to compile and run the projects. The run settings for each project are provided as images within their respective directories. Terminal commands are sufficient to execute the programs.

## Projects Overview

### Project 1: Simulation Project

**Description:**  
A starter project to get you acquainted with Java programming and data structures. It involves simple computations.

**Project Outline:**  
- **Entry Class:** `project1main`
- **Compile Command:**
  ```sh
  javac Project1/src/*.java -d Project1/bin -target 17
  ```
- **Run Command:**
  ```sh
  java project1main <inputfile> <outputfile>
  ```

**Project Description:** [description.pdf](Project1/description.pdf)

### Project 2: Discrete Event Simulation

**Description:**  
A detailed discrete event simulation project requiring proper guidelines to complete successfully.

**Project Outline:**  
- **Entry Class:** `project2main`
- **Compile Command:**
  ```sh
  javac Project2/src/*.java -d Project2/bin --release 16
  ```
- **Run Command:**
  ```sh
  java project2main <inputfile> <outputfile>
  ```

**Project Description:** [description.pdf](Project2/description.pdf)

### Project 3: Path-Finding Project

**Description:**  
Involves implementing a weighted pathfinding algorithm like Dijkstra's in the first part and a minimum spanning tree algorithm like Kruskal's or Prim's in the second part.

**Project Outline:**  
- **Entry Class:** `project3main`
- **Compile Command:**
  ```sh
  javac Project3/src/*.java -d Project3/bin --release 16
  ```
- **Run Command:**
  ```sh
  java project3main <inputfile> <outputfile>
  ```

**Project Description:** [description.pdf](Project3/description.pdf)

### Project 4: Network Flow Project

**Description:**  
A project focused on network flow in a bipartite graph, using Tarjan's Bi-Push Relabel algorithm for efficiency.

**Project Outline:**  
- **Entry Class:** `project4main`
- **Compile Command:**
  ```sh
  javac Project4/src/*.java -d Project4/bin --release 16
  ```
- **Run Command:**
  ```sh
  java project4main <inputfile> <outputfile>
  ```

**Project Description:** [description.pdf](Project4/description.pdf)  
**Tarjan's Bi-Push Relabel Paper:** [tarjan-bipushrelabel.pdf](Project4/tarjan-bipushrelabel.pdf)

## Running the Projects

1. **Navigate to the project directory**:
   ```sh
   cd ProjectX
   ```

2. **Compile the project**:
   ```sh
   javac src/*.java -d bin --release 16
   ```

3. **Run the project**:
   ```sh
   java projectXmain <inputfile> <outputfile>
   ```

Replace `ProjectX` and `projectXmain` with the respective project directory and main class name.

## Repository Structure

```
.
|- Project1
|  |- src
|  |- testcases
|  |- description.pdf
|- Project2
|  |- src
|  |- testcases
|  |- description.pdf
|- Project3
|  |- src
|  |- tester
|  |  |- ex_inputs
|  |  |- ex_outputs
|  |  |- myOutput
|  |- description.pdf
|- Project4
|  |- src
|  |- tests
|  |  |- inputs
|  |  |- outputs
|  |- description.pdf
|  |- tarjan-bipushrelabel.pdf
```

