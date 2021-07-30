package com.robusta.weatherphotocapture.presentation.capture

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.robusta.weatherphotocapture.R
import com.robusta.weatherphotocapture.extensions.allPermissionsGranted
import com.robusta.weatherphotocapture.extensions.convertToByteArray
import kotlinx.android.synthetic.main.capture_photo_fragment.*
import kotlinx.android.synthetic.main.capture_photo_fragment.view.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


const val KEY_CITY_NAME = "city_name"
const val KEY_IMAGE_BITMAP = "image_bitmap"

private const val REQUEST_CODE_PERMISSIONS = 10

class CapturePhotoFragment : Fragment() {

    private val imageCapture by lazy { ImageCapture.Builder().build() }

    private val TAG = this.javaClass.simpleName
    private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)

    private val viewModel: CapturePhotoViewModel by lazy {
        ViewModelProvider(this).get(CapturePhotoViewModel::class.java)
    }


    private lateinit var cameraExecutor: ExecutorService


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.capture_photo_fragment, container, false)
        setUpLayout(view)
        cameraExecutor = Executors.newSingleThreadExecutor()
        return view
    }



    private fun setUpLayout(view: View) {
        view.camera_capture_button.setOnClickListener {
            view.loadingView.visibility = View.VISIBLE
            viewModel.capturePhoto(imageCapture)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (REQUIRED_PERMISSIONS.allPermissionsGranted(requireContext())) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }

        val cityName = arguments?.getString(KEY_CITY_NAME) ?: ""
        viewModel.navigateToPhotoView.observe(viewLifecycleOwner, {
            if (it!=null){
                val bundle = Bundle()
                bundle.putString(KEY_CITY_NAME, cityName)
                bundle.putByteArray(KEY_IMAGE_BITMAP, it.convertToByteArray())
                findNavController().navigate(R.id.dest_photoFragment, bundle)
            }
        })
    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (REQUIRED_PERMISSIONS.allPermissionsGranted(requireContext())) {
                startCamera()
            } else {
                Toast.makeText(
                    requireContext(), "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(viewFinder.surfaceProvider)
            }

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector,
                    preview, imageCapture
                )
            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(context))
    }


    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}

