package voloshyn.android.data.dataSource.popularPlacesStorage.multichoice

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface MultiChoiceHandler<T : Any> {

    fun setItemsFlow(coroutineScope: CoroutineScope, itemsFlow: Flow<List<T>>)

    fun listen(): Flow<MultiChoiceState<T>>

    fun toggle(item: T)

    fun check(item: T)

    fun clear(item: T)
}