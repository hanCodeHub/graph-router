# Shortest Path On A Graph

### Program entirely written in Java (JDK 11)
_No frameworks or libraries were used_

### Description

**Purpose**

This is a program developed as a term Project for MET CS 526 at Boston University. The goal is to use heuristic algorithms and Java data structures to find the shortest route between nodes from any node to Z.

Provided are input files that contain:
* matrix of each node mapped to adjacent nodes and their distance from each other.
* list of each node's direct distance (dd) to node Z (as the crow flies).

**Algorithms**

Algorithm 1: Among all nodes v that are adjacent to the node n, choose the one with the smallest dd(v). 

Algorithm 2: Among all nodes v that are adjacent to the node n, choose the one for which w(n, v) + dd(v) is the smallest.

**Data Structures**

* HashMap: for mapping the connection between nodes and looking up the adjacent nodes from each node.
* ArrayList: for tracking the full sequence of traversed nodes.
* Stack: for tracking the shortest path of nodes to Z and backtracking when necessary.