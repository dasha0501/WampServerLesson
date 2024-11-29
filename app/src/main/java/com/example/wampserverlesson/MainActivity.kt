package com.example.wampserverlesson

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wampserverlesson.ui.theme.WampServerLessonTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    //private lateinit var recyclerView: RecyclerView
    //private lateinit var adapter: WampAdapter
    @Inject
    lateinit var mainApi: MainApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            /*
        recyclerView = findViewById(R.id.rcView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = WampAdapter()
        recyclerView.adapter = adapter*/
        setContent{
            MainScreen()
        }



    }
}


@Composable
fun withXML(){
    AndroidView(factory = { View.inflate(it, R.layout.main, null)
    },
        modifier = Modifier.fillMaxSize(),
       )
}

@Composable
fun MainScreen(viewModel: MainViewModel = hiltViewModel()){
    val context = LocalContext.current
    val name = remember {
        mutableStateOf("")
    }
    val age = remember {
        mutableStateOf("")
    }
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween){
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.7f)



        ) {
            items(viewModel.userList.value){
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)){
                    Text(text = it.name, modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth()
                        .padding(10.dp))
                }
            }
        }
        Column {
            TextField(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
                value = name.value,
                onValueChange = {
                name.value = it
            })
            Spacer(modifier = Modifier.height(5.dp))
            TextField(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
                value = age.value,
                onValueChange = {
                age.value = it
            })
            Spacer(modifier = Modifier.height(5.dp))
            Button(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
                onClick = {
                    viewModel.saveUser(User(
                       name.value,
                        0,
                        System.currentTimeMillis().toString(),
                        age.value
                    ))
                    /*
                    viewModel.uploadImage(
                        ImageData(
                            bitmapToBase64(context),
                            "test.jpg"
                        )
                    )*/

            }) {
                Text( text = "Save user")
            }
            Spacer(modifier = Modifier.height(5.dp))
        }


    }
}

private fun bitmapToBase64(context: Context) : String{
    val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.cat)
    val byteOutputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteOutputStream)
    val byteArray = byteOutputStream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}
