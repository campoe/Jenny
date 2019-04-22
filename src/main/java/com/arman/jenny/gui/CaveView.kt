package com.arman.jenny.gui

import com.arman.jenny.gen.cave.CaveGen
import java.awt.Color
import java.awt.Graphics

class CaveView(scale: Float = 5f) : View(scale) {

    init {
        this.bitMap = CaveGen.generateWithEntrancesAndTreasures(height, width, 50)
    }

    override fun render(g: Graphics) {
        super.render(g)
        for (i in 0 until this.bitMap.size) {
            val row = this.bitMap[i]
            for (j in 0 until row.size) {
                val cell = row[j]
                var color = when (cell) {
                    0 -> Color.BLACK
                    1 -> Color.WHITE
                    2 -> Color.GREEN
                    3 -> Color.BLUE
                    else -> Color.WHITE
                }
                g.color = color
                val scaleI = scale.toInt()
                g.fillRect(j * scaleI, i * scaleI, scaleI, scaleI)
            }
        }
    }

}