# NDFSM (non-deterministic finite state machine)
If for each incoming signal FSM has only one state in can go (like it was described in [fsm docs](FSM.md)), such FSM
called deterministic. After each incoming signal, we can always know what state our FSM will go. Such model is easy
for implementation in code, but it is hard to build such FSM from regex expression.

In non-deterministic, after each signal machine can go in multiple state. This means that if you know that machine
starts it state `1`, and on signal `a` it can go to either `2` or `3`, then you can be sure that for string `a` it will
not end it state `1`, but you cannot be sure what exactly state it will be (either `2` or `3`). You can think each time
you send a signal, FSM toss a coin to chose a state it will go. It sounds confusing, I know:).

NDFSM machine accepts some string, it there exists one path (such sequence of choices) that move FSM to accepting state.
It is really helps, as know we can think about "best situation" when we build FSM. For example, lets build NDFSM for 
regex `a*ab`. We will have next states: initial one, awaiting `b` signal, ok state, and failure state. When machine in 
initial state receives `a` signal, it will go to two possible states - it either stays in initial state, or move state
that awaits for `ab`. When in awaiting `ab` state it receives `a`, it moves to `b` awaiting state. In `b` awaiting if
it receives `b`, then goes to ok state. In all other cases it moves to failure state. Here is the table:

| Current state\Signal | a       | b       |
| ---------------------|---------|---------|
| Initial              | {Initial, `ab` awaiting} | Failure |
| `ab` awaiting        | `b` awaiting | Failure |
| `a` awaiting         | Failure      | OK      |
| Ok                   | Failure      | Failure |
| Failure              | Failure      | Failure |

As you can see, if FSM will to `ab` awaiting state just before `ab`, then for strings for regex `a*ab` it will stop in
Ok state. So the best case helps us here.

# Building DFSM from NDFSM

They idea for build DFSM is simple - as set of states is finite, then number of subsets of it is also finite. And NDFSM
can be described by set of states in which it can be possibly be now. And for each subset it is easy to build subset 
NDFSM moves when receives signal - we just need to make union of all rules for table for this signal and for state from
previous subset. Then we can enumerate subsets, and name then states for some DFSM. This algorithm is simple, but has
one flow - it creates gigantic DFSM (as number of subsets 2^n). We can improve it by only generating states initial
NFDSM can possibly go (we start with set of initial set only, and then check all possibly signals, and so on).