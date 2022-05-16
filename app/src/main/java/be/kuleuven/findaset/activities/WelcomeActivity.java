package be.kuleuven.findaset.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import java.util.ArrayList;

import be.kuleuven.findaset.R;

public class WelcomeActivity extends AppCompatActivity {
    private Button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        find
    }

        findAllModeBtn.onClick {
            launchActivity<LearnerDashboardActivity>()
            finish()

        }
        tvLogin.onClick {
            launchActivity<LearnLoginActivity>()
            finish()
        }
        vpWalkthough.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                tvHeading.text = mHeading[vpWalkthough.realItem]
            }


        })
    }

    internal inner class WalkAdapter : PagerAdapter() {
        private val mImg =
                arrayOf(R.drawable.learn_walk_1, R.drawable.learn_walk_2, R.drawable.learn_walk_3)

        override fun isViewFromObject(v: View, `object`: Any): Boolean {
            return v === `object` as View
        }

        override fun getCount(): Int {
            return mImg.size
        }

        override fun instantiateItem(parent: ViewGroup, position: Int): Any {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.learner_item_walk, parent, false)
            view.imgWalk2.loadImageFromResources(applicationContext, mImg[position])
            parent.addView(view)
            return view
        }

        override fun destroyItem(parent: ViewGroup, position: Int, `object`: Any) {
            parent.removeView(`object` as View)
        }
    }

}