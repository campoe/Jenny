package com.arman.jenny.gui

import java.awt.Canvas
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.image.BufferStrategy

typealias BitMap = Array<IntArray>

fun BitMap.prettyPrint() {
    for (i in 0 until this.size) {
        val row = this[i]
        for (j in 0 until row.size) {
            print(row[j].toString() + " ")
        }
        println()
    }
}

abstract class View(protected val scale: Float = 5f) : Canvas(), Runnable {

    private lateinit var g: Graphics
    var running: Boolean = false
        private set
    private lateinit var bs: BufferStrategy
    protected lateinit var bitMap: BitMap

    init {
        this.isVisible = true
        this.size = Dimension(WIDTH / scale.toInt(), HEIGHT / scale.toInt())
    }

    fun init() {
        createBufferStrategy(3)
        bs = bufferStrategy
        g = bs.drawGraphics
        running = true
        val thread = Thread(this)
        thread.start()
    }

    override fun run() {
        var time: Double
        var lastTime = System.nanoTime() / 1000000000.0
        var passed: Double
        var delta = 0.0
        var rate = 0.0
        var frames = 0
        var fps = 0
        while (running) {
            var render = false
            time = System.nanoTime() / 1000000000.0
            passed = time - lastTime
            lastTime = time
            delta += passed
            rate += passed
            val tickSpeed = 1.0 / 60.0
            while (delta >= tickSpeed) {
                delta -= tickSpeed
                render = true
                //ticks / updates
                tick(tickSpeed.toFloat())
                if (rate >= 1.0) {
                    rate = 0.0
                    fps = frames
                    frames = 0
                }
            }
            if (render) {
                //render
                render(g)
                bs.show()
                frames++
            } else {
                try {
                    Thread.sleep(20)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

            }
        }
    }

    open fun tick(elapsedTime: Float) = Unit

    open fun render(g: Graphics) {
        g.color = Color.BLACK
        g.fillRect(0, 0, width, height)
    }

    companion object {

        const val WIDTH = 800
        const val HEIGHT = 600

    }

}
