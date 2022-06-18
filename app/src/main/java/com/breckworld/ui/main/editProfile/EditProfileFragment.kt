package com.breckworld.ui.main.editProfile

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.breckworld.R
import com.breckworld.architecture.BaseMVVMFragment
import com.breckworld.databinding.FragmentEditProfileBinding
import com.breckworld.livedata.EventObserver
import com.breckworld.util.AppUtil
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import permissions.dispatcher.*
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import java.io.File

/**
 * @author Dmytro Bondarenko
 *         Date: 10.06.2019
 *         Time: 14:44
 *         E-mail: bondes87@gmail.com
 */
@RuntimePermissions
class EditProfileFragment :
    BaseMVVMFragment<EditProfileViewModel, FragmentEditProfileBinding, EditProfileFragment.Events>() {

    override fun viewModelClass(): Class<EditProfileViewModel> = EditProfileViewModel::class.java

    override fun layoutResId(): Int = R.layout.fragment_edit_profile

    override fun subscribeToEvents() {
        viewModel.eventsLiveData.observe(this, EventObserver { events ->
            when (events) {
                Events.PICK_IMAGE -> enableCameraWithPermissionCheck()
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ////AppUtil.makeStatusBarTransparent(1, requireActivity())
        showBottomNavigation(false)
        //setupStatusbar(true, true)
        setupStatusBarPadding()
        viewModel.profile.removeObservers(this)
        viewModel.profile.observe(this, Observer { profileDB ->
            profileDB?.let { viewModel.initData(it) }
        })

        binding.imageViewBack.setOnClickListener(View.OnClickListener {
            AppUtil.hideSoftKeyboard(requireContext())
            activity?.finish()
        })
    }

    override fun onStart() {
        super.onStart()
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    override fun onStop() {
        super.onStop()
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }

    @NeedsPermission(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun enableCamera() {
        if (ActivityCompat.checkSelfPermission(context!!, Manifest.permission.CAMERA) !=
            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context!!,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        EasyImage.openChooserWithGallery(this, "", 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        EasyImage.handleActivityResult(requestCode, resultCode, data, activity, object : DefaultCallback() {
            override fun onImagePickerError(e: Exception?, source: EasyImage.ImageSource?, type: Int) {
                showToast(getString(R.string.try_again))
            }

            override fun onImagesPicked(imagesFiles: List<File>, source: EasyImage.ImageSource, type: Int) {
                activity?.runOnUiThread {
                    createImageFile(imagesFiles)?.let {
                        viewModel.isAvatarChanged.value = true
                        viewModel.profileAvatarFile.value = it
                    }
                }
            }
        })
    }

    private fun createImageFile(imageFiles: List<File>): File? {
        return Compressor(activity)
            .setQuality(90)
            .setCompressFormat(Bitmap.CompressFormat.JPEG)
            .setDestinationDirectoryPath(
                Environment.getExternalStorageDirectory().toString() + File.separator + getString(R.string.app_name)
            )
            .compressToFile(imageFiles[0])
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    private fun setupStatusBarPadding() {
        val top = scroll_view_content.paddingTop + getStatusBarHeight()
        val bottom = scroll_view_content.paddingBottom
        val start = scroll_view_content.paddingStart
        val end = scroll_view_content.paddingEnd
        scroll_view_content.setPadding(start, top, end, bottom)
    }

    enum class Events {
        PICK_IMAGE
    }
}