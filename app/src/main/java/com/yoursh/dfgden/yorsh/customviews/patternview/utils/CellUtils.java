package com.yoursh.dfgden.yorsh.customviews.patternview.utils;

/**
 * Created by likefire on 30.12.15.
 */



import com.yoursh.dfgden.yorsh.customviews.patternview.cells.Cell;
import com.yoursh.dfgden.yorsh.customviews.patternview.cells.CellManager;

import java.util.ArrayList;
import java.util.List;

public class CellUtils {

    private CellUtils() {

    }

    public static void checkRange(int row, int column) {
        if (row < 0) {
            throw new IllegalArgumentException("row must be in range 0-" + (row - 1));
        }
        if (column < 0) {
            throw new IllegalArgumentException("column must be in range 0-" + (row - 1));
        }
    }

    public static List<Cell> stringToPattern(final String string, final CellManager cellManager) {
        final List<Cell> result = new ArrayList<>();

        if (string.isEmpty()) {
            return result;
        }

        final String[] allCells = string.split("&");
        for (String allCell : allCells) {
            final String[] rowAndColumn = allCell.split("-");
            final int row = Integer.valueOf(rowAndColumn[0]);
            final int column = Integer.valueOf(rowAndColumn[1]);
            final Cell cell = cellManager.get(row, column);
            result.add(cell);
        }
        return result;
    }

    public static List<Cell> intArrayToPattern(final int[] array, final CellManager cellManager) {
        final List<Cell> result = new ArrayList<>();

        if (array.length == 0) {
            return result;
        }

        for (int i = 0, j = array.length; i < j; i += 2) {
            final int row = array[i];
            final int column = array[i + 1];
            final Cell cell = cellManager.get(row, column);
            result.add(cell);
        }
        return result;
    }
}