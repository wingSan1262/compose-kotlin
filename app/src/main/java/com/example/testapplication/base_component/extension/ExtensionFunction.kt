package com.example.testapplication.base_component.extension

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testapplication.base_component.entities.Event
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import vanrrtech.app.ajaib_app_sample.domain.data_model.github.request.SearchPeopleRequest


fun <K> AppCompatActivity.observeEvent(
    data: LiveData<Event<K>>,
    callback : (K) -> Unit
) {
    data.observe(this) { it ->
        it.contentIfNotHandled?.let {
            callback(it)
        }
    }
}

fun EditText.textChanges(): Flow<CharSequence?> {
    return callbackFlow<CharSequence?> {
        val listener = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = Unit
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
                Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                trySend(s).isSuccess //TODO: Replace offer with trySend when we update kotlin coroutine version
            }
        }
        addTextChangedListener(listener)
        awaitClose { removeTextChangedListener(listener) }
    }.onStart{ emit(text) }
}

fun RecyclerView.addOnBottomScrollListener(onBottomReached : ()->Unit = {}){
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            (recyclerView.layoutManager as? LinearLayoutManager)?.let {
                if (dy >= 0 ) {
                    val visibleItemCount = it.childCount
                    val totalItemCount = it.itemCount
                    val pastVisibleItems = it.findFirstVisibleItemPosition()
                    if (visibleItemCount + pastVisibleItems >= totalItemCount) {
                        onBottomReached()
                    }
                }
            }
        }
    })
}

fun AppCompatActivity.showSnackBar(view : View, msg : String){
    Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show()
}

fun View.setVisibility(boolean: Boolean){
    this.visibility = if(boolean) View.VISIBLE else View.GONE
}
fun View.setVisibilityInvisible(boolean: Boolean){
    this.visibility = if(boolean) View.VISIBLE else View.INVISIBLE
}