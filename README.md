# Regex using FSM

This is simple regex library implemented in Kotlin/Native. It uses FSM (finite sate machine) to check that 
character sequence is an element of language denoted by regex expression. 
Currently, it is on early stage of development and regex expression parsing to build FSM isn't implemented.

# Docs

You can read about algorithms used for this project in markdown files placed in docs directory.
Here is table of contents for it:
- [FSM](docs/FSM.md) (finite state machine) - base explanation of what this term and it's relationship with regex.
- [NDFSM](docs/NDFSM.md) (non-deterministic FSM) - term definition, algorithm to build DFSM from NDFSM.
- [DFSM minimization] - algorithm to minimize number of states in DFSM. (docs not yet written)

# Build
Use the following Gradle commands
* to build: `./gradlew assemble`
* to test: `./gradlew check`
* to run:  `./gradlew runFSMRegexAppReleaseExecutableFSMRegex`
