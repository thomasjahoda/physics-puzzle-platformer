package com.jafleck.game.gameplay.ui

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.jafleck.extensions.libgdx.scenes.scene2d.select
import com.jafleck.extensions.libgdx.scenes.scene2d.selectBoxOfSimpleElements
import com.jafleck.game.maploading.GameMapList
import com.jafleck.game.maploading.MapSelection
import ktx.actors.onChange
import ktx.actors.onClick
import ktx.graphics.copy
import ktx.scene2d.*

class MenuRow(
    private val gameMapList: GameMapList,
    private val mapSelection: MapSelection,
    private val transparentColorBackgroundImageFactory: TransparentColorBackgroundImageFactory
) {

    val content: Actor
        get() = contentStack
    private val contentStack = KStack()

    private var menuOpen = false
    private val contentTable: KTableWidget by lazy {
        table {
            top()
            menuButton = textButton("Menu") {
                onClick {
                    if (menuOpen) {
                        closeMenu()
                    } else {
                        showMapList()
                    }
                }
            }
            row()
            add(openMenuContainer).expandX().fillX()
            row()
            add(KContainer<Actor>()).expand()
        }
    }
    private lateinit var menuButton: Button

    private val background = transparentColorBackgroundImageFactory.createColorBackground(
        Color.GRAY.copy(alpha = 0.2f)
    ).apply {
        onClick { closeMenu() }
    }

    private val openMenuContainer = container {
        fill()
    }

    private val mapListTable by lazy {
        table {
            label("Map:")
            add(selectBoxOfSimpleElements(gameMapList.maps, { it.name }) {
                select(mapSelection.selectedMap)
                mapSelection.selectedMapListeners.addNewValueListener { select(it) }
                onChange {
                    val selected = selected
                    if (selected != null && selected.element != mapSelection.selectedMap) {
                        mapSelection.selectedMap = selected.element
                    }
                }
            })
        }
    }

    init {
        closeMenu()
    }

    private fun showMapList() {
        openMenuContainer.actor = mapListTable
        contentStack.clear()
        contentStack.add(background)
        contentStack.add(contentTable)
        menuOpen = true
    }

    private fun closeMenu() {
        openMenuContainer.actor = null
        contentStack.clear()
        contentStack.add(contentTable)
        menuOpen = false
    }
}
