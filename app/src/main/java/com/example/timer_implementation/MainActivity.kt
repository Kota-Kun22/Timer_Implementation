package com.example.timer_implementation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(color=Color(0xFF101010),
                modifier = Modifier.fillMaxSize()) {

                Box(contentAlignment = Alignment.Center)
                {
                    Timer(totalTime = 100L*1000L,//1 sec =1000 milisec
                        handleColor = Color.Green,
                        inactiveBarColor =Color.DarkGray ,
                        activeBarColor = Color(0xFF37B900),
                        modifier= Modifier.size(200.dp)
                    )

                }

            }

        }
    }
}
@Composable
fun Timer(
    totalTime: Long,
    handleColor: Color,
    inactiveBarColor: Color,
    activeBarColor: Color,
    modifier: Modifier = Modifier,
    strokeWidth: Dp =5.dp,
    initialValue: Float = 1f)
{
    var size by remember { mutableStateOf(IntSize.Zero) }//size for the whole composable


    var value by remember{ mutableStateOf(initialValue) }
     /*value for the percentage value like 70% and initial value is because
    if we wanted to start the timer from 50 onlu that's why we have use initial value */


    var currentTime by remember { mutableStateOf(totalTime) }// for the current timer we need the state because it is changing


    var istimerRunning by remember { mutableStateOf(false)
    }//

    LaunchedEffect(key1 = currentTime,key2=istimerRunning){
        if(currentTime>0 && istimerRunning){
            delay(10L)
            currentTime -= 100L
            value= currentTime/totalTime.toFloat()
        }
    }

    Box(contentAlignment = Alignment.Center,
        modifier = modifier.onSizeChanged { size= it }
       )
    {//now drawing the arc and handle here
        Canvas(modifier = modifier)
        {
            //inActive arc
            drawArc(color= inactiveBarColor,
                startAngle = -215f,
                sweepAngle = 250f,
                useCenter = false,/*this false prevent our end to* match at the center*/
                size= Size(size.width.toFloat(),size.height.toFloat()),
                style= Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)/* this cap tells that the ends will be rounded*/
            )
            //active Arc
            drawArc(color= activeBarColor,
                startAngle = -215f,
                sweepAngle = 250f *value,/* here our sweepAngle will be changed based on the time  */
                useCenter = false,/*this false prevent our end to* match at the center*/
                size= Size(size.width.toFloat(),size.height.toFloat()),
                style= Stroke(
                    strokeWidth.toPx(),
                    cap = StrokeCap.Round/* this cap tells that the ends will be rounded*/
                )
            )
            /* now for the  handle the big dot ,that will be set for that we will be needing the
            *  center of the circel (canvas) the angle the x width and y height */

            val center = Offset(size.height/2f,size.width/2f)
            val beta= (250f* value+145f)*(PI/180f).toFloat()
            //from this we get the angle between the center and the handle from which we can get the perpendicular and base
            val r= size.width/2f
            val a= cos(beta)*r
            val b = sin(beta)*r

            drawPoints(
                listOf(Offset(center.x+a,center.y+b)),//error
                pointMode= PointMode.Points,
                color= handleColor,
                strokeWidth=(strokeWidth*3f).toPx(),
                cap=StrokeCap.Round,
            )
        }
        Text(text=(currentTime/1000L).toString(),
            fontSize=44.sp,
            fontWeight = FontWeight.Bold,
            color=Color.White
        )
        Button(onClick = {

                         if(currentTime<=0L){
                             currentTime= totalTime
                             istimerRunning= true
                         } else{
                             istimerRunning= !istimerRunning
                         }
                         },
            modifier=Modifier.align(Alignment.BottomCenter),
            colors = ButtonDefaults.buttonColors(
                containerColor =
               if(! istimerRunning||currentTime<=0L)
               {
                   Color.Green
               }
               else
               {
                   Color.Red
               }
            )
        )
        {
            Text(
                text = if(istimerRunning&&currentTime>=0L) "Stop"
                else if (!istimerRunning&&currentTime>=0L)"Start"
                else "Restart"
            )
        }

    }


}