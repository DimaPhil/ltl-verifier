package automaton

import org.w3c.dom.Element
import org.w3c.dom.Node
import org.xml.sax.SAXException
import java.io.File
import java.io.IOException
import java.util.*
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException

class Automaton(val events: List<Event>, val states: List<State>, val transitions: List<Transition>) {
    data class Event(val name: String, val comment: String)

    data class State(val id: Int, val name: String, val type: Int, val incoming: List<Int>, val outgoing: List<Int>)

    data class Transition(val id: Int, val event: String, val guard: String, val actions: List<String>, val code: String)

    companion object {

        @Throws(ParserConfigurationException::class, IOException::class, SAXException::class)
        fun readFromFile(filename: String): Automaton {
            val file = File(filename)
            val dbFactory = DocumentBuilderFactory.newInstance()
            val dBuilder = dbFactory.newDocumentBuilder()
            val doc = dBuilder.parse(file)
            doc.documentElement.normalize()

            val events = ArrayList<Event>()
            val states = ArrayList<State>()
            val transitions = ArrayList<Transition>()

            val stateMachine = doc.getElementsByTagName("Statemachine").item(0) as Element
            val eventsList = stateMachine.getElementsByTagName("event")
            for (i in 0 until eventsList.length) {
                val e = eventsList.item(i) as Element
                val name = e.getAttribute("name")
                val comment = e.getAttribute("comment")
                events.add(Event(name, comment))
            }

            val widgets = doc.getElementsByTagName("widget")
            for (i in 0 until widgets.length) {
                val item = widgets.item(i)
                if (item.nodeType != Node.ELEMENT_NODE)
                    continue
                val e = item as Element
                val id = Integer.valueOf(e.getAttribute("id"))
                val widgetType = e.getAttribute("type")
                when (widgetType) {
                    "State" -> {
                        val stateAttributes = e.getElementsByTagName("attributes").item(0) as Element
                        val name = stateAttributes.getElementsByTagName("name").item(0).textContent
                        val stateType = stateAttributes.getElementsByTagName("type").item(0).textContent

                        val incoming = ArrayList<Int>()
                        val incomingList = stateAttributes.getElementsByTagName("incoming")
                        for (incIdx in 0 until incomingList.length) {
                            val incId = (incomingList.item(incIdx) as Element).getAttribute("id")
                            incoming.add(Integer.valueOf(incId))
                        }

                        val outgoing = ArrayList<Int>()
                        val outgoingList = stateAttributes.getElementsByTagName("outgoing")
                        for (outIdx in 0 until outgoingList.length) {
                            val outId = (outgoingList.item(outIdx) as Element).getAttribute("id")
                            outgoing.add(Integer.valueOf(outId))
                        }

                        val state = State(id, name, Integer.valueOf(stateType), incoming, outgoing)
                        states.add(state)
                    }
                    "Transition" -> {
                        val stateAttributes = e.getElementsByTagName("attributes").item(0) as Element
                        val eventElement = stateAttributes.getElementsByTagName("event").item(0) as Element
                        val event = eventElement.getAttribute("name")
                        val code = stateAttributes.getElementsByTagName("code").item(0).textContent
                        val guard = stateAttributes.getElementsByTagName("guard").item(0).textContent

                        val actions = ArrayList<String>()
                        val actionsList = stateAttributes.getElementsByTagName("action")
                        for (actIdx in 0 until actionsList.length) {
                            val actionElement = actionsList.item(actIdx) as Element
                            val actionName = actionElement.getAttribute("name")
                            actions.add(actionName)
                        }

                        val transition = Transition(id, event, guard, actions, code)
                        transitions.add(transition)
                    }
                    else -> throw IllegalStateException("Unknown widget type: $widgetType")
                }
            }

            return Automaton(events, states, transitions)
        }
    }
}
