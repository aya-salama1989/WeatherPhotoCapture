package com.robusta.weatherphotocapture.photo.gallery

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.robusta.weatherphotocapture.R
import kotlinx.android.synthetic.main.fragment_share_photo.view.*
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore


class SharePhotoFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_share_photo, container, false)
        setLayout(view)
        return view
    }

    private fun setLayout(view: View) {
        val photoURI = arguments?.getString(KEY_PHOTO_URI)?:""
        Glide.with(requireActivity())
            .load(photoURI)
            .error(R.drawable.common_full_open_on_phone)
            .into(view.ivFullPhoto)

        view.btn_share.setOnClickListener {
            val sharingIntent = Intent(Intent.ACTION_SEND)
            val screenshotUri: Uri = Uri.parse(photoURI)
            sharingIntent.type = "image/jpeg"
            sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri)
            startActivity(Intent.createChooser(sharingIntent, "Share image using"))
        }
    }


}