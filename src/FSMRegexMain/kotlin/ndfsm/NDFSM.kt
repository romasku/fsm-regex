package ndfsm

import dfsm.DFSM
import dfsm.DFSMState
import fsm.EMPTY_MOVE_CHARACTER
import fsm.FSM
import pop
import push


class NDFSM (states: List<NDFSMState>, startState: NDFSMState, finalStates: List<NDFSMState>): FSM {
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
        while (stack.isNotEmpty()) {
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

    override fun checkString(string: String): Boolean {
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

    fun toDFSM(): DFSM {
        val statesMap = HashMap<Set<Int>, DFSMState>()
        val statesStack = ArrayList<Set<Int>>()

        fun getOrInsertStateSet(stateSet: Set<Int>): DFSMState {
            return statesMap[stateSet] ?: run {
                statesStack.push(stateSet)
                val newState = DFSMState()
                statesMap[stateSet] = newState
                newState
            }
        }

        val startState = getOrInsertStateSet(eClosure(setOf(startStateId)))
        while (statesStack.isNotEmpty()) {
            val currentStateSet = statesStack.pop()
            val possibleCharacterMoves = HashSet<Char>()
            for (state in currentStateSet) {
                possibleCharacterMoves += transitions[state].keys
            }
            possibleCharacterMoves -= setOf(EMPTY_MOVE_CHARACTER)
            for (character in possibleCharacterMoves) {
                val newStateSet = HashSet<Int>()
                for (state in currentStateSet) {
                    newStateSet += transitions[state][character] ?: arrayOf()
                }
                val dfsmState = getOrInsertStateSet(eClosure(newStateSet))
                statesMap[currentStateSet]?.addTransition(character, dfsmState)
            }
        }
        val finalStates = statesMap.filter { it.key.intersect(finalStatesSet).isNotEmpty() }.values
        return DFSM(statesMap.values.toList(), startState, finalStates.toList())
    }
}