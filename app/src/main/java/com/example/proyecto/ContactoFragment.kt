package com.example.proyecto

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.proyecto.databinding.FragmentContactoBinding
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class ContactoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title = "CONTACTANOS"

        val binding = DataBindingUtil.inflate<FragmentContactoBinding>(inflater,
            R.layout.fragment_contacto, container, false)

        centerTitle()

        binding.ivFacebook.setOnClickListener {
            /*try {
                activity!!.packageManager.getPackageInfo("com.facebook.katana", 0)
                var i = Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/1895401814079636"))
            } catch (e: Exception) {
                var i = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/Catrinsv"))
            }

            startActivity()*/
        }

        return binding.root
    }
    //Centrar texto en ActionBar
    private fun centerTitle() {
        val textViews = ArrayList<View>()
        activity?.window?.decorView?.findViewsWithText(textViews, activity?.title, View.FIND_VIEWS_WITH_TEXT)
        if (textViews.size > 0) {
            var appCompatTextView: AppCompatTextView? = null
            if (textViews.size == 1)
                appCompatTextView = textViews[0] as AppCompatTextView
            else {
                for (v in textViews) {
                    if (v.parent is Toolbar) {
                        appCompatTextView = v as AppCompatTextView
                        break
                    }
                }
            }
            if (appCompatTextView != null) {
                val params = appCompatTextView.layoutParams
                params.width = ViewGroup.LayoutParams.MATCH_PARENT
                appCompatTextView.layoutParams = params
                appCompatTextView.textAlignment = View.TEXT_ALIGNMENT_CENTER
            }
        }
    }

    private fun getOpenFacebookIntent() {

    }
}
