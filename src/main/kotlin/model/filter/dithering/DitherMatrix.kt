package model.filter.dithering

class DitherMatrix(val matrix: Array<Array<Int>>) {

    companion object Factory {
        fun getBayerDitherMatrix(dimension: Int): DitherMatrix {
            when (dimension) {
                2 -> return getBayer2x2()
                3 -> return getBayer3x3()
                4 -> return getBayer4x4()
                6 -> return getBayer6x6()
                else -> {
                    throw RuntimeException("Unsopported dimension of dither matrix")
                }
            }
        }

        private fun getBayer2x2(): DitherMatrix {
            return DitherMatrix(arrayOf(
                    arrayOf(1, 3),
                    arrayOf(4, 2)))
        }

        private fun getBayer3x3(): DitherMatrix {
            return DitherMatrix(arrayOf(
                    arrayOf(3, 7, 4),
                    arrayOf(6, 1, 9),
                    arrayOf(2, 8, 5)))
        }

        private fun getBayer4x4(): DitherMatrix {
            return DitherMatrix(arrayOf(
                    arrayOf(1, 9, 3, 11),
                    arrayOf(13, 5, 15, 7),
                    arrayOf(4, 12, 2, 10),
                    arrayOf(16, 8, 14, 6)))
        }

        private fun getBayer6x6(): DitherMatrix {
            return DitherMatrix(arrayOf(
                    arrayOf(9, 25, 13, 11, 27, 15),
                    arrayOf(21, 1, 33, 23, 3, 35),
                    arrayOf(5, 29, 17, 7, 31, 19),
                    arrayOf(12, 28, 16, 10, 26, 14),
                    arrayOf(24, 4, 36, 22, 2, 34),
                    arrayOf(8, 32, 20, 6, 30, 18)))
        }
    }

    fun get(x: Int, y: Int): Int {
        return matrix[x][y]
    }
}