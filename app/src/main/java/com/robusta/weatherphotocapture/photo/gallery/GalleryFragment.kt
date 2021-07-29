package com.robusta.weatherphotocapture.photo.gallery

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.robusta.weatherphotocapture.R
import com.robusta.weatherphotocapture.extensions.allPermissionsGranted
import kotlinx.android.synthetic.main.gallery_fragment.view.*

const val KEY_PHOTO_URI="photo_uri"

class GalleryFragment : Fragment() {

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 20
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private val viewModel: GalleryViewModel by lazy {
        ViewModelProvider(this).get(GalleryViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.gallery_fragment, container, false)

        viewModel.imagesList.observe(viewLifecycleOwner, {
            setLayout(view.rvGallery, it)
        })

        if(!REQUIRED_PERMISSIONS.allPermissionsGranted(requireContext())){
            ActivityCompat.requestPermissions(
                requireActivity(),
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }


        return view
    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (REQUIRED_PERMISSIONS.allPermissionsGranted(requireContext())) {

            } else {
                Toast.makeText(
                    requireContext(), "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun setLayout(rvGallery: RecyclerView, imagesList: List<Uri>) {
        val galleryAdapter = GalleryRecyclerAdapter(PhotoClickListener {
            val bundle = Bundle()
            bundle.putString(KEY_PHOTO_URI, it.toString())
            findNavController().navigate(R.id.dest_photoFragment, bundle)
        })
        galleryAdapter.submitList(imagesList)
        rvGallery.adapter = galleryAdapter
    }

}