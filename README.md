============================================================
JAVA DEVOPS LAB
Java Build, Packaging, and Ant — Hands-On Learning Repository
=============================================================

## PURPOSE OF THIS REPOSITORY

This repository is a hands-on learning lab created to understand **Java from a DevOps perspective**.

Instead of focusing on Java as a programming language, this repo focuses on:

* How Java applications are **built**
* How they are **packaged**
* How they are **executed**
* How build tools like **Ant** automate these steps
* What artifacts (JARs, class files) are produced and why

This is the kind of **high-level + practical knowledge** expected from a DevOps engineer working with Java projects.

---

## BIG PICTURE: HOW JAVA APPLICATIONS ARE BUILT & RUN

Java follows this lifecycle:

1. Developer writes `.java` source code
2. JDK compiles `.java` → `.class` (bytecode)
3. `.class` files are packaged into JAR files
4. JAR files are shipped to environments (dev/test/prod)
5. JRE runs the JAR on a JVM

This repo walks through **each step manually**, then automates it using Ant.

---

## CORE JAVA CONCEPTS (VERY IMPORTANT)

### JVM (Java Virtual Machine)

* The JVM is the **engine that runs Java bytecode**
* It is platform-specific (Linux JVM, Windows JVM, etc.)
* JVM executes `.class` files, not `.java` files
* JVM provides:

  * Memory management
  * Garbage collection
  * Platform independence

Key idea:

> Java is “write once, run anywhere” because bytecode runs on the JVM.

---

### JRE (Java Runtime Environment)

* JRE = JVM + standard Java libraries
* Used ONLY to **run** Java applications
* Does NOT include:

  * `javac`
  * `jar`
  * build tools

If a machine only needs to run a Java app (like a production server), **JRE is enough**.

---

### JDK (Java Development Kit)

* JDK = JRE + development tools
* Includes:

  * `javac` → Java compiler
  * `jar` → packaging tool
  * `java` → runtime launcher
  * debugging & tooling

If a machine needs to **build or compile Java**, it MUST have a JDK.

DevOps relevance:

* CI servers (Jenkins, GitHub Actions) need **JDK**
* Runtime servers may need only **JRE**

---

### javac (Java Compiler)

* Converts `.java` → `.class`
* Output is **bytecode**, not machine code
* Bytecode is platform-independent

Example:

```
javac -d build/classes src/com/acme/hello/Main.java
```

Meaning:

* `-d build/classes` → where compiled `.class` files go
* Source files remain unchanged
* Output directory is generated

---

### .class files

* Compiled bytecode
* Cannot be meaningfully edited by humans
* Executed by JVM
* Stored inside directories matching the package structure

Example:

```
com/acme/hello/Main.class
```

---

### JAR (Java ARchive)

* A JAR is basically a **ZIP file**
* Contains:

  * `.class` files
  * resources (configs, text files, etc.)
  * metadata (MANIFEST)

JARs are:

* Build artifacts
* What gets deployed to servers
* What CI/CD pipelines publish

Inspect a JAR:

```
jar tf app.jar
```

---

### MANIFEST.MF

* A metadata file inside the JAR
* Lives at: `META-INF/MANIFEST.MF`
* Controls how the JAR behaves

Most important attribute:

```
Main-Class: com.acme.app.Main
```

This tells Java:

> “This is the class that contains `public static void main()`”

Important rule:

* Manifest file **must end with a newline**
* Otherwise, Java may ignore it

---

### java command

Used to run Java applications.

Two common modes:

1. Run a class directly

```
java -cp <classpath> com.acme.app.Main
```

2. Run a runnable JAR

```
java -jar app.jar
```

Important difference:

* `java -jar` ignores external classpath
* `java -cp` allows multiple JARs

---

### CLASSPATH (CRITICAL CONCEPT)

Classpath tells Java:

> “Where should I look for classes and libraries?”

Used in TWO places:

1. Compile time (`javac`)
2. Run time (`java`)

Example:

```
javac -classpath lib/util.jar Main.java
java -cp app.jar:lib/util.jar com.acme.Main
```

OS difference:

* Linux/macOS → `:`
* Windows → `;`

Classpath mistakes are the #1 cause of Java runtime errors.

---

---

## PROJECT STRUCTURE IN THIS REPO

```
java-devops-lab/
├── hello-cli/
├── multi-jar/
└── ant-demo/
```

Each project focuses on a **specific DevOps learning goal**.

---

## PROJECT 1: hello-cli (Single Runnable JAR)

GOAL:

* Understand javac
* Understand manifest
* Build a runnable JAR manually

STRUCTURE:

```
hello-cli/
├── src/
│   └── com/acme/hello/Main.java
├── build/        (generated)
├── dist/         (generated)
└── manifest.mf
```

BUILD FLOW:

1. javac compiles source into build/classes
2. jar packages classes into dist/hello-cli.jar
3. manifest defines entry point
4. java -jar runs the application

DEVOPS TAKEAWAY:

* This is the simplest deployable Java artifact
* Many internal tools and batch jobs look like this

---

## PROJECT 2: multi-jar (Library + Application)

GOAL:

* Understand dependencies
* Understand classpath
* Understand why builds fail without correct wiring

STRUCTURE:

```
multi-jar/
├── lib/
│   └── dist/acme-util.jar
└── app/
    └── dist/app.jar
```

IMPORTANT REALITY:

* Java does NOT bundle dependencies automatically
* DevOps engineers must understand how apps locate libraries

RUN MODE:

```
java -cp app.jar:lib.jar com.acme.app.Main
```

DEVOPS TAKEAWAY:

* Explains “ClassNotFoundException”
* Explains why fat JARs exist
* Explains runtime dependency issues

---

## PROJECT 3: ant-demo (Build Automation)

GOAL:

* Replace manual commands with repeatable builds
* Learn legacy but still common Java tooling

KEY FILE:

```
build.xml
```

ANT CONCEPTS:

* Targets (clean, compile, jar, run)
* Dependencies between targets
* Declarative build steps

WHY ANT MATTERS FOR DEVOPS:

* Many legacy systems still use Ant
* CI/CD pipelines often invoke Ant
* Understanding Ant helps debug old build systems

---

## WHY THIS MATTERS FOR DEVOPS

As a DevOps engineer, you are expected to:

* Know what tools are needed to build vs run
* Understand what artifacts are produced
* Debug build failures
* Fix classpath and runtime issues
* Configure CI pipelines correctly

You are NOT expected to:

* Write complex Java business logic
* Optimize Java algorithms

This repo focuses on **what DevOps engineers actually need**.

---

## WHAT TO PRACTICE NEXT

Recommended next steps:

1. Add versioning to JAR names
2. Embed build version into MANIFEST
3. Write a shell script to build all projects
4. Learn how Maven/Gradle automate these same steps
5. Run these builds inside Docker

