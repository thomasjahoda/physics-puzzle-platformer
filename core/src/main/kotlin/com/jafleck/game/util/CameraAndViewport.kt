package com.jafleck.game.util

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.ScreenViewport

class GameCamera : OrthographicCamera()
class GameViewport(minWorldWidth: Float, minWorldHeight: Float, camera: GameCamera) : ExtendViewport(minWorldWidth, minWorldHeight, camera)

class UiCamera : OrthographicCamera()
class UiViewport(camera: UiCamera) : ScreenViewport(camera)
