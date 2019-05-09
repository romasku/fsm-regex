package dfsm

class DFSM(states: List<DFSMState>, startState: DFSMState, finalStates: List<DFSMState>) {
    private val transitions = Array(states.size) { index ->
        states[index].transitions.mapValues { (_, stateForCharacter) -> states.indexOf(stateForCharacter) }
    }
    private val startStateId = states.indexOf(startState)
    private val finalStatesSet = finalStates.map { states.indexOf(it) }.toSet()

    fun checkString(string: String): Boolean {
        var currentState = startStateId
        for (character in string) {
            currentState = transitions[currentState][character] ?: return false
        }
        return currentState in finalStatesSet
    }
}