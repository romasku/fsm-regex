package regexfsm

class NDFSM (states: List<NDFSMState>, startState: NDFSMState, finalStates: List<NDFSMState>) {
    private val transitions: Array<Map<Char, Array<Int>>>
    private val startStateId: Int
    private val finalStatesBitSet: Set<Int>

    init {
        fun getStateIds(statesToMap: List<NDFSMState>): Array<Int> {
            return states.mapIndexed { index, state -> index to state }
                .filter { (_, state) -> state in statesToMap }
                .map { (index, _) -> index }.toTypedArray()
        }
        transitions  = Array(states.size) { index ->
            val map: HashMap<Char, Array<Int>> = HashMap()
            for (character in states[index].transitions.keys) {
                map[character] = getStateIds(states[index].transitions[character]!!)
            }
            map
        }
        startStateId = getStateIds(listOf(startState))[0]
        finalStatesBitSet = getStateIds(finalStates).toSet()
    }

    private fun eClosure(states: Set<Int>): Set<Int> {
        val closure = HashSet(states)
        val stack = ArrayList(states)
        while (stack.size != 0) {
            val stateId = stack[stack.size - 1]
            stack.removeAt(stack.size - 1)
            for (nextStateId in transitions[stateId][emptyMoveCharacter]?: arrayOf()) {
                if (nextStateId !in closure) {
                    stack.add(nextStateId)
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