package regex_lexer

import ndfsm.NDFSMState


class PartialNDFSM(states: List<NDFSMState>, startState: NDFSMState, finalStates: List<NDFSMState>) {
    companion object {
        val digits: PartialNDFSM
        val any: PartialNDFSM
        init {
            // For Digits
            val stateDigitStart = NDFSMState()
            val stateDigitFinal = NDFSMState()
            for (char in listOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')) {
                stateDigitStart.addTransition(char, stateDigitFinal)
            }
            digits = PartialNDFSM(listOf(stateDigitStart, stateDigitFinal), stateDigitStart, listOf(stateDigitFinal))
            // Any char (. symbol)
            val stateAnyStart = NDFSMState()
            val stateAnyFinal = NDFSMState()
            for (char in (1..255).map { it.toChar() }) {
                if (char != '\n') {
                    stateAnyStart.addTransition(char, stateAnyFinal)
                }
            }
            any = PartialNDFSM(listOf(stateAnyStart, stateAnyFinal), stateAnyStart, listOf(stateAnyFinal))

        }

        fun forCharacter(char: Char) : PartialNDFSM {
            val startState = NDFSMState()
            val finalState = NDFSMState()
            startState.addTransition(char, finalState)
            return PartialNDFSM(listOf(startState, finalState), startState, listOf(finalState))
        }
    }
}
