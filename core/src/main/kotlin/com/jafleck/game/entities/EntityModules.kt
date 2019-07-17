package com.jafleck.game.entities

import com.jafleck.game.entities.creatorutil.entityCreatorUtilModule
import com.jafleck.game.entities.maploading.entityMaploadingModule

val entityModules = listOf(
    playerModule,
    platformModule,
    waterModule,
    thrownBallModule,
    ropeModule,
    visualDebugMarkerModule,
    entityMaploadingModule,
    entityCreatorUtilModule
)
