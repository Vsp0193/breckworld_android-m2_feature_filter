package com.breckworld.activity

import android.Manifest
import android.R.attr
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Base64
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.breckworld.App
import com.breckworld.R
import com.breckworld.databinding.ActivityEditProfileBinding
import com.breckworld.repository.Repository2
import com.breckworld.util.AppUtil
import com.breckworld.viewmodel.EditProfileVM
import com.breckworld.viewmodel.factory.EditProfileVMFactory
import com.breckworld.webservice.ApiClient
import com.breckworld.webservice.ApiInterface
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.photo_option.view.*
import pub.devrel.easypermissions.EasyPermissions


class EditProfileActivity : AppCompatActivity() {

    lateinit var editProfileBinding: ActivityEditProfileBinding
    private var apiService: ApiInterface? = null
    private lateinit var editProfileVM: EditProfileVM
    var dialog: Dialog? = null
    private val REQUEST_CODE_CAMERA: Int = 1
    private val REQUEST_CODE_GALLERY: Int = 2
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

    private var isWritePermission = false
    private var isReadPermission = false
    private var isCameraPermission = false
    private var imageBitmap: Bitmap? = null
    private var imageUrl: String? = ""
    private var selectedOption: Int? = 1 // 1 - Camera, 2 - Gallery

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        editProfileBinding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(editProfileBinding.root)
        apiService = ApiClient.createService(ApiInterface::class.java, this)
        val repositories = Repository2(apiService!!, this)

        editProfileVM = ViewModelProvider(
            this, EditProfileVMFactory(repositories)
        ).get(EditProfileVM::class.java)

        editProfileBinding.imageViewBack.setOnClickListener {
            AppUtil.hideSoftKeyboard(this@EditProfileActivity)
            super.onBackPressed()
        }

        // Auto fill
        editProfileBinding.editTextFirstName.setText(App.mLocalStore?.getUserProfile()?.first_name)
        editProfileBinding.editTextLastName.setText(App.mLocalStore?.getUserProfile()?.last_name)
        editProfileBinding.textViewEmail.setText(App.mLocalStore?.getUserProfile()?.email)
        Glide.with(this).load(App.mLocalStore?.getUserProfile()!!.profile_pic)
            .apply(RequestOptions.placeholderOf(R.drawable.user_placeholder).dontAnimate())
            .into(editProfileBinding.imageViewUserProfile)


        editProfileBinding.textViewSend.setOnClickListener {
            if (editProfileBinding.editTextFirstName.text.toString().trim().length <= 0) {
                Toast.makeText(
                    this@EditProfileActivity,
                    resources.getString(R.string.please_enter_your_first_name),
                    Toast.LENGTH_SHORT
                ).show()
            } else if (editProfileBinding.editTextLastName.text.toString().trim().length <= 0) {
                Toast.makeText(
                    this@EditProfileActivity,
                    resources.getString(R.string.please_enter_your_last_name),
                    Toast.LENGTH_SHORT
                ).show()
            } else {

                editProfileVM.editProfileApiCall(
                    this@EditProfileActivity,
                    this@EditProfileActivity,
                    App.mLocalStore?.accessToken.toString(),
                    (if (editProfileBinding.editTextFirstName.text.toString().trim().length <= 0) {
                        App.mLocalStore?.getUserProfile()?.first_name
                    } else {
                        editProfileBinding.editTextFirstName.text.toString().trim()
                    }).toString(),
                    (if (editProfileBinding.editTextLastName.text.toString().trim().length <= 0) {
                        App.mLocalStore?.getUserProfile()?.last_name
                    } else {
                        editProfileBinding.editTextLastName.text.toString().trim()
                    }).toString(),
                    if (imageBitmap != null) {
                        Base64.encodeToString(AppUtil.byteArrayImage(imageBitmap!!), Base64.DEFAULT)
                    } else if (!imageUrl.equals("")) {
                        Base64.encodeToString(AppUtil.stringToBase64(imageUrl!!), Base64.DEFAULT)
                    } else {
                        ""
                    }
                )
            }
        }

        //EnableRuntimePermission()

        editProfileBinding.layoutImage.setOnClickListener {
            //showBottomDialog()
            requestPermission()
        }

        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                isReadPermission =
                    permissions[Manifest.permission.READ_EXTERNAL_STORAGE] ?: isReadPermission
                isWritePermission =
                    permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] ?: isWritePermission
                isCameraPermission = permissions[Manifest.permission.CAMERA] ?: isCameraPermission

                if (isReadPermission && isCameraPermission && isWritePermission) {
                    if (selectedOption == 2) {
                        galleryIntent()
                    } else {
                        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        startActivityForResult(cameraIntent, REQUEST_CODE_CAMERA)
                    }
                } else {
                    showMandatoryPermissionsNeedDialog(this@EditProfileActivity);
                }
            }
    }

    private fun showBottomDialog() {
        try {
            var mBottomSheetDialog = BottomSheetDialog(this);
            var sheetView = layoutInflater.inflate(R.layout.photo_option, null);
            mBottomSheetDialog.setContentView(sheetView)
            sheetView.optionCamera.setOnClickListener {
                selectedOption = 1
                requestPermission()
                /*val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, 1)*/
                mBottomSheetDialog.hide()
            }

            sheetView.optionGallery.setOnClickListener {
                selectedOption = 2
                requestPermission()
                /*val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, 2)*/
                mBottomSheetDialog.hide()
            }

            sheetView.optionCancel.setOnClickListener {
                mBottomSheetDialog.hide()
            }

            mBottomSheetDialog.show()
        } catch (e: Exception) {
        }

    }

    private fun galleryIntent() {
        if (Build.VERSION.SDK_INT < 19) {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Select Document"),
                REQUEST_CODE_GALLERY
            )
        } else {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE_GALLERY)
        }
    }

    private fun showMandatoryPermissionsNeedDialog(context: Context) {
        dialog = Dialog(this)
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setCancelable(false)
        dialog?.setContentView(R.layout.item_permission)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val tv_detail = dialog?.findViewById(R.id.tv_detail) as AppCompatTextView
        val tvTitle = dialog?.findViewById(R.id.tvTitle) as AppCompatTextView

        val tv_edit = dialog?.findViewById(R.id.tv_edit) as TextView
        val tv_ok = dialog?.findViewById(R.id.tv_ok) as TextView

        tvTitle.text = getString(R.string.permission_alert)
        tv_detail.text = getString(R.string.mandatory_permission_access_required)
        tv_ok.text = getString(R.string.ok)
        tv_edit.text = getString(R.string.cancel)

        tv_ok.setOnClickListener {
            dialog?.dismiss()
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            val uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            startActivity(intent)
        }

        tv_edit.setOnClickListener {
            dialog?.dismiss()
        }
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.show()

    }

    fun requestPermission() {
        isReadPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        isWritePermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        isCameraPermission =
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED

        val permissionRequest: MutableList<String> = ArrayList()
        if (!isReadPermission) {
            permissionRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (!isWritePermission) {
            permissionRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (!isCameraPermission) {
            permissionRequest.add(Manifest.permission.CAMERA)
        }
        //if (permissionRequest.isNotEmpty()){
        permissionLauncher.launch(permissionRequest.toTypedArray())
        //}
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_CAMERA && data != null) {

            imageBitmap = data.extras?.get("data") as Bitmap
            editProfileBinding.imageViewUserProfile.setImageBitmap(data.extras?.get("data") as Bitmap)

        }
        else if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_GALLERY && data != null) {
            /*editProfileBinding.imageViewUserProfile.setImageURI(data?.data)
            var bitmap = (editProfileBinding.imageViewUserProfile.drawable as BitmapDrawable).bitmap
            AppUtil.base64ToString(bitmap)*/
            /*imageUrl = data.data?.toString()
            Log.d("IMAGE_URL", imageUrl + "")
            Glide.with(this).load(imageUrl).into(editProfileBinding.imageViewUserProfile)
            Base64.encodeToString(AppUtil.stringToBase64(imageUrl!!), Base64.DEFAULT)*/

            val galleryPermissions = arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )

            if (EasyPermissions.hasPermissions(this, *galleryPermissions)) {
                val selectedImage: Uri = data?.getData()!!
                val filePath = arrayOf(MediaStore.Images.Media.DATA)
                val c = contentResolver.query(selectedImage, filePath, null, null, null)
                c!!.moveToFirst()
                val columnIndex = c!!.getColumnIndex(filePath[0])
                val picturePath = c!!.getString(columnIndex)
                c!!.close()
                val thumbnail = BitmapFactory.decodeFile(picturePath)
                editProfileBinding.imageViewUserProfile.setImageBitmap(thumbnail)
            } else {
                EasyPermissions.requestPermissions(
                    this, "Access for storage",
                    101, *galleryPermissions
                )
            }
        }
    }


    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                val bitmap = data?.extras!!["data"] as Bitmap?
                editProfileBinding.imageViewUserProfile.setImageBitmap(bitmap)
            } else if (requestCode == 2) {
                val galleryPermissions = arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                if (EasyPermissions.hasPermissions(this, galleryPermissions.toString())) {
                    val selectedImage = data?.data
                    val filePath = arrayOf(MediaStore.Images.Media.DATA)
                    val c: Cursor? =
                        contentResolver.query(selectedImage!!, filePath, null, null, null)
                    c?.moveToFirst()
                    val columnIndex: Int = c?.getColumnIndex(filePath[0])!!
                    val picturePath: String = c.getString(columnIndex)
                    c.close()
                    val thumbnail = BitmapFactory.decodeFile(picturePath)
                    editProfileBinding.imageViewUserProfile.setImageBitmap(thumbnail)
                } else {
                    EasyPermissions.requestPermissions(
                        this@EditProfileActivity, "Access for storage",
                        101, galleryPermissions.toString()
                    )
                }
            }
        }
    }

    fun EnableRuntimePermission() {
        val RequestPermissionCode = 1
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this@EditProfileActivity,
                Manifest.permission.CAMERA
            )
        ) {
            Toast.makeText(
                this@EditProfileActivity,
                "CAMERA permission allows us to Access CAMERA app",
                Toast.LENGTH_LONG
            ).show()
        } else {
            ActivityCompat.requestPermissions(
                this@EditProfileActivity, arrayOf(
                    Manifest.permission.CAMERA
                ), RequestPermissionCode
            )
        }
    }

    @JvmName("onRequestPermissionsResult1")
    fun onRequestPermissionsResult(RC: Int, per: Array<String?>?, PResult: IntArray) {
        super.onRequestPermissionsResult(RC, per!!, PResult)
        val RequestPermissionCode = 1
        when (RC) {
            RequestPermissionCode -> if (PResult.size > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(
                    this@EditProfileActivity,
                    "Permission Granted, Now your application can access CAMERA.",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(
                    this@EditProfileActivity,
                    "Permission Canceled, Now your application cannot access CAMERA.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }*/
}