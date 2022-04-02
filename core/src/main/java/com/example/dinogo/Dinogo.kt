package com.example.dinogo

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import kotlin.random.Random
import kotlin.*
import com.badlogic.gdx.math.Rectangle

import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.graphics.Color

import com.badlogic.gdx.graphics.g2d.BitmapFont

/** [com.badlogic.gdx.ApplicationListener] implementation shared by all platforms. */
class Dinogo : ApplicationAdapter() {
    lateinit var batch: SpriteBatch
    lateinit var backGround: Texture
    lateinit var dizzy: Texture
    var width = 0f
    var height = 0f
    var manWidth = 0f
    lateinit var man: Array<Texture>
    var w = 0f
    var h = 0f
    var manState = 0
    var pause = 0
    var gravity = 0.2f
    var velocity = 0f
    var manY = 0f
    var coinXs = HashMap<Int, Float>()
    var coinYs = HashMap<Int, Float>()
    var coinRectangles = HashMap<Int, Rectangle>()

    var bombXs = HashMap<Int, Float>()
    var bombYs = HashMap<Int, Float>()
    var bombRectangles = HashMap<Int, Rectangle>()
    lateinit var bomb: Texture
    var bombCount = 0
    var numOfBombs = 0
    lateinit var coin: Texture
    var coinCount = 0
    lateinit var random: Random
    var numOfCoins = 0
    lateinit var manRectangle: Rectangle
    lateinit var font: BitmapFont
    var score = 0
    var gameState = 0
    override fun create() {
        super.create()
        batch = SpriteBatch()
        backGround = Texture("bg.png")
        width = Gdx.graphics.width.toFloat()
        height = Gdx.graphics.height.toFloat()
        w = 0.5f * width
        h = 0.5f * height

        man = arrayOf(
                Texture("frame1.png"), Texture("frame2.png"),
                Texture("frame3.png"), Texture("frame4.png")
        )
        manWidth = man[0].width.toFloat()
        manY = height
        coin = Texture("coin.png")
        bomb = Texture("bomb.png")
        random = Random(1)
        dizzy = Texture("frame5.png")
        font = BitmapFont()
        font.color = Color.WHITE
        font.data.setScale(10f)

    }//Create

    fun makeCoin() {

        val height: Float = random.nextFloat() * Gdx.graphics.height
        coinXs[numOfCoins] = (Gdx.graphics.width).toFloat()
        coinYs[numOfCoins] = height
        numOfCoins += 1
    }

    fun makeBomb() {
        val height = random.nextFloat() * Gdx.graphics.height
        bombYs[numOfBombs] = height
        bombXs[numOfBombs] = (Gdx.graphics.width).toFloat()
        numOfBombs += 1
    }

    override fun render() {
        super.render()

        batch.begin()
        batch.draw(backGround, 0f, 0f, width, height)


        if (gameState == 0) {
            if (Gdx.input.justTouched()) {
                gameState = 1
            }
        } else if (gameState == 1) { //game is alive

            // BOMB
            if (bombCount < 250) {
                bombCount++
            } else {
                bombCount = 0
                makeBomb()
            }//if
            //bombRectangles.clear()
            for (i in 0 until numOfBombs) {
                batch.draw(bomb, bombXs[i]!!, bombYs[i]!!)
                bombXs[i] = bombXs[i]!! - 8
                bombRectangles[i] =
                        Rectangle(
                                bombXs[i]!!.toFloat(), bombYs[i]!!.toFloat(),
                                bomb.width.toFloat(), bomb.height.toFloat()
                        )
            }//for

            //Coin
            if (coinCount < 100) {
                coinCount += 1
            } else {
                coinCount = 0
                makeCoin()
            }//if
            // coinRectangles.clear();
            for (i in 0 until numOfCoins) {
                batch.draw(coin, coinXs[i]!!, coinYs[i]!!)
                coinXs[i] = coinXs[i]!! - 4
                coinRectangles[i] =
                        Rectangle(
                                coinXs[i]!!.toFloat(), coinYs[i]!!.toFloat(),
                                coin.width.toFloat(), coin.height.toFloat()
                        )
            }//for
            if (Gdx.input.justTouched()) { //mouse clicked
                velocity -= 20
            }//if

            if (pause < 8) {
                pause += 1
            } else {
                pause = 0
                manState = (manState + 1) % 4
            }//else
            velocity += gravity
            manY -= velocity
            if (manY < 160) {
                manY = 160f
            }
        } else if (gameState == 2) {
            //Game Over
            if (Gdx.input.justTouched()) {
                gameState = 1
                manY = height
                score = 0
                velocity = 0f
                coinCount = 0
                bombCount = 0
                numOfBombs = 0
                numOfCoins = 0
            }//if (Gdx.input.justTouched())

        } //else if (gameState == 2) {

        if (gameState == 2) {
            batch.draw(dizzy, w - manWidth / 2, manY / 2 - 100)
        } else {
            batch.draw(man[manState], w - manWidth / 2, manY / 2 - 100)
        }
        manRectangle = Rectangle(
                w - manWidth / 2.toFloat(), manY / 2 - 100,
                man[manState].width.toFloat(),
                man[manState].height.toFloat()
        )

        for (i in 0 until numOfCoins) {
            if (Intersector.overlaps(manRectangle, coinRectangles[i])) {
                score++
                //coinRectangles[i] =  0
                coinXs[i] = -100f
                coinYs[i] = -100f
                break
            }//if (Intersector.overlaps
        }//for (i in 0 until numOfCoins) {

        for (i in 0 until numOfBombs) {
            if (Intersector.overlaps(manRectangle, bombRectangles[i])) {
                //Gdx.app.log("Bomb!", "Collision!")
                bombXs[i] = -100f
                bombYs[i] = -100f
                gameState = 2
            }// if (Intersector.o
        }// for (i in 0 until numOfBombs)

        font.draw(batch, score.toString(), 100f, 150f)

        batch.end()
    }//render

    override fun dispose() {
        super.dispose()
        batch.dispose()
    }//dispose

}//myGame
// bombXs.clear();
// bombYs.clear();
// bombRectangles.clear();