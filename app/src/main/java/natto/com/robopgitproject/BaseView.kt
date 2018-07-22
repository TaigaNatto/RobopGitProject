package natto.com.robopgitproject

public interface BaseView<T> {
    fun setPresenter(presenter: T)
}