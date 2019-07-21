package com.jafleck.game.gameplay.ui

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.jafleck.extensions.libgdx.scenes.scene2d.select
import com.jafleck.extensions.libgdx.scenes.scene2d.selectBoxOfSimpleElements
import com.jafleck.game.maploading.GameMapList
import com.jafleck.game.maploading.MapSelection
import ktx.actors.onChange
import ktx.actors.onClick
import ktx.scene2d.KTableWidget
import ktx.scene2d.container
import ktx.scene2d.table
import ktx.scene2d.textButton

class MenuRow(
    private val gameMapList: GameMapList,
    private val mapSelection: MapSelection
) {

    val content: Actor
        get() = contentTable
    private val contentTable: KTableWidget by lazy {
        table {
            menuButton = textButton("Menu") {
                var menuOpen = false
                onClick {
                    if (menuOpen) {
                        openMenuContainer.actor = null
                    } else {
                        showMapList()
                    }
                    menuOpen = !menuOpen
                }
            }
            row()
            add(openMenuContainer).expandX().fillX()
        }
    }
    private lateinit var menuButton: Button
    private val openMenuContainer = container {
        fill()
    }
    private val mapListTable by lazy {
        table {
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

    private fun showMapList() {
        openMenuContainer.actor = mapListTable
    }
}
