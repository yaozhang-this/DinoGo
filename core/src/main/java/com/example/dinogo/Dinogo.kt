package com.example.dinogo

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import kotlin.random.Random

class Dinogo : ApplicationAdapter(){
    lateinit var batch: SpriteBatch
    lateinit var image : Texture
    //initialize bingo array
    val from = 1
    val to = 60
    val random = Random
    var bingoNumbers = IntArray(24){random.nextInt(to-from) + from}
    var gameWon = false
    var gameState = arrayOf(2,2,2,2,2,2,2,2,2,2,2,2,0,2,2,2,2,2,2,2,2,2,2,2,2)
    var winningPositions = arrayOf(arrayOf(0,1,2,3,4),arrayOf(5,6,7,8,9), arrayOf(10,11,12,13,14),
        arrayOf(15,16,17,18,19), arrayOf(20,21,22,23,24), arrayOf(0,5,10,15,20), arrayOf(1,6,11,16,21),
        arrayOf(2,7,12,17,22), arrayOf(3,8,13,18,23), arrayOf(4,9,14,19,24), arrayOf(0,6,12,18,24),
        arrayOf(4,8,12,16,20))
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

    fun isWin(){
        for (win in winningPositions){
            var sum = gameState[win[0]] + gameState[win[1]] + gameState[win[2]] + gameState[win[3]] + gameState[win[4]]
            if (sum == 0){
                gameWon = true
                return
            }
        }
    }//isWin
} //END