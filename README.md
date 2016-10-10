# ML-MDP (Machine Learning - Markov Decision Process)
Intro to machine learning project: This project implements the value iteration algorithm for finding the optimal policy for each state of an MDP using Bellman’s equation.

Compiling and Running
---------
Insure you have the java development kit (JDK) 8 installed ([link](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)) installed for your operating system. 

### linux / unix

Open a terminal and change directory to the ML-MDP folder. Then do the following to compile and run:

```bash
cd src/
javac Main.java
java Main 4 2 ../resources/DataSet1/test.in 0.9
```
Again, note the arguments:

1. number of states
2. number of possible actions
3. path to the input file.
4. discount factor

### Windows

After installing the JDK 8, locate and get the path for the java compiler `javac.exe`. If you installed `jdk-8u101-windows-x64.exe` from Oracles website, the the path is likely `C:\Program Files\Java\jdk1.8.0_101\bin
`.

Now, navigate to the ML-MDP folder in Windows Explorer. To open a command prompt at this location go to File > open command prompt.

Check the java version, this is what my output looks like:
```shell
C:\Users\Will\Documents\GitHub\ML-MDP>java -version
java version "1.8.0_101"
Java(TM) SE Runtime Environment (build 1.8.0_101-b13)
Java HotSpot(TM) 64-Bit Server VM (build 25.101-b13, mixed mode)
```

Add the path to the JDK bin so we can run the `javac.exe` compiler.
```shell
C:\Users\Will\Documents\GitHub\ML-MDP>set path=%path%;C:\Program Files\Java\jdk1.8.0_101\bin
```

Note that setting the path here does not persist. Once you close the command prompt and open it again, the path variable will need to be set again.

Now compile and run:

```shell
C:\Users\Will\Documents\GitHub\ML-MDP>cd ./src
C:\Users\Will\Documents\GitHub\ML-MDP\src>javac Main.java
C:\Users\Will\Documents\GitHub\ML-MDP\src>java Main ../resources/dataSet1/train2.dat ../resources/dataSet1/test2.dat 0.3 800
```

Again, note the arguments:

1. number of states
2. number of possible actions
3. path to the input file.
4. discount factor

