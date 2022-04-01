package com.example.dinogo

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class Dinogo : ApplicationAdapter(){
    lateinit var batch: SpriteBatch
    lateinit var image : Texture
    override fun create() {
        batch = SpriteBatch()
        image = Texture("libgdx.png")
    } //create

    override fun render() {
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.2f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        batch.begin()
        batch.draw(image, 140f, 210f)
        batch.end()
    } //render

    override fun dispose() {
        batch.dispose()
    } //dispose()
} //END