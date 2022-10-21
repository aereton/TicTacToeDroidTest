package com.example.tictactoetest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ContextThemeWrapper
import androidx.databinding.DataBindingUtil
import com.example.tictactoetest.databinding.ActivityMainBinding
import kotlin.math.abs

class MainActivity : AppCompatActivity() {

    enum class PlayerTurn { A, B }

    private lateinit var binding: ActivityMainBinding

    private val cells = mutableListOf(
        0,-1,-2,
        -3,-4,-5,
        -6,-7,-8,
    )

    var currentPlayer = PlayerTurn.A
    var gameRunning = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.btnRestart.isClickable = false
        binding.btnRestart.alpha = 0.5f

        val cellBtns = listOf(
            binding.a1, binding.a2, binding.a3,
            binding.b1, binding.b2, binding.b3,
            binding.c1, binding.c2, binding.c3
        )

        val imgA = R.drawable.ic_baseline_close_24
        val imgB = R.drawable.ic_baseline_panorama_fish_eye_24

        binding.btnRestart.setOnClickListener {
            if (!gameRunning) {
                // reset cells
                for (i in 0 downTo -8) {
                    cells[abs(i)] = i
                }
                // reset cell buttons
                for (btn in cellBtns) {
                    btn.setBackgroundResource(R.color.cellbg_run)
                    btn.setImageResource(0)
                }
                gameRunning = true
                binding.btnRestart.isClickable = false
                binding.btnRestart.alpha = 0.5f
            }
        }

        for(i in cellBtns.indices) {
            cellBtns[i].setOnClickListener {
                if (gameRunning) {
                    if (setCell(i)) {
                        when (currentPlayer) {
                            PlayerTurn.A -> cellBtns[i].setImageResource(imgA)
                            PlayerTurn.B -> cellBtns[i].setImageResource(imgB)
                        }
                        switchPlayerTurn()
                        var win = checkWin()
                        if (win.any() || cells.all { it > 0 }) {
                            for (i in win) {
                                cellBtns[i].setBackgroundResource(R.color.cellbg_win)
                            }
                            gameRunning = false
                            binding.btnRestart.isClickable = true
                            binding.btnRestart.alpha = 1f
                        }
                    }
                }
            }
        }
    }

    private fun setCell(cellIndex: Int) : Boolean {
        if (cells[cellIndex] <= 0) {
            when (currentPlayer) {
                PlayerTurn.A -> {
                    cells[cellIndex] = 1
                }
                PlayerTurn.B -> {
                    cells[cellIndex] = 2
                }
            }
            return true
        }
        return false
    }

    private fun switchPlayerTurn() {
        currentPlayer = when (currentPlayer) {
            PlayerTurn.A -> PlayerTurn.B
            PlayerTurn.B -> PlayerTurn.A
        }
    }

    private fun checkWin(): List<Int> {
        // ROWS
        if (cells[0] == cells[1] && cells[0] == cells[2]) {
            return listOf(0,1,2)
        }
        if (cells[3] == cells[4] && cells[3] == cells[5]) {
            return listOf(3,4,5)
        }
        if (cells[6] == cells[7] && cells[6] == cells[8]) {
            return listOf(6,7,8)
        }

        // COLUMNS
        if (cells[0] == cells[3] && cells[0] == cells[6]) {
            return listOf(0,3,6)
        }
        if (cells[1] == cells[4] && cells[1] == cells[7]) {
            return listOf(1,4,7)
        }
        if (cells[2] == cells[5] && cells[2] == cells[8]) {
            return listOf(2,5,8)
        }

        // DIAGONALS
        if (cells[0] == cells[4] && cells[0] == cells[8]) {
            return listOf(0,4,8)
        }
        if (cells[2] == cells[4] && cells[2] == cells[6]) {
            return listOf(2,4,6)
        }


        return listOf()
    }
}