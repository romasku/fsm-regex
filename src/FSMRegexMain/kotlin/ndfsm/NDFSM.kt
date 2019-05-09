package ndfsm

import fsm.EMPTY_MOVE_CHARACTER
import pop
import push


class NDFSM (states: List<NDFSMState>, startState: NDFSMState, finalStates: List<NDFSMState>) {
    private val transitions = Array(states.size) { index ->
        states[index].transitions.mapValues {(_, statesForCharacter) ->
            statesForCharacter.map { states.indexOf(it) }.toTypedArray()
        }
    }
    private val startStateId = states.indexOf(startState)
    private val finalStatesSet = finalStates.map { states.indexOf(it) }.toSet()

    private fun eClosure(states: Set<Int>): Set<Int> {
        val closure = HashSet(states)
        val stack = ArrayList(states)
        while (stack.size != 0) {
            val stateId = stack.pop()
            for (nextStateId in transitions[stateId][EMPTY_MOVE_CHARACTER] ?: arrayOf()) {
                if (nextStateId !in closure) {
                    stack.push(nextStateId)
                    closure.add(nextStateId)
                }
            }
        }
        return closure
    }

    fun checkString(string: String): Boolean {
        var currentStates = eClosure(setOf(startStateId))
        for (character in string) {
            var nextStates = setOf<Int>()
            for (stateId in currentStates) {
                nextStates += eClosure(transitions[stateId][character]?.toSet() ?: setOf())
            }
            currentStates = nextStates
        }
        return currentStates.intersect(finalStatesSet).isNotEmpty()
    }
}