package com.example.dinogo

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Circle
import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.MathUtils.random
import com.badlogic.gdx.math.Rectangle
import java.util.*

/** [com.badlogic.gdx.ApplicationListener] implementation shared by all platforms. */
class Dinogo : ApplicationAdapter() {

    lateinit var batch: SpriteBatch
    lateinit var background: Texture
    lateinit var birds: Array<Texture>
    lateinit  var gameover: Texture


    var screenWidth = 0
    var screenHeight = 0
    var birdCenterX = 0
    var birdY = 0
    var whichBird = 0
    var gameState = 0
    var velocity = 0
    var gravity = 0

    var bombXs = HashMap<Int, Float>()
    var bombYs = HashMap<Int, Float>()
    lateinit var bomb: Texture
    var bombCount = 0
    var numOfBombs = 0


    lateinit  var randomGenerator: Random
    var bombRectangles = HashMap<Int, Rectangle>()
    lateinit var shapeRenderer: ShapeRenderer
    lateinit  var birdCircle: Circle
    var score = 0
    var tubePassed = 0
    var font: BitmapFont? = null

    override fun create() {
        batch = SpriteBatch()
        background = Texture("bg.png")
        gameover = Texture("gameover.jpg")
        bomb = Texture("bomb.png")
        screenHeight = Gdx.graphics.height
        screenWidth = Gdx.graphics.width
        birds = arrayOf(Texture("frame1.png"), Texture("frame2.png"), Texture("frame5.png"))
        birdCenterX = 1
        birdY = 1
        randomGenerator = Random()


        birdCircle = Circle()
        // shapeRenderer = ShapeRenderer();

        font = BitmapFont()
        font!!.color = Color.GOLD
        font!!.data.setScale(15f)
        initialize()
    }//create

    fun initialize() {

        whichBird = 0
        velocity = 0
        gravity = 1
        gameState = 0
        birdCenterX = 1
        birdY = 1
        bombXs.clear()
        bombYs.clear()
        bombRectangles.clear()
        numOfBombs = 0

        birdCircle = Circle()
        shapeRenderer = ShapeRenderer();
        tubePassed = 0
        score = 0

    }//initialize()

    fun drawBirds() {
        batch.draw(birds[whichBird], birdCenterX.toFloat(), birdY.toFloat())
        if(gameState == 1) whichBird = 1 - whichBird
        else if(gameState == 2) whichBird = 2
        else if(gameState == 0) whichBird = 0

    } //drawBirds
    fun makeBomb() {
        val height = 20
        bombYs[numOfBombs] = height.toFloat()
        bombXs[numOfBombs] = (Gdx.graphics.width).toFloat()
        numOfBombs += 1
    }

    override fun render() {
        batch.begin()
        batch.draw(background, 0f, 0f, screenWidth.toFloat(), screenHeight.toFloat())
        if (gameState == 1) {
            if (bombCount < 250) {
                bombCount++
            } else {
                bombCount = 0
                makeBomb()
            }//if
            for (i in 0 until numOfBombs) {
                batch.draw(bomb, bombXs[i]!!, bombYs[i]!!)
                bombXs[i] = bombXs[i]!! - 17
                bombRectangles[i] =
                        Rectangle(
                                bombXs[i]!!.toFloat(), bombYs[i]!!.toFloat(),
                                bomb.width.toFloat(), bomb.height.toFloat()
                        )
            }//for
            if (Gdx.input.justTouched()) {
                if(birdY == 0)
                {
                    birdY = 10
                    velocity = -30
                }

            } //if (Gdx.input

            System.out.println(birdY)
            if (birdY > 0) {
                velocity = velocity + gravity
                birdY = birdY - velocity
            } //if (birdY
            else {
                birdY = 0
            }
        } else if (gameState == 0) {
            if (Gdx.input.justTouched()) {
                gameState = 1
            }
        } else if (gameState == 2) {
            batch.draw(gameover, (screenWidth / 2 - gameover.width / 2).toFloat(), (screenHeight / 2).toFloat()
            )
            if (Gdx.input.justTouched()) {
                gameState = 1
                initialize()
            }
        } //endif
        drawBirds()
        for (i in 0 until numOfBombs) {
            if (Intersector.overlaps(birdCircle, bombRectangles[i])) {
                //Gdx.app.log("Bomb!", "Collision!")
                bombXs[i] = -100f
                bombYs[i] = -100f
                gameState = 2
            }// if (Intersector.o
        }// for (i in 0 until numOfBombs)
        font!!.draw(batch, "" + score, 100f, screenHeight.toFloat())
        batch.end()
        //shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        // shapeRenderer.setColor(Color.BLUE);
        birdCircle.set(0F, birdY + (birds[0].height).toFloat(), (birds[0].width / 2).toFloat())
        //shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);

        // shapeRenderer.end();
    } //render

    override fun dispose() {
        batch.dispose()
    } //dispose()

}//END
