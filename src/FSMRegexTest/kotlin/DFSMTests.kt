import dfsm.DFSM
import dfsm.DFSMState
import fsm.EMPTY_MOVE_CHARACTER
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class DFSMTests {
    @Test
    fun statesWorkProperly() {
        val state1 = DFSMState()
        val state2 = DFSMState()
        val state3 = DFSMState()
        state1.addTransition('a', state2)
        state1.addTransition('b', state3)
        assertEquals(state2, state1.transitions['a'])
        assertEquals(state3, state1.transitions['b'])
    }

    @Test
    fun `DFSM states raises exception when used improperly`() {
        val state1 = DFSMState()
        val state2 = DFSMState()
        assertFails {
            state1.addTransition(EMPTY_MOVE_CHARACTER, state2)
        }
    }

    private fun runTestsForFSM(fsm: DFSM, checks: List<Pair<String, Boolean>>) {
        for ((string, result) in checks) {
            assertEquals(result, fsm.checkString(string), "Checking for \"$string\"")
        }
    }


    @Test
    fun `DFSM works properly for ab*c`() {
        val state1 = DFSMState()
        val state2 = DFSMState()
        val state3 = DFSMState()
        state1.addTransition('a', state2)
        state2.addTransition('b', state2)
        state2.addTransition('c', state3)
        val fsm = DFSM(listOf(state1, state2, state3), state1, listOf(state3))
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
        val state1 = DFSMState()
        val state2 = DFSMState()
        val state3 = DFSMState()
        val state4 = DFSMState()
        state1.addTransition('a', state2)
        state2.addTransition('b', state3)
        state2.addTransition('c', state3)
        state3.addTransition('d', state4)
        val fsm = DFSM(listOf(state1, state2, state3, state4), state1, listOf(state4))
        val checks = listOf(
                "abd" to true,
                "acd" to true,
                "ac" to false,
                "acdd" to false,
                "sdjfksdjkj fksjdfkjds sjdfkj" to false)
        this.runTestsForFSM(fsm, checks)
    }

}
