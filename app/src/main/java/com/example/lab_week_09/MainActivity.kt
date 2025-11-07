package com.example.lab_week_09

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.room.util.copy
import com.example.lab_week_09.ui.theme.LAB_WEEK_09Theme
import com.example.lab_week_09.ui.theme.MoshiAdapter
import com.example.lab_week_09.ui.theme.OnBackgroundItemText
import com.example.lab_week_09.ui.theme.OnBackgroundTitleText
import com.example.lab_week_09.ui.theme.PrimaryTextButton
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

//Previously we extend AppCompatActivity,
//now we extend ComponentActivity
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        //Here, we use setContent instead of setContentView
        setContent {
            //Here, we wrap our content with the theme
            //You can check out the LAB_WEEK_09Theme inside Theme.kt
            LAB_WEEK_09Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    //We use Modifier.fillMaxSize() to make the surface fill the whole screen
                    modifier = Modifier.fillMaxSize(),
                    //We use MaterialTheme.colorScheme.background to get the background color
                    //and set it as the color of the surface
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    App(
                        navController = navController
                    )
                }
            }
        }
    }
}

//Declare a data class called Student
data class Student(
    var name: String
)


//Here, instead of defining it in an XML file,
//we create a composable function called Home
//@Preview is used to show a preview of the composable
@Preview(showBackground = true)

@Composable
fun Home(
    navigateFromHomeToResult: (List<Student>) -> Unit
) {
    //Here, we create a mutable state list of Student
    //We use remember to make the list remember its value
    //This is so that the list won't be recreated when the composable recomposes
    //We use mutableStateListOf to make the list mutable
    //This is so that we can add or remove items from the list
    //If you're still confused, this is basically the same concept as using
    //useState in React
    val listData = remember { mutableStateListOf(
        Student("Tanu"),
        Student("Tina"),
        Student("Tono")
    )}
    //Here, we create a mutable state of Student
    //This is so that we can get the value of the input field
    val inputField = remember { mutableStateOf(Student("")) }

    // Logika memeriksa input kosong atau tidak
    val isInputValid = inputField.value.name.isNotBlank()

    //We call the HomeContent composable
    //Here, we pass:
    //listData to show the list of items inside HomeContent
    //inputField to show the input field value inside HomeContent
    //A lambda function to update the value of the inputField
    //A lambda function to add the inputField to the listData
    HomeContent(
        listData,
        inputField.value,
        { input -> inputField.value = inputField.value.copy(input) },
        {
            if (isInputValid){
                listData.add(inputField.value)
                inputField.value = Student("")
            }
        },
        { navigateFromHomeToResult(listData.toList()) }
    )
}

//@Composable is used to tell the compiler that this is a composable function
//It's a way of defining a composable
@Composable
fun HomeContent(
    //Here, we define a parameter called items
    listData: SnapshotStateList<Student>,
    inputField: Student,
    onInputValueChange: (String) -> Unit,
    onButtonClick: () -> Unit,
    navigateFromHomeToResult: () -> Unit
    ) {
        //Here, we use LazyColumn to lazily display a list of items horizontally
        //LazyColumn is more efficient than Column
        //because it only composes and lays out the currently visible items
        //much like a RecyclerView
        //You can also use LazyRow to lazily display a list of items horizontally
        LazyColumn {
            //Here, we use item to display an item inside the LazyColumn
            item {
                //Here, we use Column to display a list of items vertically
                //You can also use Row to display a list of items horizontally
                Column (
                    //Modifier.padding(16.dp) is used to add padding to the Column
                    //You can also use Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    //to add padding horizontally and vertically
                    //or Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 8.dp)
                    //to add padding to each side
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize(),
                    //Alignment.CenterHorizontally is used to align the Column horizontally
                    //You can also use verticalArrangement = Arrangement.Center to align the Column vertically
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    //Here, we call the OnBackgroundTitleText UI Element
                    OnBackgroundTitleText(text = stringResource(
                        id = R.string.enter_item)
                    )
                    //Here, we use TextField to display a text input field
                    TextField(
                        //Set the value of the input field
                        value = inputField.name,
                        //Set the keyboard type of the input field
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text
                        ),
                        //Set what happens when the value of the input field changes
                        onValueChange = {
                            //Here, we call the onInputValueChange lambda function
                            //and pass the value of the input field as a parameter
                            //This is so that we can update the value of the inputField
                            onInputValueChange(it)
                        }
                    )
                    //Here, we call the PrimaryTextButton UI Element
                    Row{
                        PrimaryTextButton(text = stringResource(
                            id = R.string.button_click)
                        ) {
                            onButtonClick()
                        }
                        PrimaryTextButton(text = stringResource(id =  R.string.button_navigate)) {
                            navigateFromHomeToResult()
                        }
                    }
                }
            }
            //Here, we use items to display a list of items inside the LazyColumn
            //This is the RecyclerView replacement
            items(listData) { item ->
                Column(
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    //Here, we call the OnBackgroundItemText UI Element
                    OnBackgroundItemText(text = item.name)
                }
            }
        }
    }

//Here, we create a preview function of the Home composable
//This function is specifically used to show a preview of the Home composable
//This is only for development purpose
//@Preview(showBackground = true)
/*@Composable
fun PreviewHome() {
    Home()
}*/

//Here, we create a composable function called App
//This will be the root composable of the app
@Composable
fun App(navController: NavHostController) {
    //Here, we use NavHost to create a navigation graph
    //We pass the navController as a parameter
    //We also set the startDestination to "home"
    //This means that the app will start with the Home composable
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        //Here, we create a route called "home"
        //We pass the Home composable as a parameter
        //This means that when the app navigates to "home",
        //the Home composable will be displayed
        composable("home") {
            //Here, we pass a lambda function that navigates to "resultContent"
            //and pass the listData as a parameter
            Home { listData ->
                val listType = Types.newParameterizedType(List::class.java, Student::class.java)
                val jsonAdapter = MoshiAdapter.moshi.adapter<List<Student>>(listType)

                // 2. Ubah list menjadi string JSON
                val jsonString = jsonAdapter.toJson(listData)

                navController.navigate("resultContent/?listData=$jsonString")
            }
        }
        //Here, we create a route called "resultContent"
        //We pass the ResultContent composable as a parameter
        //This means that when the app navigates to "resultContent",
        //the ResultContent composable will be displayed
        //You can also define arguments for the route
        //Here, we define a String argument called "listData"
        //We use navArgument to define the argument
        //We use NavType.StringType to define the type of the argument
        composable(
            "resultContent/?listData={listData}",
            arguments = listOf(navArgument("listData") {
                type = NavType.StringType }
            )
        ) { backStackEntry ->
            val jsonString = backStackEntry.arguments?.getString("listData").orEmpty()
            //Here, we pass the value of the argument to the ResultContent composable
            ResultContent(
                listData = jsonString
            )
        }
    }
}

//Here, we create a composable function called ResultContent
//ResultContent accepts a String parameter called listData from the Home composable
//then displays the value of listData to the screen
@Composable
fun ResultContent(listData: String) {
    // 1. Buat adapter untuk mengubah JSON kembali menjadi List<Student>
    val listType = Types.newParameterizedType(List::class.java, Student::class.java)
    val jsonAdapter: JsonAdapter<List<Student>> = MoshiAdapter.moshi.adapter(listType)

    // 2. Parse string JSON. Gunakan "remember" agar tidak di-parse ulang setiap recomposition.
    // Jika jsonString kosong, berikan list kosong.
    val studentList =
        if (listData.isNotEmpty()) {
            jsonAdapter.fromJson(listData)
        } else {
          emptyList()
        }

    // Display the list of students
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OnBackgroundTitleText(text = "Result Page")
        // Use LazyColumn to display the list of student names
        LazyColumn(modifier = Modifier.padding(top = 16.dp)) {
            items(studentList.orEmpty()) { student ->
                OnBackgroundItemText(text = student.name)
            }
        }
    }
}