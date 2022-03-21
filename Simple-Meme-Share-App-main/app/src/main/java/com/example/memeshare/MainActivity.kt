package com.example.memeshare

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_main.*

//9. For Sharing the meme, we need the current url of the image, so we create a global variable
// so we create a currentImageUrl of string type this is null
var currentImageUrl: String? = null

@Suppress("RedundantSamConstructor")
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadMeme()  // calling this function will give us a response
    }



/*
    //Created a private function loadMeme, which takes the volley code

    private fun loadMeme () {
        val textView = findViewById<TextView>(R.id.text)
// ...

// Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(this)
        val url = "https://www.google.com"

// Request a string response from the provided URL.
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                // Display the first 500 characters of the response string.
Log.d("Success Response" , response.substring(0, 500))            },
            Response.ErrorListener {
                Log.d("error", it.localizedMessage)
            })

// Add the request to the RequestQueue.
        queue.add(stringRequest)

    }


 */





    // 1. Created a private function loadMeme, which takes the volley code [Volley is a api processing and caching library]
    //we need to get the json response for the meme api

    private fun loadMeme () {

        //7. we need to show the progress bar when the loadMeme Function is called
        progressBar.visibility = View.VISIBLE



// 2. Instantiate the RequestQueue.
// val queue= Volley.newRequestQueue(this)
//  12.  we don`t require the above line of code, anymore because, we use a singleton volley pattern



// 3. Request a string response from the provided URL.
        val url = "https://meme-api.herokuapp.com/gimme"



//4. Getting the url data into jsonObject

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
               Response.Listener  {response -> currentImageUrl = response.getString("url")


                   // 5. Use Glide to Load the image into the ImageView, this will directly load the photo in the view
                  //  Glide.with(this).load(url).into(memeImageView)

                    //8.In order to vanish the progressBar when meme/photo loads or not, we put a listner in the glide method
                    //This listner method is interface that tells, what to do when image load passes or fails
                    Glide.with(this).load(currentImageUrl).listener(object : RequestListener<Drawable>{

                        override fun onLoadFailed
                                (e: GlideException?,
                                 model: Any?,
                                 target: Target<Drawable>?,
                                 isFirstResource: Boolean
                                ): Boolean {
                            progressBar.visibility = View.GONE
                            return false;
                        }

                        override fun onResourceReady
                                (resource: Drawable?,
                                 model: Any?,
                                 target: Target<Drawable>?,
                                 dataSource: DataSource?,
                                 isFirstResource: Boolean
                                  ): Boolean {
                            progressBar.visibility = View.GONE
                            return false;
                        }



                    }).into(memeImageView)



                },
               Response.ErrorListener  {
                    Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_LONG).show()
                }
        )


// 6. Add the request to the RequestQueue.
//   queue.add(jsonObjectRequest)

// 13. We will not use the above line of code to request the Queue, instead we will use the singleton pattern way of adding the json object to Queue
// We call the Queue form the MySingleton class
    MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)



}


    //7. In order to get the next meme, we will call the loadMeme() function
    fun nextMeme(view: View) {
        loadMeme()

    }



    fun shareMeme(view: View) {

        //10. Now to use the share function, we use intent, [Intent in Android is used for interprocessing communication]
        // There can be many actions that we can choose from here,we choose the send action
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_TEXT, "Hey,Checkout this cool meme i found out on Reddit! $currentImageUrl")
        //Here we choose which ty[es of apps should pop up for sharing, so below we choose any type of text sharing app
        intent.type = "text/plain"
        //Now we need to create a chooser [ that helps us in choosing the apps for sharing]
        val chooser = Intent.createChooser(intent, "Share this meme using...")
        startActivity(chooser)

    }
}