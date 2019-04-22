package com.arman.jenny.gen.cave

import java.util.*

/**
 * Created by Coen on 22-4-2019.
 */
object CaveGen {

    private const val FLOOR_LIMIT = 3
    private const val WALL_LIMIT = 4
    private const val TREASURE_SECRECY = 4
    private const val TREASURE_PROBABILITY = 0.1
    private const val ENTRANCE_PROBABILITY = 0.2
    private const val WALL_PROBABILITY = 0.37

    private const val WALL = 0
    private const val FLOOR = 1
    private const val TREASURE = 2
    private const val ENTRANCE = 3
    private const val STAIRS = 4

    private var random: Random = Random()

    private fun initialize(data: Array<IntArray>) {
        val rows = data.size
        val cols = data[0].size
        for (x in 0 until rows) {
            for (y in 0 until cols) {
                data[x][y] = if (Math.random() < WALL_PROBABILITY) WALL else FLOOR
            }
        }
        for (y in 0 until cols) {
            data[rows - 1][y] = WALL
            data[0][y] = WALL
        }
        for (x in 0 until rows) {
            data[x][cols - 1] = WALL
            data[x][0] = WALL
        }
    }

    private fun iterate(data: Array<IntArray>): Array<IntArray> {
        val rows = data.size
        val cols = data[0].size
        val newData = data
        for (x in 1 until rows - 1) {
            for (y in 1 until cols - 1) {
                val count = countNeighbours(data, x, y)
                if (data[x][y] == WALL) {
                    if (count < FLOOR_LIMIT) {
                        newData[x][y] = FLOOR
                    } else {
                        newData[x][y] = WALL
                    }
                } else {
                    if (count > WALL_LIMIT) {
                        newData[x][y] = WALL
                    } else {
                        newData[x][y] = FLOOR
                    }
                }
            }
        }
        return newData
    }

    private fun countNeighbours(data: Array<IntArray>, x: Int, y: Int): Int {
        val rows = data.size
        val cols = data[0].size
        var count = 0
        for (i in -1..1) {
            for (j in -1..1) {
                if (i == 0 && j == 0) {
                    continue
                }
                val neighbourX = x + i
                val neighbourY = y + j
                if (neighbourX < 0 || neighbourY < 0 || neighbourX >= rows || neighbourY >= cols) {
                    count++
                } else if (data[neighbourX][neighbourY] == WALL || data[neighbourX][neighbourY] == ENTRANCE) {
                    count++
                }
            }
        }
        return count
    }

    private fun addEntrances(data: Array<IntArray>) {
        val rows = data.size
        val cols = data[0].size
        for (y in 0 until cols) {
            //check most left column
            if (data[1][y] == FLOOR && Math.random() < ENTRANCE_PROBABILITY) {
                data[0][y] = ENTRANCE
            }
            //check most right column
            if (data[rows - 2][y] == FLOOR && Math.random() < ENTRANCE_PROBABILITY) {
                data[rows - 1][y] = ENTRANCE
            }
        }
        for (x in 0 until rows) {
            //check upper row
            if (data[x][1] == FLOOR && Math.random() < ENTRANCE_PROBABILITY) {
                data[x][0] = ENTRANCE
            }
            //check lower row
            if (data[x][cols - 2] == FLOOR && Math.random() < ENTRANCE_PROBABILITY) {
                data[x][cols - 1] = ENTRANCE
            }
        }
    }

    private fun addStairs(data: Array<IntArray>) {
        while (true) {
            var i = random.nextInt(data.size)
            var j = random.nextInt(data[i].size)
            while (i < data.size) {
                while (j < data[i].size) {
                    if (data[i][j] == FLOOR) {
                        data[i][j] = STAIRS
                        return
                    }
                    j++
                }
                i++
            }
        }
    }

    private fun placeTreasures(data: Array<IntArray>) {
        for (x in 1 until data.size - 1) {
            for (y in 1 until data[x].size - 1) {
                if (data[x][y] == FLOOR) {
                    if (countNeighbours(data, x, y) >= TREASURE_SECRECY) {
                        if (Math.random() < TREASURE_PROBABILITY) {
                            data[x][y] = TREASURE
                        }
                    }
                }
            }
        }
    }

    fun generate(rows: Int, cols: Int, iterations: Int): Array<IntArray> {
        val data = Array(rows) { IntArray(cols) }
        initialize(data)
        for (i in 0 until iterations) {
            iterate(data)
        }
        return data
    }

    fun generateWithStairs(rows: Int, cols: Int, iterations: Int): Array<IntArray> {
        val data = generate(rows, cols, iterations)
        addStairs(data)
        return data
    }

    fun generateWithEntrances(rows: Int, cols: Int, iterations: Int): Array<IntArray> {
        val data = generate(rows, cols, iterations)
        addEntrances(data)
        return data
    }

    fun generateWithTreasures(rows: Int, cols: Int, iterations: Int): Array<IntArray> {
        val data = generate(rows, cols, iterations)
        placeTreasures(data)
        return data
    }

    fun generateWithEntrancesAndTreasures(rows: Int, cols: Int, iterations: Int): Array<IntArray> {
        val data = generate(rows, cols, iterations)
        placeTreasures(data)
        addEntrances(data)
        return data
    }

}