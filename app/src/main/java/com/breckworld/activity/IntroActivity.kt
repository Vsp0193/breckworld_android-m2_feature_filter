package com.breckworld.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.breckworld.R
import com.breckworld.adapter.IntroAdapter
import com.breckworld.databinding.ActivityIntroBinding
import com.breckworld.model.IntroModel
import java.util.*


class IntroActivity : AppCompatActivity() {

    lateinit var introBinding: ActivityIntroBinding
    private var currentPage = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        introBinding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(introBinding.root)

        window.navigationBarColor = ContextCompat.getColor(this, R.color.colorBlackType1)

        setDataForSlider()
        introBinding.textViewNext.setOnClickListener {

            if (introBinding.textViewNext.text.equals("Finish")) {
                val intent = Intent(this@IntroActivity, HomeActivity::class.java)
                startActivity(intent)
            }

            introBinding.pager.setCurrentItem(currentPage++, true)
            if (currentPage >= 7)
                introBinding.textViewNext.setText("Finish")
            else
                introBinding.textViewNext.setText("Next")
        }

        introBinding.textViewSkipIntro.setOnClickListener {
            val intent = Intent(this@IntroActivity, HomeActivity::class.java)
            startActivity(intent)
        }

        introBinding.pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                currentPage = position+1
                if (position >= 6)
                    introBinding.textViewNext.setText("Finish")
                else
                    introBinding.textViewNext.setText("Next")
            }
        })

    }

    private fun setDataForSlider() {
        val introModel = ArrayList<IntroModel>()
        introModel.add(
            IntroModel(
                "Introducing Real World Search",
                "Ever wanted to find instant info about the place you're looking at? Well, now you can with Real World Search. \n\nSee what a shop is like before you step inside. Learn about a landmark. Even check where the closest toilet is if you get caught short. There are so many uses for this world-first function. \n\nAlternatively, sit at home and discover things to do around you that you perhaps didn't know existed."
            )
        )
        introModel.add(
            IntroModel(
                "Introducing Real World Search",
                "Ever wanted to find instant info about the place you're looking at? Well, now you can with Real World Search. \n\nSee what a shop is like before you step inside. Learn about a landmark. Even check where the closest toilet is if you get caught short. There are so many uses for this world-first function. \n\nAlternatively, sit at home and discover things to do around you that you perhaps didn't know existed."
            )
        )
        introModel.add(
            IntroModel(
                "Introducing Real World Search",
                "Ever wanted to find instant info about the place you're looking at? Well, now you can with Real World Search. \n\nSee what a shop is like before you step inside. Learn about a landmark. Even check where the closest toilet is if you get caught short. There are so many uses for this world-first function. \n\nAlternatively, sit at home and discover things to do around you that you perhaps didn't know existed."
            )
        )
        introModel.add(
            IntroModel(
                "Introducing Real World Search",
                "Ever wanted to find instant info about the place you're looking at? Well, now you can with Real World Search. \n\nSee what a shop is like before you step inside. Learn about a landmark. Even check where the closest toilet is if you get caught short. There are so many uses for this world-first function. \n\nAlternatively, sit at home and discover things to do around you that you perhaps didn't know existed."
            )
        )
        introModel.add(
            IntroModel(
                "Introducing Real World Search",
                "Ever wanted to find instant info about the place you're looking at? Well, now you can with Real World Search. \n\nSee what a shop is like before you step inside. Learn about a landmark. Even check where the closest toilet is if you get caught short. There are so many uses for this world-first function. \n\nAlternatively, sit at home and discover things to do around you that you perhaps didn't know existed."
            )
        )
        introModel.add(
            IntroModel(
                "Introducing Real World Search",
                "Ever wanted to find instant info about the place you're looking at? Well, now you can with Real World Search. \n\nSee what a shop is like before you step inside. Learn about a landmark. Even check where the closest toilet is if you get caught short. There are so many uses for this world-first function. \n\nAlternatively, sit at home and discover things to do around you that you perhaps didn't know existed."
            )
        )
        introModel.add(
            IntroModel(
                "Introducing Real World Search",
                "Ever wanted to find instant info about the place you're looking at? Well, now you can with Real World Search. \n\nSee what a shop is like before you step inside. Learn about a landmark. Even check where the closest toilet is if you get caught short. There are so many uses for this world-first function. \n\nAlternatively, sit at home and discover things to do around you that you perhaps didn't know existed."
            )
        )
        introBinding.pager.adapter = IntroAdapter(this, introModel)
        introBinding.indicator.setViewPager(introBinding.pager)

        // Auto start of viewpager
        /*val handler = Handler()
        val Update = Runnable {
            if (currentPage == introModel.size) {
                currentPage = 0
            }
            introBinding.pager.setCurrentItem(currentPage++, true)
        }
        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                handler.post(Update)
            }
        }, 2000, 4000)*/
    }

}