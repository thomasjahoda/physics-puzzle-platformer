package com.jafleck.game.util

import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.input.GestureDetector

class GameInputMultiplexer : InputMultiplexer()
class UiInputMultiplexer : InputMultiplexer()

class BasicGameGestureDetector(listener: GestureListener) : GestureDetector(listener)
