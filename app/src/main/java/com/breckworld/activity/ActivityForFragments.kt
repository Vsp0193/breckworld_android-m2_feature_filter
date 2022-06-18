package com.breckworld.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.breckworld.R
import com.breckworld.fragment.*
import com.breckworld.ui.login.privacyPolicy.PrivacyPolicyFragment
import com.breckworld.ui.login.termsOfService.TermsOfServiceFragment
import com.breckworld.ui.main.editProfile.EditProfileFragment
import com.breckworld.ui.main.reportIssue.ReportIssueFragment
import com.breckworld.ui.main.tutorial.TutorialFragment

class ActivityForFragments : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_for_fragments)

        if (intent != null) {
            if (intent.getStringExtra("fragmentName").equals("TermsOfService")) {
                addFragmentToActivity(TermsOfServiceFragment())
            } else if (intent.getStringExtra("fragmentName").equals("PrivacyPolicy")) {
                addFragmentToActivity(PrivacyPolicyFragment())
            } else if (intent.getStringExtra("fragmentName").equals("AppTutorial")) {
                addFragmentToActivity(TutorialFragment())
            } else if (intent.getStringExtra("fragmentName").equals("ReportAnIssue")) {
                addFragmentToActivity(ReportIssueFragment())
            } else if (intent.getStringExtra("fragmentName").equals("EditProfile")) {
                addFragmentToActivity(EditProfileFragment())
            }
        }
    }

    private fun addFragmentToActivity(fragment: Fragment?) {
        if (fragment == null) return
        val frag = supportFragmentManager.beginTransaction()
        frag.add(R.id.container, fragment)
        frag.addToBackStack(null)
        frag.commitAllowingStateLoss()
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.container)
        /*var intent = Intent(this@ActivityForFragments, HomeActivity::class.java)
        startActivity(intent)*/
        finish()

    }
}