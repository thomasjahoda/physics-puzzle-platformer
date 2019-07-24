package com.jafleck.game.entities

import com.jafleck.game.entities.creatorutil.entityCreatorUtilModule
import com.jafleck.game.entities.maploading.entityMaploadingModule

val entityModules = listOf(
    playerModule,
    platformModule,
    waterModule,
    goalZoneModule,
    deathZoneModule,
    gadgetPickupModule,
    thrownBallModule,
    ropeModule,
    visualDebugMarkerModule,
    entityMaploadingModule,
    entityCreatorUtilModule
)
