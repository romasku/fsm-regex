package fsmregex


class NDFSM (states: List<NDFSMState>, startState: NDFSMState, finalStates: List<NDFSMState>) {
    private val transitions: Array<Map<Char, Array<Int>>>
    private val startStateId: Int
    private val finalStatesBitSet: Set<Int>

    init {
        transitions  = Array(states.size) { index ->
            states[index].transitions.mapValues {(_, statesForCharacter) ->
                statesForCharacter.map { states.indexOf(it) }.toTypedArray()
            }
        }
        startStateId = states.indexOf(startState)
        finalStatesBitSet = finalStates.map { states.indexOf(it) }.toSet()
    }

    private fun eClosure(states: Set<Int>): Set<Int> {
        val closure = HashSet(states)
        val stack = ArrayList(states)
        while (stack.size != 0) {
            val stateId = stack.pop()
            for (nextStateId in transitions[stateId][emptyMoveCharacter] ?: arrayOf()) {
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
        return currentStates.intersect(finalStatesBitSet).isNotEmpty()
    }
}