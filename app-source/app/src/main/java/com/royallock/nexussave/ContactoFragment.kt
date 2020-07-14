package com.royallock.nexussave

/*
    This file is part of Nexus$ave.

    Nexus$ave is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Nexus$ave is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Nexus$ave.  If not, see <https://www.gnu.org/licenses/>.
 */

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.royallock.nexussave.databinding.FragmentContactoBinding


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

        Toast.makeText(this.activity, "click en cada logo para redirigirse a la pagina", Toast.LENGTH_LONG).show()

        binding.ivFacebook.setOnClickListener {
            startActivity(getOpenFacebookIntent())
        }
        binding.ivTwitter.setOnClickListener {
            startActivity(getOpenTwitterIntent())
        }

        binding.ivInstagram.setOnClickListener {
            startActivity(getOpenInstagramIntent())
        }

        return binding.root
    }


    private fun getOpenFacebookIntent(): Intent {
        try {
            activity!!.packageManager.getPackageInfo("com.facebook.katana", 0)
            var i = Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/101799554938532"))
            return i
        } catch (e: Exception) {
            var i = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/Royal-Lock-Company-101799554938532"))
            return i
        }
    }

    private fun getOpenTwitterIntent(): Intent {
        try {
            activity!!.packageManager.getPackageInfo("com.twitter.android", 0)
            var i = Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=1279179482767806465"))
            return i
        } catch (e: Exception) {
            var i = Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/RoyalLockCom"))
            return i
        }
    }

    private fun getOpenInstagramIntent(): Intent {
        try {
            activity!!.packageManager.getPackageInfo("com.instagram.android", 0)
            var i = Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/_u/royallockcompany"))
            return i
        } catch (e: Exception) {
            var i = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/royallockcompany"))
            return i
        }
    }
}
