package voloshyn.android.data.popularPlacesStorage.multichoice


interface MultiChoiceState<T> {

    fun isChecked(item: T): Boolean

    /**
     * The total number of checked items
     */
    val checkedItem: List<T>

}