package com.example.tipappjet

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tipappjet.components.InputField
import com.example.tipappjet.roundbuttons.RoundButton
import com.example.tipappjet.ui.theme.TipAppJetTheme

@ExperimentalComposeUiApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {
                Text(text = "hello", color = Color.DarkGray)
            }
        }
    }
}

@ExperimentalComposeUiApi
@Composable
fun MyApp(content: @Composable () -> Unit){
    TipAppJetTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            // hardcoded color for bg
            color = Color.White
        ) {
            Column (modifier = Modifier.padding(top = 15.dp)){
                MidCard()
            }

        }
    }
}


//@Preview
@Composable
fun TopCard(totalPerPerson: Double){
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(8.dp)
            .clip(shape = RoundedCornerShape(corner = CornerSize(15.dp))),
        color = colorResource(id = R.color.peach)

    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier

        ) {
            Text(
                text = "TOTAL PER PERSON:",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.navy_blue),
                modifier = Modifier
                    .padding(bottom = 8.dp)
            )
            Text(
                text = String.format("%.2f", totalPerPerson),
                fontSize = 25.sp,
                color = colorResource(id = R.color.navy_blue),
                fontWeight = FontWeight.ExtraBold
            )
        }
    }

}


@Composable
fun MidCard(){
    val tipAmountState = remember {
        mutableStateOf(0.0)
    }
    val peopleCounter = remember {
        mutableStateOf(1)
    }
    val totalPerPerson = remember {
        mutableStateOf(0.0)
    }
    val range = IntRange(start = 1, endInclusive = 100)
    BillForm(
        range = range,
        peopleCounter = peopleCounter,
        tipAmountState = tipAmountState,
        totalPerPerson = totalPerPerson){billAmt ->
            Log.d("AMT", billAmt)

    }


}

@OptIn(ExperimentalComposeUiApi::class)
@Preview
@Composable
fun MyAppPreview(){
    TipAppJetTheme {
        MyApp {

        }
    }

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BillForm(
    modifier: Modifier = Modifier,
    range: IntRange = 1..100,
    peopleCounter : MutableState<Int>,
    tipAmountState: MutableState<Double>,
    totalPerPerson: MutableState<Double>,
    onValChange: (String) -> Unit = {}


    ) {

    val totalBillState = remember {
        mutableStateOf("")
    }

    // create a valid state of the text field, pass the value to check
    val validState = remember(totalBillState.value) {
        // check the value
        totalBillState.value.trim().isNotEmpty()
    }

    val keyoboardController = LocalSoftwareKeyboardController.current
    val sliderPositionState = remember {
        mutableStateOf(0f)
    }
    val moneyIcon = ImageVector.vectorResource(R.drawable.ic_dollar)
    val tint = colorResource(id = R.color.navy_blue)
    val bgColorBtn = colorResource(id = R.color.peach)

    TopCard(totalPerPerson.value)
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        border = BorderStroke(
            width = 3.dp,
            color = colorResource(id = R.color.navy_blue)
        ),
        shape = RoundedCornerShape(16.dp),
        color = Color.White
    ) {
        // main Col
        Column(

        ) {
            // insert text field
            InputField(
                valueState = totalBillState,
                labelId = "Enter Bill",
                isSingleLine = true,
                enabled = true,
                onAction = KeyboardActions {
                    // check if "something" is valid
                    if (!validState) return@KeyboardActions
                    //  - onvaluechanged
                    onValChange(totalBillState.value.trim())

                    keyoboardController?.hide()
                },
                leadingIcon = moneyIcon,
            )

            if (validState) {
                // Row for Split and -/+
                Row(
                    modifier = Modifier
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.Start,
                ) {
                    // text for "Split"
                    Text(
                        text = "Split:",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.navy_blue),
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 12.dp, end = 60.dp)
                    )
                    Spacer(modifier = Modifier.width(60.dp))

                    RoundButton(
                        icon = ImageVector.vectorResource(R.drawable.ic_add),
                        //Log.d(  "RoundBtn",  "Plus")
                        onClick = {
                            peopleCounter.value += 1
                            totalPerPerson.value = calculateTotalPerPerson(
                                totalBillState.value.toDouble(),
                                sliderPositionState.value.toInt(),
                                peopleCounter.value
                            )
                        },
                        tint = tint,
                        backgroundColor = bgColorBtn
                    )
                    Text(
                        text = "${peopleCounter.value}",
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.navy_blue),
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 9.dp, end = 9.dp)
                    )
                    RoundButton(
                        icon = ImageVector.vectorResource(R.drawable.ic_minus),
                        onClick = {
                            if (peopleCounter.value > 1) peopleCounter.value -= 1
                            totalPerPerson.value = calculateTotalPerPerson(
                                totalBillState.value.toDouble(),
                                sliderPositionState.value.toInt(),
                                peopleCounter.value
                            )
                        },
                        tint = tint,
                        backgroundColor = bgColorBtn
                    )


                }

                // Row for Tip and $ value
                Row(modifier = Modifier.padding(4.dp)) {
                    // text for "Tip"
                    Text(
                        text = "Tip:",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.navy_blue),
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 12.dp, end = 60.dp, top = 15.dp)
                    )

                    Text(
                        text = "$${tipAmountState.value}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.navy_blue),
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 95.dp, top = 15.dp)
                    )
                }

                // Column for %
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 25.dp)

                ) {
                    Text(
                        text = "${sliderPositionState.value.toInt()}%",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = colorResource(id = R.color.navy_blue),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.height(14.dp))

//                   insert  Slider
                    Slider(
                        value = sliderPositionState.value,
                        onValueChange = { newValue ->
                            sliderPositionState.value = newValue
                            //                            Log.d("slider", "$newVal")
                            tipAmountState.value = calculateTip(
                                totalBillState.value.toDouble(),
                                sliderPositionState.value.toInt()
                            )
                            totalPerPerson.value = calculateTotalPerPerson(
                                totalBillState.value.toDouble(),
                                sliderPositionState.value.toInt(),
                                peopleCounter.value
                            )
                        },
                        valueRange = 0f..100f,
                        steps = 100,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )


                }

                // insert Bar

            } else {
                Box() {}
            }

        }


    }
}



fun calculateTip(totalBill: Double, tipPercentage: Int) : Double{
    return if(totalBill > 1 && totalBill.toString().isNotEmpty())
        (totalBill * tipPercentage) / 100 else 0.0
}

fun calculateTotalPerPerson(totalBill: Double, tipPercentage: Int, persontCount : Int): Double{
    val finalBill = calculateTip(totalBill, tipPercentage) + totalBill
    return finalBill/persontCount
}



