package com.jafleck.game.util.files

import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver

interface AssetsFileHandleResolver : FileHandleResolver
class DefaultAssetsFileHandleResolver : InternalFileHandleResolver(), AssetsFileHandleResolver
