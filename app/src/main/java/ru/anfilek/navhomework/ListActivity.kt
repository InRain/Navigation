package ru.anfilek.navhomework

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ru.anfilek.navhomework.databinding.DialogCameraSelectBinding


class ListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        findViewById<FloatingActionButton>(R.id.fabStartCamera).setOnClickListener {
            startCameraFeature()
        }

        findViewById<Button>(R.id.buttonItem).setOnClickListener {
            startItemActivity()
        }

    }

    private fun startCameraFeature() {

        createDialogSelectCamera()

    }

    private fun startItemActivity() {
        val itemIntent = Intent(this,ItemActivity::class.java)
        itemIntent.putExtra(SOME_RESOURCE_ID,"This is some resource from ListActivity")
        startActivity(itemIntent)
    }


    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.CAMERA),
            REQUEST_PERMISSION_CAMERA
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_PERMISSION_CAMERA -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCameraFeature()
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED
                    ) {
                        showMessageOnDeniedPermission(resources.getString(R.string.please_provide_access_camera),
                            DialogInterface.OnClickListener { dialog, which ->
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    requestCameraPermission()
                                }
                            })
                    }
                }
            }
        }
    }

    private fun showMessageOnDeniedPermission(
        message: String,
        okListener: DialogInterface.OnClickListener
    ) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton(resources.getString(R.string.OK), okListener)
            .setNegativeButton(resources.getString(R.string.cancel), null)
            .create()
            .show()
    }

    private fun showPreRequestCameraPermissionDialog() {
        AlertDialog.Builder(this)
            .setMessage(resources.getString(R.string.pre_permission_request_dialog))
            .setPositiveButton(
                resources.getString(R.string.OK)
            ) { _, _ -> requestCameraPermission() }
            .setNegativeButton(resources.getString(R.string.cancel)
            ) { p0, _ -> p0?.dismiss() }
            .create()
            .show()
    }

    private fun createDialogSelectCamera(){
        val cameraDialogBuilder = AlertDialog.Builder(this)

        val view = layoutInflater.inflate(R.layout.dialog_camera_select,null)
        val dialogBinding = DialogCameraSelectBinding.inflate(layoutInflater,
            view as ViewGroup?, false)

        cameraDialogBuilder.setPositiveButton(resources.getString(R.string.OK)){_, _ ->
            when{
                dialogBinding.radioOwnCamera.isChecked ->{
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            this,
                            Manifest.permission.CAMERA
                        )
                    ) {
                        showPreRequestCameraPermissionDialog()
                    } else {
                        startActivity(Intent(this, CameraActivity::class.java))
                    }
                }
                dialogBinding.radioExtCamera.isChecked ->{
                    val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivity(takePictureIntent)
                }
            }

        }
        cameraDialogBuilder.setNegativeButton(resources.getString(R.string.cancel)){p0, _ -> p0.dismiss()}


        val dialog = cameraDialogBuilder.create()
        dialog.setView(dialogBinding.root)
        dialog.show()
    }

    companion object {
        const val REQUEST_PERMISSION_CAMERA = 1001
        const val SOME_RESOURCE_ID = "SOME_RESOURCE"
    }
}