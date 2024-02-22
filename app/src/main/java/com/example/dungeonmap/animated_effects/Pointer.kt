package com.example.dungeonmap.animated_effects


import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import com.example.dungeonmap.MainViewModel
import com.example.dungeonmap.Position
import com.example.dungeonmap.plus
import com.example.dungeonmap.toPosition
import me.nikhilchaudhari.quarks.CreateParticles
import me.nikhilchaudhari.quarks.particle.Acceleration
import me.nikhilchaudhari.quarks.particle.EmissionType
import me.nikhilchaudhari.quarks.particle.Force
import me.nikhilchaudhari.quarks.particle.LifeTime
import me.nikhilchaudhari.quarks.particle.ParticleColor
import me.nikhilchaudhari.quarks.particle.ParticleSize
import me.nikhilchaudhari.quarks.particle.Velocity


@Composable
fun EffectsButton(mVM: MainViewModel)  {

    //start drawing a custom shape with the points from the pointsList
    var center: Position by remember { mutableStateOf(Position.Zero) }

    Icon(
        imageVector = Icons.Filled.StarOutline,
        contentDescription = "effects",
        tint = Color.White,
        modifier = Modifier.fillMaxSize()
        //.background(Color.White)

        .pointerInput(Unit) {
            detectDragGestures(
                onDragStart = { position -> center = position.toPosition() },
                onDragEnd = {
                    mVM.addAnimatedPointerEffect(
                    position = center,
                    duration = 10
                )
                },
                onDrag = { _, dragAmount -> center += dragAmount }
            )
        }
    )
}




@Composable
fun InvokeParticles (
    modifier: Modifier = Modifier,
    position: Position = Position.Zero,
    duration: Int = 10000
    ) {
        println("rendered position = ${position.x}, ${position.y}")
        CreateParticles(
            modifier = Modifier.fillMaxSize(),
            // Set the initial position particles (From where do you want to shoot/generate particles)
            x = position.x, y = position.y,
            // Set the velocity of particle in x and y direction
            velocity = Velocity(xDirection = 1f, yDirection = 1f),
            // Set the force acting on particle
            force = Force.Gravity(-0.2f),
            // set acceleration on both x and y direction
            acceleration = Acceleration(1f, 1f),
            // set the desired size of particle that you want
            particleSize = ParticleSize.RandomSizes(25..50),
            // set particle colors or color
            particleColor = ParticleColor.RandomColors(listOf(Color.White, Color.Red, Color.Red, Color.Magenta)),
            // set the max lifetime and aging factor of a particle
            lifeTime = LifeTime(25f, 0.6f),
            // set the emission type - how do you want to generate particle - as a flow/stream, as a explosion/blast
            emissionType = EmissionType.FlowEmission(maxParticlesCount = 500, emissionRate = 0.5F ),
            // duration of animation
            durationMillis = duration
        )
}

@Preview
@Composable
fun AnimatedPointerPreview() {
    InvokeParticles(Modifier, Position(500F, 1000F))
}