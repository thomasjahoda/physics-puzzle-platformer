package com.jafleck.game.util.math

/**
 * From https://github.com/vania-pooh/kotlin-algorithms/blob/master/src/com/freaklius/kotlin/algorithms/sort/QuickSort.kt - copied because I'm too lazy to implement it again and again.
Copyright (C) 2013 Ivan Krutov
Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
object QuickSort {

    fun sort(arr: FloatArray): FloatArray {
        sortArrayPart(arr, 0, arr.size - 1)
        return arr
    }

    /**
     * Swaps initial array elements having keys in [fromIndex toIndex] range
     * so that two subarrays are formed: leftArr[i] <= arr[middleIndex] <= rightArr[j]
     * for any i, j and fromIndex <= middleIndex <= toIndex
     * @param arr
     * @param fromIndex
     * @param toIndex
     * @return returns middleIndex value
     */
    private fun partition(arr: FloatArray, fromIndex: Int, toIndex: Int): Int {
        val lastElementValue = arr[toIndex] // note potential performance-improvement: use 3-value median instead of last element
        var i = fromIndex - 1
        for (j in fromIndex until toIndex) {
            if (arr[j] <= lastElementValue) {
                i++
                swap(arr, i, j)
            }
        }
        //Swapping leftmost element of the second part with the last element of the array, i.e. with middle element
        swap(arr, i + 1, toIndex)
        return i + 1
    }

    /**
     * Sorts a part of input array
     * @param arr
     * @param fromIndex
     * @param toIndex
     */
    fun sortArrayPart(arr: FloatArray, fromIndex: Int, toIndex: Int) {
        if (fromIndex < toIndex) {
            val middleIndex = partition(arr, fromIndex, toIndex)
            sortArrayPart(arr, fromIndex, middleIndex - 1)
            sortArrayPart(arr, middleIndex + 1, toIndex)
        }
    }
}

/**
 * Swaps i-th and j-th elements of the array
 * @param arr
 * @param i
 * @param j
 */
private fun swap(arr: FloatArray, i: Int, j: Int): FloatArray {
    val tmp = arr[i]
    arr[i] = arr[j]
    arr[j] = tmp
    return arr
}
