package dfsm

import fsm.EMPTY_MOVE_CHARACTER

class DFSMState {
    private val _transitions: MutableMap<Char, DFSMState> = HashMap()
    val transitions: Map<Char, DFSMState> get() = _transitions

    fun addTransition(char: Char, state: DFSMState) {
        if (char == EMPTY_MOVE_CHARACTER) {
            throw Exception("Cannot add e-string (emtpy string) transition to state in determined FSM")
        }
        _transitions[char] = state
    }
}