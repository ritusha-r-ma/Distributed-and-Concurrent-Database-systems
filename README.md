# Distributed Replicated Concurrency Control and Recovery Database System

## Overview

This project implements a **distributed database system** supporting **Serializable Snapshot Isolation (SSI)** with **data replication**, **site failure handling**, and **failure recovery**. The simulator models a distributed environment consisting of **10 sites** hosting **20 replicated variables**, ensuring transactional consistency and fault tolerance in the presence of concurrent transactions.

The project was developed to demonstrate core concepts in distributed databases, including concurrency control, replication strategies, and recovery mechanisms.

---

## Features

- Serializable Snapshot Isolation (SSI)
- Distributed transaction processing
- Multi-site data replication
- Site failure and recovery simulation
- Available Copies replication protocol
- Dynamic serialization graph for conflict detection
- Snapshot-based reads
- Transaction commit and abort handling
- Fault-tolerant transaction execution

---

## System Architecture

```
                Main (Class)
               /      |      \
              /       |       \
      File Parser  Transaction   Site Manager
            |        Manager         ^
            |           |            |
Input Parsing           |      Site Management
            ^           |
            |           |
     Transaction <------+
          ^
          |
 Transaction Status

 Transaction Manager
          |
          |
 Available Copies / Serialization Graph
          |
      Finalizer

Site Status --> Site --> Site Manager
```

---

## Core Components

### Transaction Manager

Responsible for executing transactions, processing reads and writes, maintaining snapshots, and coordinating transaction commits.

### Site Manager

Maintains the state of all sites, initializes replicated variables, and manages site failures and recoveries.

### Serialization Graph

Builds a dependency graph between transactions to detect cycles and guarantee serializable execution.

### Available Copies Protocol

Ensures transactions commit only if all required replicas remain available after failures.

### Finalizer

Commits successful transactions by propagating writes to all appropriate replicas and generating execution logs.

---

## Algorithms Implemented

- Serializable Snapshot Isolation (SSI)
- Available Copies Replication Protocol
- Dynamic Serialization Graph
- Cycle Detection
- Snapshot Reads
- Multi-site Failure Recovery

---

## Technologies Used

- Java
- Object-Oriented Design
- Distributed Systems
- Transaction Processing
- Concurrency Control
- Replication
- Recovery Algorithms

---

## Project Structure

```
src/
│
├── Main.java
├── FileParser.java
├── TransactionManager.java
├── SiteManager.java
├── Site.java
├── Transaction.java
├── SerializationGraph.java
├── AvailableCopies.java
├── Finalizer.java
└── ...
```

---

## Building the Project

Compile the project:

```bash
javac *.java
```

Run the simulator:

```bash
java Main input.txt
```

---

## Supported Operations

Example input:

```text
begin(T1)
R(T1,x2)
W(T1,x2,50)
fail(3)
recover(3)
end(T1)
dump()
```

---

## Sample Output

```text
T2 writes x4:44 at sites 1,3,4,5,6,7,8,9,10

Transaction T5 aborted due to:
- Available Copies violation
- Serialization Graph conflict
```

---

## Learning Outcomes

This project demonstrates practical implementation of:

- Distributed transaction management
- Serializable Snapshot Isolation
- Replication protocols
- Concurrency control
- Serialization graph construction
- Deadlock and conflict detection
- Distributed failure recovery
- Fault-tolerant database systems

---

## References

1. Bernstein, Hadzilacos, Goodman — *Concurrency Control and Recovery in Database Systems*

2. Philip A. Bernstein — *Concurrency Control and Recovery*

3. Course material on Serializable Snapshot Isolation and Available Copies Protocol

---

## License

This project was developed for academic purposes as part of a Distributed Database Systems course.
