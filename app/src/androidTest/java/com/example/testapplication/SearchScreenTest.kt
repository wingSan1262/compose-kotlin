package com.example.testapplication


import android.content.Intent
import android.content.res.Resources
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.testapplication.feature.MainActivity
import com.example.testapplication.feature.PeopleViewHolder
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.*
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
class SearchScreenTest {
    val intent = Intent(ApplicationProvider.getApplicationContext(),
        MainActivity::class.java).putExtra("title", "Testing rules!")
    @Rule @JvmField val activityScenario = ActivityScenarioRule<MainActivity>(intent)

    var activity : MainActivity? = null
    @Before
    fun setup(){
        activityScenario.scenario.onActivity {
            activity = it
        }
    }

    @Test fun searchScreen_shownUiComponent() {
        runBlocking {
            delay(1000)
            onView(withId(R.id.cv_search)).check(matches(isDisplayed()))
            onView(withId(R.id.listRv)).check(matches(isDisplayed()))
        }
    }
    @Test fun searchScreen_shownLoadingUiComponent() {
        runBlocking {
            delay(2000)
            onView(withId(R.id.cv_loading)).check(matches(isDisplayed()))
        }
    }
    @Test fun searchScreen_checkTheEditText() {
        runBlocking {
            delay(1000)
            onView(withId(R.id.cv_search)).check(matches(isDisplayed()))
            onView(withId(R.id.search_field)).perform(typeText("test"))
            Espresso.closeSoftKeyboard()
            onView(withId(R.id.search_field)).check(
                matches(withText("test")))
        }
    }
    @Test fun searchScreen_scrollCheckRecyclerView() {
        runBlocking {
            delay(3000)
            onView(withId(R.id.listRv)).perform(
                // scrollTo will fail the test if no item matches.
                RecyclerViewActions.actionOnItemAtPosition<PeopleViewHolder>(
                    7,
                    scrollTo()
                )
            )

            delay(3000)
            onView(
                RecyclerViewMatcher(R.id.listRv)
                    .atPositionOnView(7, R.id.name_tv))
                .check(matches(withText("R5-D4")))
            onView(
                RecyclerViewMatcher(R.id.listRv)
                    .atPositionOnView(7, R.id.gender_tv))
                .check(matches(withText("n/a")))
            onView(
                RecyclerViewMatcher(R.id.listRv)
                    .atPositionOnView(7, R.id.height_tv))
                .check(matches(withText("97 Cm")))
            onView(
                RecyclerViewMatcher(R.id.listRv)
                    .atPositionOnView(7, R.id.mass_tv))
                .check(matches(withText("32 Kg")))
            onView(
                RecyclerViewMatcher(R.id.listRv)
                    .atPositionOnView(7, R.id.birth_tv))
                .check(matches(withText("unknown")))
            onView(
                RecyclerViewMatcher(R.id.listRv)
                    .atPositionOnView(7, R.id.hair_tv))
                .check(matches(withText("n/a Hair")))
            onView(
                RecyclerViewMatcher(R.id.listRv)
                    .atPositionOnView(7, R.id.skin_tv))
                .check(matches(withText("white, red Skin")))
            onView(
                RecyclerViewMatcher(R.id.listRv)
                    .atPositionOnView(7, R.id.eye_tv))
                .check(matches(withText("red Eye")))
        }
    }
    @Test fun searchScreen_scrollCheckRecyclerView_pagingLoadUi() {
        runBlocking {
            delay(5000)
            onView(withId(R.id.listRv)).perform(ScrollToBottomAction())
            delay(1000)
            onView(withId(R.id.cv_loading)).check(matches(isDisplayed()))
            delay(1000)
            onView(withId(R.id.cv_loading)).check(matches(not(isDisplayed())))
        }
    }
    @Test fun searchScreen_scrollCheckRecyclerView_pagingLoadContent() {
        runBlocking {
            waitForFirstLoad()
            scrollingToMaxCount()
            assertThat(activity?.searchViewModel?.maxCount!!, `is`(activity?.adapter?.itemCount!!))
        }
    }
    @Test fun searchScreen_scrollCheckRecyclerView_canScrollToTopAgain() {
        runBlocking {
            waitForFirstLoad()

            scrollingToMaxCount()
            assertThat(activity?.searchViewModel?.maxCount!!, `is`(activity?.adapter?.itemCount!!))

            onView(withId(R.id.listRv)).perform(
                // scrollTo will fail the test if no item matches.
                RecyclerViewActions.actionOnItemAtPosition<PeopleViewHolder>(
                    0,
                    scrollTo()
                )
            )
            delay(500)
        }
    }
    @Test fun singleItem_searchingFunctionOnRecyclerViewItem() {
        runBlocking {
            delay(1000)
            onView(withId(R.id.cv_search)).check(matches(isDisplayed()))
            onView(withId(R.id.search_field)).perform(typeText("R5-D4"))
            Espresso.closeSoftKeyboard()
            onView(withId(R.id.search_field)).check(
                matches(withText("R5-D4")))
            delay(3000)
            val textContent = getText(RecyclerViewMatcher(R.id.listRv)
                .atPositionOnView(0, R.id.name_tv))
            if (textContent?.contains("R5-D4", true) != true)
                throw Throwable("search function fail")
        }
    }
    @Test fun multipleItem_searchingFunctionOnRecyclerViewItem() {
        runBlocking {
            delay(1000)
            onView(withId(R.id.cv_search)).check(matches(isDisplayed()))
            onView(withId(R.id.search_field)).perform(typeText("skywalker"))
            Espresso.closeSoftKeyboard()
            onView(withId(R.id.search_field)).check(
                matches(withText("skywalker")))
            delay(3000)
            var textContent = getText(RecyclerViewMatcher(R.id.listRv)
                .atPositionOnView(0, R.id.name_tv))
            if (textContent?.contains("skywalker", true) != true)
                throw Throwable("search function fail")

            textContent = getText(RecyclerViewMatcher(R.id.listRv)
                .atPositionOnView(1, R.id.name_tv))
            if (textContent?.contains("skywalker", true) != true)
                throw Throwable("search function fail")

            textContent = getText(RecyclerViewMatcher(R.id.listRv)
                .atPositionOnView(2, R.id.name_tv))
            if (textContent?.contains("skywalker", true) != true)
                throw Throwable("search function fail")
        }
    }
    @Test fun multipleItem_searchingFunctionOnRecyclerViewItem_scollingtest() {
        runBlocking {
            delay(1000)
            onView(withId(R.id.cv_search)).check(matches(isDisplayed()))
            onView(withId(R.id.search_field)).perform(typeText("a"))
            Espresso.closeSoftKeyboard()
            onView(withId(R.id.search_field)).check(
                matches(withText("a")))
            waitForFirstLoad()
            onView(withId(R.id.listRv)).perform(ScrollToBottomAction())
            checkLoading()
            onView(withId(R.id.listRv)).perform(
                // scrollTo will fail the test if no item matches.
                RecyclerViewActions.actionOnItemAtPosition<PeopleViewHolder>(
                    0,
                    scrollTo()
                )
            )
            delay(500)
        }
    }
    @Test fun multipleItem_searchingFunctionOnRecyclerViewItem_scollingSizeTest() {
        runBlocking {
            delay(1000)
            onView(withId(R.id.cv_search)).check(matches(isDisplayed()))
            onView(withId(R.id.search_field)).perform(typeText("a"))
            Espresso.closeSoftKeyboard()
            onView(withId(R.id.search_field)).check(
                matches(withText("a")))
            waitForFirstLoad()

            scrollingToMaxCount()

            onView(withId(R.id.listRv)).perform(
                // scrollTo will fail the test if no item matches.
                RecyclerViewActions.actionOnItemAtPosition<PeopleViewHolder>(
                    0,
                    scrollTo()
                )
            )
            delay(500)
        }
    }


    /**
     * method helper :)
     */
    suspend fun scrollingToMaxCount(){
        var errorTry = 0
        while (activity!!.searchViewModel!!.maxCount > activity!!.adapter.itemCount){
            delay(200)
            onView(withId(R.id.listRv)).perform(ScrollToBottomAction())
            try {
                checkLoading()
            } catch (e : Throwable){
                errorTry = errorTry + 1
                if(errorTry > 4)
                    throw  Throwable("search scrolling fail")
            }
        }
    }
    suspend fun checkLoading(){
        for(i in 0..20){
            delay(100)
            try {
                onView(withId(R.id.cv_loading)).check(matches(isDisplayed()))
            } catch(e : Throwable) {
                if(i == 20)
                    throw Throwable("loading was not shown")
                continue
            }
            break
        }

        for(i in 0..20){
            delay(100)
            try {
                onView(withId(R.id.cv_loading)).check(matches(not(isDisplayed())))
            } catch(e : Throwable) {
                if(i == 20)
                    throw Throwable("loading was shown")
                continue
            }
            break
        }
        delay(500)
    }
    suspend fun waitForFirstLoad(){
        for(i in 0..10){
            delay(500)
            if(activity?.searchViewModel?.isQuerying != false){
                continue
            }
            delay(500)
            break
        }
        for(i in 0..10){
            delay(500)
            if(activity?.searchViewModel?.isQuerying != true){
                continue
            }
            delay(500)
            return
        }
    }


}

class ScrollToBottomAction : ViewAction {
    override fun getDescription(): String {
        return "scroll RecyclerView to bottom"
    }

    override fun getConstraints(): Matcher<View> {
        return allOf<View>(isAssignableFrom(RecyclerView::class.java), isDisplayed())
    }

    override fun perform(uiController: UiController?, view: View?) {
        val recyclerView = view as RecyclerView
        val itemCount = recyclerView.adapter?.itemCount
        val position = itemCount?.minus(1) ?: 0
        recyclerView.scrollToPosition(position)
        uiController?.loopMainThreadUntilIdle()
    }
}

fun getText(matcher: Matcher<View?>?): String? {
    val stringHolder = arrayOf<String?>(null)
    onView(matcher).perform(object : ViewAction {
        override fun getConstraints(): Matcher<View> {
            return isAssignableFrom(TextView::class.java)
        }

        override fun getDescription(): String {
            return "getting text from a TextView"
        }

        override fun perform(uiController: UiController, view: View) {
            val tv = view as TextView //Save, because of check in getConstraints()
            stringHolder[0] = tv.text.toString()
        }
    })
    return stringHolder[0]
}


class RecyclerViewMatcher(private val recyclerViewId: Int) {
    fun atPosition(position: Int): TypeSafeMatcher<View?> {
        return atPositionOnView(position, -1)
    }

    fun atPositionOnView(position: Int, targetViewId: Int): TypeSafeMatcher<View?> {
        return object : TypeSafeMatcher<View?>() {
            var resources: Resources? = null
            var childView: View? = null
            override fun describeTo(description: org.hamcrest.Description?) {
                var idDescription = Integer.toString(recyclerViewId)
                if (resources != null) {
                    idDescription = try {
                        resources!!.getResourceName(recyclerViewId)
                    } catch (var4: Resources.NotFoundException) {
                        String.format(
                            "%s (resource name not found)",
                            *arrayOf<Any>(Integer.valueOf(recyclerViewId))
                        )
                    }
                }
                description?.appendText("with id: $idDescription")
            }

            override fun matchesSafely(item: View?): Boolean {
                resources = item?.resources
                if (childView == null) {
                    val recyclerView = item?.rootView?.findViewById<View>(
                        recyclerViewId
                    ) as RecyclerView
                    childView = if (recyclerView != null && recyclerView.id == recyclerViewId) {
                        recyclerView.findViewHolderForAdapterPosition(position)!!.itemView
                    } else {
                        return false
                    }
                }
                return if (targetViewId == -1) {
                    item === childView
                } else {
                    val targetView = childView!!.findViewById<View>(targetViewId)
                    item === targetView
                }
            }
        }
    }
}