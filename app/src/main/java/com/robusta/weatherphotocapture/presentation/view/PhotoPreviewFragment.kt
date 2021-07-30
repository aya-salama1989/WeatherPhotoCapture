package com.robusta.weatherphotocapture.presentation.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.robusta.weatherphotocapture.R
import com.robusta.weatherphotocapture.extensions.convertToBitmap
import com.robusta.weatherphotocapture.extensions.rotateBitMap
import com.robusta.weatherphotocapture.presentation.capture.KEY_CITY_NAME
import com.robusta.weatherphotocapture.presentation.capture.KEY_IMAGE_BITMAP
import com.robusta.weatherphotocapture.presentation.gallery.KEY_PHOTO_URI
import kotlinx.android.synthetic.main.fragment_share_photo.view.*

/**
 * This class covers two use cases
 * 1- user opens a photo from gallery, photo has a Global share button
 * @param photoURI is the local photo file URI
 * 2- user takes a photo, preview the results and save or discard it
 * @param photo taken photo ByteArray
 */
class PhotoPreviewFragment : Fragment() {

    private lateinit var cityName:String

    private val previewModel: PhotoPreviewVM by lazy {
        ViewModelProvider(this).get(PhotoPreviewVM::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cityName = arguments?.getString(KEY_CITY_NAME)?:""
        if(!cityName.isNullOrEmpty()){
            previewModel.getWeatherData(cityName)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_share_photo, container, false)
        val photoURI = arguments?.getString(KEY_PHOTO_URI) ?: ""
        val photo = arguments?.getByteArray(KEY_IMAGE_BITMAP)

        view.loadingView.visibility = View.VISIBLE
        if (photo != null) {
            setPreviewLayout(view, photo)
        } else {
            setViewImageLayout(view, photoURI)
        }
        view.loadingView.visibility = View.GONE
        return view
    }

    private fun setViewImageLayout(view: View, photoURI: String) {
        view.group.visibility = View.GONE
        Glide.with(requireActivity())
            .load(photoURI)
            .error(R.drawable.common_full_open_on_phone)
            .into(view.ivFullPhoto)

        view.btn_share.visibility = View.VISIBLE
        view.btn_share.setOnClickListener {
            val sharingIntent = Intent(Intent.ACTION_SEND)
            val screenshotUri: Uri = Uri.parse(photoURI)
            sharingIntent.type = "image/jpeg"
            sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri)
            startActivity(Intent.createChooser(sharingIntent, "Share image using"))
        }
    }

    private fun setPreviewLayout(view: View, photo: ByteArray) {
        view.group.visibility = View.VISIBLE
        val p = photo.convertToBitmap()
        view.ivFullPhoto.setImageBitmap(p.rotateBitMap(90))
        previewModel.temperature.observe(viewLifecycleOwner, {
            view.tv_temprature.text = it.temp.toString()
            view.tv_city_name.text = cityName
            view.tv_weather_condition.text = "Feels like: ${it.feelsLike}"
        })

        view.btn_save.visibility = View.VISIBLE
        view.btn_save.setOnClickListener {
            view.btn_save.visibility = View.GONE
            view.loadingView.visibility = View.VISIBLE
            view.isDrawingCacheEnabled = true
            val bitMap =view.getDrawingCache(true)
            previewModel.saveImage(bitMap)
        }

        previewModel.navigateToGallery.observe(viewLifecycleOwner, {
            if(it){
                findNavController().navigate(R.id.dest_galleryFragment)
            }else{
                Toast.makeText(requireActivity(),"An error happened while saving the Image",Toast.LENGTH_LONG).show()
                findNavController().navigate(R.id.dest_mainFragment)
            }
        })
    }


}