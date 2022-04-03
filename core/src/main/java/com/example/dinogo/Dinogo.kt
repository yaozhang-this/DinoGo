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
import com.badlogic.gdx.math.Rectangle
import kotlin.random.Random
import java.util.*
import kotlin.collections.HashMap

/** [com.badlogic.gdx.ApplicationListener] implementation shared by all platforms. */
class Dinogo : ApplicationAdapter() {

    lateinit var batch: SpriteBatch
    lateinit var background: Texture
    lateinit var dinos: Array<Texture>
    lateinit  var gameover: Texture


    var screenWidth = 0
    var screenHeight = 0
    var dinoCenterX = 0
    var dinoY = 0
    var whichDino = 0
    var gameState = 0
    var velocity = 0
    var gravity = 0
    var birdXs = HashMap<Int, Float>()
    var birdYs = HashMap<Int, Float>()
    lateinit var bird: Texture
    var birdCount = 0
    var numOfBirds = 0
    var bingoRectangles = HashMap<Int, Rectangle>()
    var birdRectangles = HashMap<Int, Rectangle>()
    lateinit var shapeRenderer: ShapeRenderer
    lateinit  var dinoCircle: Circle
    var score = 0
    var startTime = 0
    var tubePassed = 0
    var font: BitmapFont? = null
    var bingoObj : BitmapFont? = null
    val from = 1
    val to = 60
    var bingoX = HashMap<Int, Float>()
    var bingoStore = HashMap<Int, Int>()
    val random = Random
    var bingoCount = 0
    var numOfBingo = 0
    var bingoNumbers = (1..60).shuffled().take(25)
    var gameWon = false
    var bingoState = arrayOf(2,2,2,2,2,2,2,2,2,2,2,2,0,2,2,2,2,2,2,2,2,2,2,2,2)
    var winningPositions = arrayOf(arrayOf(0,1,2,3,4),arrayOf(5,6,7,8,9), arrayOf(10,11,12,13,14),
        arrayOf(15,16,17,18,19), arrayOf(20,21,22,23,24), arrayOf(0,5,10,15,20), arrayOf(1,6,11,16,21),
        arrayOf(2,7,12,17,22), arrayOf(3,8,13,18,23), arrayOf(4,9,14,19,24), arrayOf(0,6,12,18,24),
        arrayOf(4,8,12,16,20))

    fun isWin(){
        for (win in winningPositions){
            var sum = bingoState[win[0]] + bingoState[win[1]] + bingoState[win[2]] + bingoState[win[3]] + bingoState[win[4]]
            if (sum == 0){
                gameState = 3
                return
            }
        }
    }//isWin
    override fun create() {
        batch = SpriteBatch()
        background = Texture("bg.png")
        gameover = Texture("gameover.jpg")
        bird = Texture("bird.png")
        screenHeight = Gdx.graphics.height
        screenWidth = Gdx.graphics.width
        dinos = arrayOf(Texture("frame1.png"), Texture("frame2.png"), Texture("frame5.png"))
        dinoCenterX = 1
        dinoY = 1



        dinoCircle = Circle()
        // shapeRenderer = ShapeRenderer();

        font = BitmapFont()
        bingoObj = BitmapFont()
        bingoObj!!.color = Color.WHITE
        bingoObj!!.data.setScale(15f)
        font!!.color = Color.GOLD
        font!!.data.setScale(15f)
        initialize()
    }//create
    fun chooseNumber(): Int {
        var rando = random.nextInt(0,24)
        while (bingoState[rando] != 2){
            rando = random.nextInt(0,24)
        }
        return rando
    }
    fun initialize() {

        whichDino = 0
        velocity = 0
        gravity = 1
        gameState = 0
        dinoCenterX = 1
        dinoY = 1
        birdXs.clear()
        birdYs.clear()
        bingoX.clear()
        bingoStore.clear()
        bingoRectangles.clear()
        birdRectangles.clear()
        numOfBirds = 0
        numOfBingo = 0
        bingoCount = 0
        dinoCircle = Circle()
        shapeRenderer = ShapeRenderer();
        tubePassed = 0
        score = 0
        bingoState = arrayOf(2,2,2,2,2,2,2,2,2,2,2,2,0,2,2,2,2,2,2,2,2,2,2,2,2)
        bingoNumbers = (1..60).shuffled().take(25)

    }//initialize()

    fun drawdinos() {
        batch.draw(dinos[whichDino], dinoCenterX.toFloat(), dinoY.toFloat())
        if(gameState == 1) whichDino = 1 - whichDino
        else if(gameState == 2) whichDino = 2
        else if(gameState == 0) whichDino = 0

    } //drawdinos
    fun makeBird() {
        val height = 20
        birdYs[numOfBirds] = height.toFloat()
        birdXs[numOfBirds] = (Gdx.graphics.width).toFloat()

        numOfBirds += 1
    }
    fun makeBingo()
    {
        bingoX[numOfBingo] = (Gdx.graphics.width).toFloat()
        bingoStore[numOfBingo] = bingoNumbers[chooseNumber()]
        numOfBingo += 1
    }

    override fun render() {
        batch.begin()
        batch.draw(background, 0f, 0f, screenWidth.toFloat(), screenHeight.toFloat())
        if (gameState == 1) {
            score = (System.currentTimeMillis()/ 1000).toInt() - startTime
            if (birdCount < 250) {
                birdCount++
            } else {
                birdCount = 0
                makeBird()
            }//if
            if (bingoCount < 120)
            {
                bingoCount++
            }
            else{
                bingoCount = 0
                makeBingo()
            }
            for (i in 0 until numOfBingo) {
                bingoObj!!.draw(batch, "" + bingoStore[i], bingoX[i]!!, 300F)
                bingoX[i] = bingoX[i]!! - 20
                bingoRectangles[i] =
                    Rectangle(
                        bingoX[i]!!.toFloat(), 300F,
                        bird.width.toFloat(), bird.height.toFloat()
                    )
            }


            for (i in 0 until numOfBirds) {
                batch.draw(bird, birdXs[i]!!, birdYs[i]!!)
                birdXs[i] = birdXs[i]!! - 17
                birdRectangles[i] =
                    Rectangle(
                        birdXs[i]!!.toFloat(), birdYs[i]!!.toFloat(),
                        bird.width.toFloat(), bird.height.toFloat()
                    )
            }//for
            if (Gdx.input.justTouched()) {
                if(dinoY == 0)
                {
                    dinoY = 10
                    velocity = -30
                }

            } //if (Gdx.input

            System.out.println(dinoY)
            if (dinoY > 0) {
                velocity = velocity + gravity
                dinoY = dinoY - velocity
            } //if (dinoY
            else {
                dinoY = 0
            }
        } else if (gameState == 0) {
            if (Gdx.input.justTouched()) {
                startTime = (System.currentTimeMillis()/ 1000).toInt()
                gameState = 1
            }
            font!!.data.setScale(7f)
            font!!.draw(batch,""+bingoNumbers[0],150f,1500f)
            font!!.draw(batch,""+bingoNumbers[1],300f,1500f)
            font!!.draw(batch,""+bingoNumbers[2],500f,1500f)
            font!!.draw(batch,""+bingoNumbers[3],700f,1500f)
            font!!.draw(batch,""+bingoNumbers[4],900f,1500f)
            font!!.draw(batch,""+bingoNumbers[5],150f,1300f)
            font!!.draw(batch,""+bingoNumbers[6],300f,1300f)
            font!!.draw(batch,""+bingoNumbers[7],500f,1300f)
            font!!.draw(batch,""+bingoNumbers[8],700f,1300f)
            font!!.draw(batch,""+bingoNumbers[9],900f,1300f)
            font!!.draw(batch,""+bingoNumbers[10],150f,1100f)
            font!!.draw(batch,""+bingoNumbers[11],300f,1100f)
            font!!.draw(batch,"!",500f,1100f)
            font!!.draw(batch,""+bingoNumbers[13],700f,1100f)
            font!!.draw(batch,""+bingoNumbers[14],900f,1100f)
            font!!.draw(batch,""+bingoNumbers[15],150f,900f)
            font!!.draw(batch,""+bingoNumbers[16],300f,900f)
            font!!.draw(batch,""+bingoNumbers[17],500f,900f)
            font!!.draw(batch,""+bingoNumbers[18],700f,900f)
            font!!.draw(batch,""+bingoNumbers[19],900f,900f)
            font!!.draw(batch,""+bingoNumbers[20],150f,700f)
            font!!.draw(batch,""+bingoNumbers[21],300f,700f)
            font!!.draw(batch,""+bingoNumbers[22],500f,700f)
            font!!.draw(batch,""+bingoNumbers[23],700f,700f)
            font!!.draw(batch,""+bingoNumbers[24],900f,700f)
        } else if (gameState == 2) {
            batch.draw(gameover, (screenWidth / 2 - gameover.width / 2).toFloat(), (screenHeight / 2).toFloat()
            )
            if (Gdx.input.justTouched()) {
                gameState = 1
                initialize()
            }
        } //endif
        else if(gameState == 3)
        {
            var posX = arrayOf(150f,300f,500f,700f,900f,150f,300f,500f,700f,900f,150f,300f,500f,700f,900f,150f,300f,500f,700f,900f,150f,300f,500f,700f,900f)
            var posY = arrayOf(1500f,1500f,1500f,1500f,1500f,1300f,1300f,1300f,1300f,1300f,1100f,1100f,1100f,1100f,1100f,900f,900f,900f,900f,900f,700f,700f,700f,700f,700f)
            for(num in 0..24){
                if (bingoState[num] == 0){
                    font!!.draw(batch, "("+bingoNumbers[num]+")", posX[num], posY[num])
                }
                else
                    font!!.draw(batch, ""+bingoNumbers[num], posX[num], posY[num])
            }
            font!!.draw(batch,"GAME WON!\nYou Score is $score\n(lower is better)",200f,500f)
            if (Gdx.input.justTouched()) {
                gameState = 1
                initialize()
            }
        }
        drawdinos()
        for (i in 0 until numOfBirds) {
            if (Intersector.overlaps(dinoCircle, birdRectangles[i])) {
                //Gdx.app.log("bird!", "Collision!")
                birdXs[i] = -100f
                birdYs[i] = -100f
                gameState = 2
            }// if (Intersector.o
        }// for (i in 0 until numOfBirds)
        for (i in 0 until numOfBingo) {
            if (Intersector.overlaps(dinoCircle, bingoRectangles[i])) {
                //Gdx.app.log("bird!", "Collision!")
                bingoX[i] = -1000f
                for(j in bingoNumbers.indices){
                    if (bingoStore[i] == bingoNumbers[j])
                    {

                        bingoState[j] = 0
                        isWin()
                        break
                    }
                }

            }// if (Intersector.o
        }// for (i in 0 until numOfBirds)
        font!!.draw(batch, "" + score, 100f, screenHeight.toFloat())
        batch.end()
        //shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        // shapeRenderer.setColor(Color.BLUE);
        dinoCircle.set(0F, dinoY + (dinos[0].height).toFloat(), (dinos[0].width / 2).toFloat())
        //shapeRenderer.circle(dinoCircle.x, dinoCircle.y, dinoCircle.radius);

        // shapeRenderer.end();
    } //render

    override fun dispose() {
        batch.dispose()
    } //dispose()

}//END
