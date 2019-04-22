package com.arman.jenny

import com.arman.jenny.gui.CaveView
import com.arman.jenny.gui.View
import org.junit.Test
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.JFrame

class TestCave {

    @Test
    fun test() {
        val view = CaveView()
        val frame = JFrame()
        frame.add(view, BorderLayout.CENTER)
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.size = Dimension(View.WIDTH, View.HEIGHT)
        frame.isResizable = false
        frame.setLocationRelativeTo(null)
        frame.isVisible = true
        view.init()
        while (view.running) {}
    }

}