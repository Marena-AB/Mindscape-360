package edu.moravian.mindscape360

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.jetbrains.compose.resources.painterResource
import vrapp.composeapp.generated.resources.Res
import vrapp.composeapp.generated.resources.arrow_back

@Composable
fun ActualScreen(navController: NavController) {
    FloatingActionButton(onClick = {navController.popBackStack() },
        modifier = Modifier
            .padding(top = 35.dp, start = 10.dp),
        containerColor = Color(0xFF4F7942),
    ) {
        Icon(
            painter = painterResource(Res.drawable.arrow_back),
            contentDescription = "Back",
            modifier = Modifier.padding(8.dp),
            tint = Color.White,
            )
    }
}