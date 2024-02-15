package voloshyn.android.data.popularPlacesStorage.multichoice

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import voloshyn.android.data.popularPlacesStorage.PopularPlace
import javax.inject.Inject


class MultiChoiceHandlerImpl @Inject constructor() : MultiChoiceHandler<PopularPlace>,
    MultiChoiceState<PopularPlace> {

    private val checkedIds = HashSet<Int>()
    private var items: List<PopularPlace> = emptyList()
    private val stateFlow = MutableStateFlow(OnChanged())

    override fun setItemsFlow(coroutineScope: CoroutineScope, itemsFlow: Flow<List<PopularPlace>>) {
        coroutineScope.launch {
            itemsFlow.collectLatest { list ->
                items = list
                checkedIds.clear() // Clear the previous checked IDs
                list.filter { it.isChecked }.forEach { checkedIds.add(it.name) }
                notifyUpdates()
            }
        }
    }

    override fun listen(): Flow<MultiChoiceState<PopularPlace>> {
        return stateFlow.map { this }
    }

    override fun clear(item: PopularPlace) {
        if (!exists(item)) return
        checkedIds.remove(item.name)
        notifyUpdates()
    }

    override fun check(item: PopularPlace) {
        if (!exists(item)) return
        checkedIds.add(item.name)
        notifyUpdates()
    }

    override fun toggle(item: PopularPlace) {
        if (isChecked(item)) {
            clear(item)
        } else {
            check(item)
        }
    }

    override fun isChecked(item: PopularPlace): Boolean {
        return checkedIds.contains(item.name)
    }

    private fun exists(item: PopularPlace): Boolean {
        return items.any { it.name == item.name }
    }

    override val checkedItem: List<PopularPlace>
        get() = items.filter { checkedIds.contains(it.name) }.
            map { element ->
                element.copy(isChecked = true)
            }



    private fun notifyUpdates() {
        stateFlow.value = OnChanged()
    }

    private class OnChanged
}