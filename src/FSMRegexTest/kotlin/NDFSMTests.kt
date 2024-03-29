import fsm.EMPTY_MOVE_CHARACTER
import fsm.FSM
import ndfsm.NDFSM
import ndfsm.NDFSMState
import kotlin.test.Test
import kotlin.test.assertEquals

class NDFSMTests {
    @Test
    fun statesWorkProperly() {
        val state1 = NDFSMState()
        val state2 = NDFSMState()
        val state3 = NDFSMState()
        state1.addTransition('a', state2)
        state1.addTransition('a', state3)
        state1.addTransition('b', state2)
        assertEquals(listOf(state2, state3), state1.transitions['a'])
        assertEquals(listOf(state2), state1.transitions['b'])
    }

    private fun runTestsForFSM(fsm: FSM, checks: List<Pair<String, Boolean>>) {
        for ((string, result) in checks) {
            assertEquals(result, fsm.checkString(string), "Checking for \"$string\"")
        }
    }

    @Test
    fun `NDFSM works properly for ab*c`() {
        val state1 = NDFSMState()
        val state2 = NDFSMState()
        val state3 = NDFSMState()
        state1.addTransition('a', state2)
        state2.addTransition('b', state2)
        state2.addTransition('c', state3)
        val fsm = NDFSM(listOf(state1, state2, state3), state1, listOf(state3))
        val checks = listOf(
                "abbbc" to true,
                "ac" to true,
                "abc" to true,
                "aa" to false,
                "abbcf" to false,
                "fabbc" to false,
                "sdjfksdjkj fksjdfkjds sjdfkj" to false)
        this.runTestsForFSM(fsm, checks)
    }

    @Test
    fun `NDFSM works properly for a(b|c)d`() {
        val state1 = NDFSMState()
        val state2 = NDFSMState()
        val state3 = NDFSMState()
        val state4 = NDFSMState()
        state1.addTransition('a', state2)
        state2.addTransition('b', state3)
        state2.addTransition('c', state3)
        state3.addTransition('d', state4)
        val fsm = NDFSM(listOf(state1, state2, state3, state4), state1, listOf(state4))
        val checks = listOf(
                "abd" to true,
                "acd" to true,
                "ac" to false,
                "acdd" to false,
                "sdjfksdjkj fksjdfkjds sjdfkj" to false)
        this.runTestsForFSM(fsm, checks)
    }

    @Test
    fun `NDFSM works properly for a*b*c*`() {
        val state1 = NDFSMState()
        val state2 = NDFSMState()
        val state3 = NDFSMState()
        state1.addTransition('a', state1)
        state1.addTransition(EMPTY_MOVE_CHARACTER, state2)
        state2.addTransition('b', state2)
        state2.addTransition(EMPTY_MOVE_CHARACTER, state3)
        state3.addTransition('c', state3)
        val fsm = NDFSM(listOf(state1, state2, state3), state1, listOf(state3))
        val checks = listOf(
                "" to true,
                "abc" to true,
                "c" to true,
                "aaabbbbbccccccc" to true,
                "bac" to false,
                "acdd" to false,
                "sdjfksdjkj fksjdfkjds sjdfkj" to false)
        this.runTestsForFSM(fsm, checks)
    }

    @Test
    fun `NDFSM to DFSM works for a*b*c*`() {
        val state1 = NDFSMState()
        val state2 = NDFSMState()
        val state3 = NDFSMState()
        state1.addTransition('a', state1)
        state1.addTransition(EMPTY_MOVE_CHARACTER, state2)
        state2.addTransition('b', state2)
        state2.addTransition(EMPTY_MOVE_CHARACTER, state3)
        state3.addTransition('c', state3)
        val fsm = NDFSM(listOf(state1, state2, state3), state1, listOf(state3))
        val dfsm = fsm.toDFSM()
        val checks = listOf(
                "" to true,
                "abc" to true,
                "c" to true,
                "aaabbbbbccccccc" to true,
                "bac" to false,
                "acdd" to false,
                "sdjfksdjkj fksjdfkjds sjdfkj" to false)
        this.runTestsForFSM(dfsm, checks)
    }

    @Test
    fun `NDFSM to DFSM works for a*ab*bc*c`() {
        val state1 = NDFSMState()
        val state2 = NDFSMState()
        val state3 = NDFSMState()
        val state4 = NDFSMState()
        state1.addTransition('a', state1)
        state1.addTransition('a', state2)
        state2.addTransition('b', state2)
        state2.addTransition('b', state3)
        state3.addTransition('c', state3)
        state3.addTransition('c', state4)
        val fsm = NDFSM(listOf(state1, state2, state3, state4), state1, listOf(state4))
        val dfsm = fsm.toDFSM()
        val checks = listOf(
                "abc" to true,
                "aaabc" to true,
                "aabcccc" to true,
                "aaabbbbbccccccc" to true,
                "ab" to false,
                "ac" to false,
                "sdjfksdjkj fksjdfkjds sjdfkj" to false)
        this.runTestsForFSM(dfsm, checks)
    }
}
