# FSM (finite state machine)
In general, finite state machine can be imagined as black box has one finite number associated to it (it's) state. One
state should be marked as special - start state. You can also send signals to FSM (set of all possible signals is also 
finite set). On each signal, machine will change it'sstate.

For example, imagine next FSM: it has can be it three states - `{1, 2, 3}`, and possible signals is `{a, b}`. Then we can
describe how our machine works using next table (we assume that start state is `1`): 

| Current state\Signal | a | b |
| ---------------------|---|---|
| 1                    | 1 | 2 |
| 2                    | 3 | 2 |
| 3                    | 3 | 3 |

We this FSM *starts*, it is in `1` state. Then, if it receives signal `b`, it moves to state `2`. If it then receives 
signal `b`, it moves to sate `2` - it is ok that it is current state, machine can stay in same state for some signals.
Now, to make this example more interesting, we can imagine that we send strings of `a` and `b` to machine on observe, 
what will be last state. For example, for string `aabbb` it will be `2`: 
```
   a      a      b      b      b
1 ---> 1 ---> 1 ---> 2 ---> 2 ---> 2
```
As you can see, for any string that starts with `aaaa..aaa` (finite, maybe zero number of `a`), followed by 
`bb..bb`, fsm will stop in state `2`. What is more interesting, it is the only type of strings that will lead to `2`.
If strings contains `a` after `b`, then FSM will move to state 3, and, as we can see, it will never leave it. So, this
FSM is equivalent in some sense to regex `a*b+` (zero or more `a`, then one or more `b`). To make it more formal, we
can call state `2` as accepting state. Then a string is allowed by FSM, if it stops in accepting state. This library is
all about build FSM for giving regex expression.
