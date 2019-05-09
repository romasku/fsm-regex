package dfsm

import fsm.FSM

class DFSM(states: List<DFSMState>, startState: DFSMState, finalStates: List<DFSMState>): FSM {
    private val transitions = Array(states.size) { index ->
        states[index].transitions.mapValues { (_, stateForCharacter) -> states.indexOf(stateForCharacter) }
    }
    private val startStateId = states.indexOf(startState)
    private val finalStatesSet = finalStates.map { states.indexOf(it) }.toSet()

    val statesCount get() = transitions.size

    override fun checkString(string: String): Boolean {
        var currentState = startStateId
        for (character in string) {
            currentState = transitions[currentState][character] ?: return false
        }
        return currentState in finalStatesSet
    }

    private fun charactersForStatesGroup(group: Set<Int>): Set<Char> {
        return group.map { transitions[it].keys }.fold(setOf()) { all, part -> all + part }
    }

    private fun buildWithDeadState(): DFSM {
        val allChars = charactersForStatesGroup(transitions.indices.toSet())
        val newStates = transitions.indices.map { DFSMState() }
        val deadState = DFSMState()
        allChars.forEach { deadState.addTransition(it, deadState) }
        for ((oldStateId, newState) in newStates.withIndex()) {
            transitions[oldStateId].forEach { (character, oldStateIdTo) ->
                newState.addTransition(character, newStates[oldStateIdTo])
            }
            (allChars - transitions[oldStateId].keys).forEach { newState.addTransition(it, deadState) }
        }
        val allStates = newStates + listOf(deadState)
        val finalStates = newStates.filterIndexed { index, _ -> index in finalStatesSet }
        return DFSM(allStates, newStates[startStateId], finalStates)
    }

    // Next function should be called only when FSM was build with dead state, or it will fail with exception
    private fun runMinimize(): DFSM {
        val groups = hashSetOf(finalStatesSet, transitions.indices.toSet() - finalStatesSet)
        fun getGroupForRepresentative(representative: Int) = groups.first { set -> representative in set }
        var dividedLastTime = true
        while (dividedLastTime) {
            dividedLastTime = false
            for (group in groups) {
                val possibleCharacters = charactersForStatesGroup(group)
                for (character in possibleCharacters) {
                    val newGroups = group.groupBy { getGroupForRepresentative(transitions[it].getValue(character)) }
                    if (newGroups.size > 1) {
                        dividedLastTime = true
                        groups.remove(group)
                        groups.addAll(newGroups.values.map { it.toSet() })
                        continue
                    }
                }
            }
        }
        val newStatesMap = groups.associate { it to DFSMState() }
        fun getStateForRepresentative(representative: Int): DFSMState {
            return newStatesMap.entries.first { (set, _) -> representative in set }.value
        }

        val startState = getStateForRepresentative(startStateId)
        val finalStates = newStatesMap.filter { (set, _) -> set.intersect(finalStatesSet).isNotEmpty() }.values
        val deadState = newStatesMap.entries.first { (group, state) ->
            state !in finalStates && group.all { group.containsAll(transitions[it].values) }
        }.value
        val allStates = newStatesMap.values.filter { it != deadState }

        for ((key, state) in newStatesMap.entries) {
            val representative = key.first()
            for ((character, toStateId) in transitions[representative].entries) {
                val nextState = getStateForRepresentative(toStateId)
                if (nextState != deadState) {
                    state.addTransition(character, nextState)
                }
            }
        }

        return DFSM(allStates.toList(), startState, finalStates.toList())
    }

    fun minimize(): DFSM {
        return buildWithDeadState().runMinimize()
    }
}