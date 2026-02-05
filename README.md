---

# JAVA DEVOPS LAB
--- 

## Java Build, Packaging, Classpath, and Ant - A Hands-On Learning Repository

## PURPOSE OF THIS REPOSITORY

This repository is a learning-focused lab designed to understand Java from a DevOps and build-system perspective.

The goal is NOT to learn Java programming in depth.

Instead, this repository focuses on:

* How Java source code turns into runnable artifacts
* How Java applications are built, packaged, and executed
* How dependencies are resolved at compile-time and run-time
* How build tools (like Apache Ant) automate repeatable builds
* What files and folders matter in a Java project, and why

This is the kind of practical, systems-level understanding expected from a DevOps engineer working with Java-based systems.

---

## BIG PICTURE: HOW JAVA APPLICATIONS ARE BUILT AND RUN

Before touching tools or commands, it is important to understand the end-to-end lifecycle of a Java application.

#### Java follows this lifecycle:

1. A developer writes `.java` source code
2. The **JDK** compiler (`javac`) converts source code into `.class` bytecode
3. Compiled `.class` files are packaged into `JAR` files
4. JAR files are shipped to environments (`dev / test / prod`)
5. The **JRE** and **JVM** execute the **JAR** on a machine

This repository intentionally walks through these steps manually first, and then shows how tools like Ant automate them.

This mirrors real-world DevOps work: you must understand the manual steps before trusting automation.

---

## CORE JAVA CONCEPTS

## JVM (Java Virtual Machine)

#### What it is:
The JVM is the runtime engine that executes Java bytecode (.class files).

#### Why it exists:
Different operating systems use different machine instructions. The JVM abstracts this away so Java applications do not need to be rewritten per OS.

#### How it works (high level):

* Java source is compiled into bytecode
* The JVM interprets or JIT-compiles bytecode into native instructions
* The JVM manages memory, garbage collection, and threading

#### Important properties:

* JVM is platform-specific
* Bytecode is platform-independent

#### Key idea:
Java is “write once, run anywhere” because bytecode runs on the JVM, not directly on hardware.

---

## JRE (Java Runtime Environment)

#### What it is:
JRE = JVM + standard Java runtime libraries.

#### Why it exists:
Most machines only need to run Java applications, not build them.

#### What it includes:

* JVM
* Core Java libraries

#### What it does NOT include:

* javac
* jar
* build tools

#### DevOps relevance:
Production servers usually need only a JRE. Build and CI machines do not.

---

## JDK (Java Development Kit)

#### What it is:
JDK = JRE + development and build tools.

#### Why it exists:
Java applications cannot be compiled or packaged without a JDK.

#### What it includes:

* javac (compiler)
* jar (packaging tool)
* java (runtime launcher)
* debugging and tooling

#### DevOps relevance:

* CI/CD pipelines require JDK
* Build containers require JDK
* Developer machines require JDK

#### Rule of thumb:
If a machine builds Java → JDK
If a machine only runs Java → JRE

---

## javac (Java Compiler)

#### What it does:
Converts `.java` source files into .class bytecode.

#### Why compilation is separate:
Java is not interpreted directly from source. Bytecode enables portability and optimization.

#### Example command:
```
javac -d build/classes src/com/acme/hello/Main.java
```
#### Command explanation:

* javac → Java compiler
* -d build/classes → destination directory for compiled classes
* Source files remain unchanged

#### Why the `-d` option matters:

* Keeps build output separate from source code
* Enables clean builds
* Prevents committing generated files

#### DevOps takeaway:
Build output must be reproducible and disposable.

---

## .class files

#### What they are:
Compiled Java bytecode files.

#### Why they matter:
They are the real executable input for the JVM. Everything else exists to manage them.

#### How they are stored:
Directory structure mirrors the package name.

#### Example:
com/acme/hello/Main.class

---

## JAR (Java ARchive)

#### What it is:
A ZIP-based archive format used to bundle Java applications.

#### Why JARs exist:

* Easier distribution than loose class files
* Single deployable artifact
* Standard format for CI/CD pipelines

#### What a JAR contains:

* .class files
* resources (configs, text files, etc.)
* metadata (MANIFEST.MF)

#### Inspecting a JAR:
```
jar tf app.jar
```
#### Command explanation:

* jar → packaging tool
* t → table of contents
* f → file name follows

#### DevOps relevance:
JARs are the artifacts that pipelines build, version, store, and deploy.

---

## MANIFEST.MF

#### What it is:
A metadata file inside every JAR, located at META-INF/MANIFEST.MF.

#### Why it matters:
It controls how the JAR behaves at runtime.

#### Most important attribute:
```
Main-Class: com.acme.app.Main
```
#### What this means:
It tells Java which class contains the public static void main() entry point.

#### Critical rule:
The manifest file must end with a newline, or the JVM may ignore it.

#### DevOps implication:
Incorrect manifests cause runtime failures even when builds succeed.

---

## java command (runtime launcher)

#### What it does:
Starts a JVM and executes Java code.

#### Two common execution modes:

1. Run a class directly:
```
java -cp <classpath> com.acme.app.Main
```
2. Run a runnable JAR:
```
java -jar app.jar
```
#### Important difference:

* java -jar ignores external classpath
* java -cp allows multiple JARs

This distinction explains many production issues.

---

## CLASSPATH (CRITICAL CONCEPT – DEEP DIVE)

#### What classpath is:
Classpath is an explicit list of locations where Java looks for classes and libraries.

#### Why classpath exists:
Java does not scan the entire filesystem. Explicit paths ensure predictable behavior.

#### Where classpath is used:

1. Compile time (javac)
2. Runtime (java)

#### Compile-time classpath example:
```
javac -classpath lib/util.jar Main.java
```
#### Meaning:
The compiler must find all referenced classes or compilation fails.

#### Runtime classpath example:

```
java -cp app.jar:lib/util.jar com.acme.Main
```
#### Meaning:
The JVM must find all required classes or runtime fails.

#### OS separator difference:
```
Linux/macOS → :
Windows → ;
```
#### Why this causes issues:
Compile-time success does NOT guarantee runtime success.

#### DevOps takeaway:
Most Java production issues are classpath issues, not code issues.

---

## PROJECT STRUCTURE (WHAT, WHY, HOW)
```
java-devops-lab/
├── hello-cli/
├── multi-jar/
└── ant-demo/
```

#### Why this structure exists:

* Each project isolates a learning goal
* Prevents concept mixing
* Mirrors real multi-module repositories

#### Common folder conventions:

* src/ → human-written source code
* build/ → compiled output (generated)
* dist/ → deployable artifacts (generated)

Generated folders should not be committed to version control.

---

## PROJECT 1: hello-cli (Single Runnable JAR)

#### What this project is:
A minimal Java application packaged into a single runnable JAR.

#### Why it exists:
This is the simplest Java deployment model and very common for batch jobs and CLI tools.

#### How it works:

* javac compiles source code
* jar packages compiled classes
* manifest defines the entry point

#### DevOps takeaway:
Simple artifacts are easy to deploy, debug, and automate.

---

## PROJECT 2: multi-jar (Library + Application)

#### What this project is:
An application JAR that depends on a separate library JAR.

#### Why this matters:
This is the default real-world Java scenario.

#### How it works:

* Library is compiled first
* Application is compiled against the library
* Runtime requires explicit classpath configuration

#### DevOps takeaway:
Explains dependency wiring, runtime failures, and why fat JARs and build tools exist.

---

## PROJECT 3: ant-demo (Build Automation)

#### What this project is:
A Java project built using Apache Ant.

#### Why Ant exists:
Ant was one of the earliest Java build automation tools and is still used in legacy systems.

#### How Ant works:

* Declarative XML build file
* Targets represent build steps
* Dependencies enforce execution order

#### DevOps takeaway:
Understanding Ant is essential for maintaining and debugging older CI pipelines.

---

## WHY THIS MATTERS FOR DEVOPS

As a DevOps engineer, you are expected to:

* Understand build vs runtime environments
* Know which tools are required where
* Debug build and runtime failures
* Fix classpath and packaging issues
* Configure CI/CD pipelines correctly

You are NOT expected to be a Java language expert.

This repository teaches exactly what DevOps engineers need — no more, no less.

---

