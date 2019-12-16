package com.rj.kotlinpractice


import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.dialog_content.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity(), OnClickListener {

    var TAG: String = "MainActivity"
    lateinit var recyclerView: RecyclerView
    lateinit var requestQueue: RequestQueue
    lateinit var stringRequest: StringRequest
    lateinit var data: ArrayList<MovieContent>
    var URL =
        "https://api.themoviedb.org/3/movie/now_playing?api_key=d82820e04f8c9e368c7b38e2fb0caf71&language=en-US&page=1"

    //https://image.tmdb.org/t/p/w500/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recyclerview)
        getData()
        data = ArrayList<MovieContent>()
    }

    fun getData() {
        stringRequest = StringRequest(
            Request.Method.GET,
            URL,
            Response.Listener { response ->
                Log.d(TAG, response)
                val jsonObject = JSONObject(response)
                val array = jsonObject.getJSONArray("results")
                for (i in 0 until array.length()) {
                    val subObject = array.getJSONObject(i)
                    data.add(MovieContent(
                        imageURL = subObject.getString("poster_path"),
                        title = subObject.getString("original_title"),
                        rating =  subObject.getDouble("vote_average").toString(),
                        overview =  subObject.getString("overview"),
                        genres = getGenres(subObject.getJSONArray("genre_ids"))))
                    Log.d("in function list size", data.size.toString())
                }
                setGridView(data)
            },
            Response.ErrorListener { error -> error.printStackTrace() }
        )
        requestQueue = Volley.newRequestQueue(this@MainActivity)
        requestQueue.add<String>(stringRequest)

    }

    private fun setGridView(data: MutableList<MovieContent>) {
        Log.d("Size", data.size.toString())
        val adapter = CustomAdapter(this@MainActivity, data)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = adapter
    }

    @Throws(JSONException::class)
    private fun getGenres(genre_ids: JSONArray): String? {
        val genres = StringBuilder()
        for (i in 0 until genre_ids.length()) {
            when (genre_ids.getInt(i)) {
                28 -> genres.append("Action, ")
                12 -> genres.append("Adventure, ")
                16 -> genres.append("Animation, ")
                35 -> genres.append("Comedy, ")
                80 -> genres.append("Crime, ")
                99 -> genres.append("Documentary, ")
                18 -> genres.append("Drama, ")
                10751 -> genres.append("Family, ")
                14 -> genres.append("Fantasy, ")
                36 -> genres.append("History, ")
                27 -> genres.append("Horror, ")
                10402 -> genres.append("Music, ")
                9648 -> genres.append("Mystery, ")
                10749 -> genres.append("Romance, ")
                878 -> genres.append("Science Fiction, ")
                10770 -> genres.append("TV Movie, ")
                53 -> genres.append("Thriller, ")
                10752 -> genres.append("War, ")
                37 -> genres.append("Western, ")
                else -> genres.append("Unknown, ")
            }
        }
        return genres.toString()
    }

    override fun onClick(v: View?) {
        val i: Int = recyclerView.getChildLayoutPosition(v!!)
        val movieContent: MovieContent = data[i]
        var dialog = Dialog(this@MainActivity)

        dialog.setContentView(R.layout.dialog_content)
        var movieTitle : TextView = dialog.findViewById(R.id.textViewMovieTitle)
        movieTitle.text = movieContent.title

        var textView : TextView = dialog.findViewById(R.id.ratingTextView)
        var movieRating = movieContent.rating + "/10"
        textView.text = movieRating

        var imageView: ImageView = dialog.findViewById(R.id.imageView)
        var picasso : Picasso = Picasso.get().apply {
            load("https://image.tmdb.org/t/p/w500/"+movieContent.imageURL).into(imageView)
        }

        var textGenre : TextView = dialog.findViewById(R.id.textViewDialogGenre)
        textGenre.text = movieContent.genres

        dialog.show()

        var button = dialog.findViewById<Button>(R.id.buttonDialog)
        button.setOnClickListener {
            dialog.dismiss()
        }
    }
}
