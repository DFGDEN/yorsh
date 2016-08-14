package com.yoursh.dfgden.yorsh.customviews.patternview;

/**
 * Created by likefire on 30.12.15.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.os.Debug;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;


import com.yoursh.dfgden.yorsh.R;
import com.yoursh.dfgden.yorsh.customviews.patternview.cells.Cell;
import com.yoursh.dfgden.yorsh.customviews.patternview.cells.CellManager;
import com.yoursh.dfgden.yorsh.customviews.patternview.utils.CellUtils;

import java.util.ArrayList;
import java.util.List;

public class PatternView extends View {

    private int gridColumns;
    private int gridRows;
    private int maxSize = 0;
    private CellManager cellManager;
    private final Paint pathPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint dotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static final boolean PROFILE_DRAWING = false;
    private boolean drawingProfilingStarted = false;
    private static final int MILLIS_PER_CIRCLE_ANIMATING = 700;
    private OnPatternStartListener onPatternStartListener;
    private OnPatternClearedListener onPatternClearedListener;
    private OnPatternCellAddedListener onPatternCellAddedListener;
    private OnPatternDetectedListener onPatternDetectedListener;
    private ArrayList<Cell> mPattern;
    private float inProgressX = -1;
    private float inProgressY = -1;
    private long animatingPeriodStart;
    private DisplayMode patternDisplayMode = DisplayMode.Correct;
    private boolean inputEnabled = true;
    private boolean inStealthMode = false;
    private boolean inErrorStealthMode = false;
    private boolean enableHapticFeedback = true;
    private boolean patternInProgress = false;
    private final float diameterFactor = 0.10f;
    private final float hitFactor = 0.6f;
    private float squareWidth;
    private float squareHeight;
    private Bitmap bitmapBtnDefault;
    private Bitmap bitmapBtnTouched;
    private Bitmap bitmapCircleDefault;
    private Bitmap bitmapCircleSelected;
    private Bitmap bitmapCircleRed;
    private final Path currentPath = new Path();
    private final Rect invalidate = new Rect();
    private int bitmapWidth;
    private int bitmapHeight;
    private final Matrix circleMatrix = new Matrix();
    private final int padding = 0;
    private final int paddingLeft = padding;
    private final int paddingTop = padding;


    public PatternView(Context context) {
        this(context, null);
    }

    public PatternView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public void setPathColor(int color) {
        pathPaint.setColor(color);
        dotPaint.setColor(color);
    }

    public PatternView(Context context, AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        getFromAttributes(context, attrs);
        init();
        pathPaint.setDither(true);
        pathPaint.setStyle(Paint.Style.STROKE);
        pathPaint.setStrokeJoin(Paint.Join.ROUND);
        pathPaint.setStrokeCap(Paint.Cap.ROUND);
        loadBitmaps();
    }

    private void getFromAttributes(Context context, AttributeSet attrs) {
        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PatternView);
        try {
            maxSize = typedArray.getDimensionPixelSize(R.styleable.PatternView_maxSize, 0);
            final int circleColor = typedArray.getColor(R.styleable.PatternView_circleColor, Color.RED);
            final int dotColor = typedArray.getColor(R.styleable.PatternView_dotColor, circleColor);
            circlePaint.setColorFilter(new PorterDuffColorFilter(circleColor, PorterDuff.Mode.MULTIPLY));
            dotPaint.setColorFilter(new PorterDuffColorFilter(dotColor, PorterDuff.Mode.MULTIPLY));
            pathPaint.setColor(typedArray.getColor(R.styleable.PatternView_pathColor, Color.WHITE));
            gridColumns = typedArray.getInt(R.styleable.PatternView_gridColumns, 3);
            gridRows = typedArray.getInt(R.styleable.PatternView_gridRows, 3);
        } finally {
            typedArray.recycle();
        }
    }

    private void init() {
        cellManager = new CellManager(gridRows, gridColumns);
        final int matrixSize = cellManager.getSize();
        mPattern = new ArrayList<>(matrixSize);
    }


    public void setSelectedBitmap(final int resId) {
        bitmapCircleSelected = getBitmapFor(resId);
        computeBitmapSize();
    }

    public void setDefaultBitmap(final int resId) {
        bitmapCircleDefault = getBitmapFor(resId);
        computeBitmapSize();
    }

    private void loadBitmaps() {
        bitmapBtnDefault = getBitmapFor(R.drawable.pattern_btn_touched);
        bitmapBtnTouched = bitmapBtnDefault;
        bitmapCircleDefault = getBitmapFor(R.drawable.pattern_button_untouched);
        bitmapCircleSelected = getBitmapFor(R.drawable.pattern_circle_white);
        bitmapCircleRed = getBitmapFor(R.drawable.pattern_circle_blue);
        computeBitmapSize();
    }

    private void computeBitmapSize() {
        final Bitmap[] bitmaps = {bitmapBtnDefault,
                bitmapCircleSelected, bitmapCircleRed};

        for (final Bitmap bitmap : bitmaps) {
            bitmapWidth = Math.max(bitmapWidth, bitmap.getWidth());
            bitmapHeight = Math.max(bitmapHeight, bitmap.getHeight());
        }
    }

    private Bitmap getBitmapFor(final int resId) {
        return BitmapFactory.decodeResource(getContext().getResources(), resId);
    }

    public boolean isInStealthMode() {
        return inStealthMode;
    }

    public void setInStealthMode(final boolean inStealthMode) {
        this.inStealthMode = inStealthMode;
    }

    public void setInErrorStealthMode(final boolean inErrorStealthMode) {
        this.inErrorStealthMode = inErrorStealthMode;
    }

    public boolean isTactileFeedbackEnabled() {
        return enableHapticFeedback;
    }

    public void setTactileFeedbackEnabled(boolean tactileFeedbackEnabled) {
        enableHapticFeedback = tactileFeedbackEnabled;
    }


    public void setOnPatternStartListener(OnPatternStartListener onPatternStartListener) {
        this.onPatternStartListener = onPatternStartListener;
    }

    public void setOnPatternClearedListener(OnPatternClearedListener onPatternClearedListener) {
        this.onPatternClearedListener = onPatternClearedListener;
    }

    public void setOnPatternCellAddedListener(OnPatternCellAddedListener onPatternCellAddedListener) {
        this.onPatternCellAddedListener = onPatternCellAddedListener;
    }

    public void setOnPatternDetectedListener(OnPatternDetectedListener onPatternDetectedListener) {
        this.onPatternDetectedListener = onPatternDetectedListener;
    }

    public void setPattern(final DisplayMode displayMode, final List<Cell> pattern) {
        mPattern.clear();
        mPattern.addAll(pattern);
        clearPatternDrawLookup();
        for (Cell cell : pattern) {
            cellManager.draw(cell, true);
        }

        setDisplayMode(displayMode);
    }

    public void setDisplayMode(final DisplayMode displayMode) {
        patternDisplayMode = displayMode;
        if (displayMode == DisplayMode.Animate) {
            if (mPattern.size() == 0) {
                throw new IllegalStateException(
                        "you must have a pattern to "
                                + "animate if you want to set the display mode to animate");
            }
            animatingPeriodStart = SystemClock.elapsedRealtime();
            final Cell first = mPattern.get(0);
            inProgressX = getCenterXForColumn(first.getColumn());
            inProgressY = getCenterYForRow(first.getRow());
            clearPatternDrawLookup();
        }
        invalidate();
    }

    public DisplayMode getDisplayMode() {
        return patternDisplayMode;
    }

    @SuppressWarnings("unchecked")
    public List<Cell> getPattern() {
        return (List<Cell>) mPattern.clone();
    }

    public String getPatternString() {
        return patternToString();
    }

    public String patternToString() {
        if (mPattern == null) {
            return "";
        }
        final int patternSize = mPattern.size();
        final StringBuilder res = new StringBuilder(patternSize);
        for (int i = 0; i < patternSize; i++) {
            final String cellToString = mPattern.get(i).getId();
            res.append(cellToString);
            if (i != patternSize - 1) {
                res.append("&");
            }
        }
        return res.toString();
    }

    public int[] patternToIntArray() {
        if (mPattern == null) {
            return new int[0];
        }
        final int patternSize = mPattern.size();
        final int[] array = new int[patternSize * 2];
        for (int i = 0; i < patternSize; i++) {
            array[i] = mPattern.get(i).getRow();
            array[i + 1] = mPattern.get(i).getColumn();
        }
        return array;
    }

    private void notifyCellAdded() {
        if (onPatternCellAddedListener != null) {
            onPatternCellAddedListener.onPatternCellAdded();
        }
    }

    private void notifyPatternStarted() {
        if (onPatternStartListener != null) {
            onPatternStartListener.onPatternStart();
        }
    }

    private void notifyPatternDetected() {
        if (onPatternDetectedListener != null) {
            onPatternDetectedListener.onPatternDetected();
        }
    }

    private void notifyPatternCleared() {
        if (onPatternClearedListener != null) {
            onPatternClearedListener.onPatternCleared();
        }
    }

    public void clearPattern() {
        cancelClearDelay();
        resetPattern();
        notifyPatternCleared();
    }

    private void resetPattern() {
        mPattern.clear();
        clearPatternDrawLookup();
        patternDisplayMode = DisplayMode.Correct;
        invalidate();
    }

    private void clearPatternDrawLookup() {
        for (int i = 0; i < gridRows; i++) {
            for (int j = 0; j < gridColumns; j++) {
                cellManager.clearDrawing();
            }
        }
    }

    public void disableInput() {
        inputEnabled = false;
    }

    public void enableInput() {
        inputEnabled = true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        int mPaddingRight = padding;
        final int width = w - paddingLeft - mPaddingRight;
        squareWidth = width / (float) gridColumns;

        int mPaddingBottom = padding;
        final int height = h - paddingTop - mPaddingBottom;
        squareHeight = height / (float) gridRows;
    }

    @Override
    protected int getSuggestedMinimumWidth() {
        return gridColumns * bitmapWidth;
    }

    @Override
    protected int getSuggestedMinimumHeight() {
        return gridRows * bitmapWidth;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int size = Math.min(width, height);
        if (maxSize != 0) {
            size = Math.min(size, maxSize);
        }
        setMeasuredDimension(size, size);
    }

    private Cell detectAndAddHit(float x, float y) {
        final Cell cell = checkForNewHit(x, y);
        if (cell != null) {

            final ArrayList<Cell> newCells = new ArrayList<>();
            if (!mPattern.isEmpty()) {
                final Cell lastCell = mPattern.get(mPattern.size() - 1);
                int dRow = cell.getRow() - lastCell.getRow();
                int dCol = cell.getColumn() - lastCell.getColumn();
                int rsign = dRow > 0 ? 1 : -1;
                int csign = dCol > 0 ? 1 : -1;

                if (dRow == 0) {
                    for (int i = 1; i < Math.abs(dCol); i++) {
                        newCells.add(new Cell(lastCell.getRow(), lastCell.getColumn()
                                + i * csign));
                    }
                } else if (dCol == 0) {
                    for (int i = 1; i < Math.abs(dRow); i++) {
                        newCells.add(new Cell(lastCell.getRow() + i * rsign,
                                lastCell.getColumn()));
                    }
                } else if (Math.abs(dCol) == Math.abs(dRow)) {
                    for (int i = 1; i < Math.abs(dRow); i++) {
                        newCells.add(new Cell(lastCell.getRow() + i * rsign,
                                lastCell.getColumn() + i * csign));
                    }
                }

            }
            for (Cell fillInGapCell : newCells) {
                if (fillInGapCell != null && !cellManager.isDrawn(fillInGapCell)) {
                    addCellToPattern(fillInGapCell);
                }
            }
            addCellToPattern(cell);
            if (enableHapticFeedback) {
                performHapticFeedback(
                        HapticFeedbackConstants.VIRTUAL_KEY,
                        HapticFeedbackConstants.FLAG_IGNORE_VIEW_SETTING
                                | HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
            }
            return cell;
        }
        return null;
    }

    private void addCellToPattern(Cell newCell) {
        cellManager.draw(newCell, true);
        mPattern.add(newCell);
        notifyCellAdded();
    }

    private Cell checkForNewHit(float x, float y) {

        final int rowHit = getRowHit(y);
        if (rowHit < 0) {
            return null;
        }
        final int columnHit = getColumnHit(x);
        if (columnHit < 0) {
            return null;
        }

        if (cellManager.isDrawn(rowHit, columnHit)) {
            return null;
        }
        return cellManager.get(rowHit, columnHit);
    }

    private int getRowHit(float y) {

        final float squareHeight = this.squareHeight;
        float hitSize = squareHeight * hitFactor;

        float offset = paddingTop + (squareHeight - hitSize) / 2f;
        for (int i = 0; i < gridRows; i++) {

            final float hitTop = offset + squareHeight * i;
            if (y >= hitTop && y <= hitTop + hitSize) {
                return i;
            }
        }
        return -1;
    }

    private int getColumnHit(float x) {
        final float squareWidth = this.squareWidth;
        float hitSize = squareWidth * hitFactor;

        float offset = paddingLeft + (squareWidth - hitSize) / 2f;
        for (int i = 0; i < gridColumns; i++) {

            final float hitLeft = offset + squareWidth * i;
            if (x >= hitLeft && x <= hitLeft + hitSize) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        if (!inputEnabled || !isEnabled()) {
            return false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                handleActionDown(event);
                return true;
            case MotionEvent.ACTION_UP:
                handleActionUp();
                return true;
            case MotionEvent.ACTION_MOVE:
                handleActionMove(event);
                return true;
            case MotionEvent.ACTION_CANCEL:
                patternInProgress = false;
                resetPattern();
                notifyPatternCleared();

                if (PROFILE_DRAWING) {
                    if (drawingProfilingStarted) {
                        Debug.stopMethodTracing();
                        drawingProfilingStarted = false;
                    }
                }
                return true;
        }
        return false;
    }

    private void handleActionMove(MotionEvent event) {
        final int historySize = event.getHistorySize();
        for (int i = 0; i < historySize + 1; i++) {
            final float x = i < historySize ? event.getHistoricalX(i) : event
                    .getX();
            final float y = i < historySize ? event.getHistoricalY(i) : event
                    .getY();
            final int patternSizePreHitDetect = mPattern.size();
            Cell hitCell = detectAndAddHit(x, y);
            final int patternSize = mPattern.size();
            if (hitCell != null && patternSize == 1) {
                patternInProgress = true;
                notifyPatternStarted();
            }
            final float dx = Math.abs(x - inProgressX);
            final float dy = Math.abs(y - inProgressY);
            if (dx + dy > squareWidth * 0.01f) {
                float oldX = inProgressX;
                float oldY = inProgressY;

                inProgressX = x;
                inProgressY = y;

                if (patternInProgress && patternSize > 0) {
                    final ArrayList<Cell> pattern = mPattern;
                    final float radius = squareWidth * diameterFactor * 0.5f;

                    final Cell lastCell = pattern.get(patternSize - 1);

                    float startX = getCenterXForColumn(lastCell.getColumn());
                    float startY = getCenterYForRow(lastCell.getRow());

                    float left;
                    float top;
                    float right;
                    float bottom;

                    final Rect invalidateRect = invalidate;

                    if (startX < x) {
                        left = startX;
                        right = x;
                    } else {
                        left = x;
                        right = startX;
                    }

                    if (startY < y) {
                        top = startY;
                        bottom = y;
                    } else {
                        top = y;
                        bottom = startY;
                    }

                    invalidateRect.set((int) (left - radius),
                            (int) (top - radius), (int) (right + radius),
                            (int) (bottom + radius));

                    if (startX < oldX) {
                        left = startX;
                        right = oldX;
                    } else {
                        left = oldX;
                        right = startX;
                    }

                    if (startY < oldY) {
                        top = startY;
                        bottom = oldY;
                    } else {
                        top = oldY;
                        bottom = startY;
                    }

                    invalidateRect.union((int) (left - radius),
                            (int) (top - radius), (int) (right + radius),
                            (int) (bottom + radius));

                    if (hitCell != null) {
                        startX = getCenterXForColumn(hitCell.getColumn());
                        startY = getCenterYForRow(hitCell.getRow());

                        if (patternSize >= 2) {
                            hitCell = pattern.get(patternSize - 1
                                    - (patternSize - patternSizePreHitDetect));
                            oldX = getCenterXForColumn(hitCell.getColumn());
                            oldY = getCenterYForRow(hitCell.getRow());

                            if (startX < oldX) {
                                left = startX;
                                right = oldX;
                            } else {
                                left = oldX;
                                right = startX;
                            }

                            if (startY < oldY) {
                                top = startY;
                                bottom = oldY;
                            } else {
                                top = oldY;
                                bottom = startY;
                            }
                        } else {
                            left = right = startX;
                            top = bottom = startY;
                        }

                        final float widthOffset = squareWidth / 2f;
                        final float heightOffset = squareHeight / 2f;

                        invalidateRect.set((int) (left - widthOffset),
                                (int) (top - heightOffset),
                                (int) (right + widthOffset),
                                (int) (bottom + heightOffset));
                    }

                    invalidate(invalidateRect);
                } else {
                    invalidate();
                }
            }
        }
        invalidate();
    }

    private void handleActionUp() {
        if (!mPattern.isEmpty()) {
            patternInProgress = false;
            notifyPatternDetected();
            invalidate();
        }
        if (PROFILE_DRAWING) {
            if (drawingProfilingStarted) {
                Debug.stopMethodTracing();
                drawingProfilingStarted = false;
            }
        }
    }

    private void handleActionDown(MotionEvent event) {
        resetPattern();
        final float x = event.getX();
        final float y = event.getY();
        final Cell hitCell = detectAndAddHit(x, y);
        if (hitCell != null) {
            patternInProgress = true;
            patternDisplayMode = DisplayMode.Correct;
            notifyPatternStarted();
        } else {
            patternInProgress = false;
            notifyPatternCleared();
        }
        if (hitCell != null) {
            final float startX = getCenterXForColumn(hitCell.getColumn());
            final float startY = getCenterYForRow(hitCell.getRow());

            final float widthOffset = squareWidth / 2f;
            final float heightOffset = squareHeight / 2f;

            invalidate((int) (startX - widthOffset),
                    (int) (startY - heightOffset),
                    (int) (startX + widthOffset), (int) (startY + heightOffset));
        }
        inProgressX = x;
        inProgressY = y;
        if (PROFILE_DRAWING) {
            if (!drawingProfilingStarted) {
                Debug.startMethodTracing("LockPatternDrawing");
                drawingProfilingStarted = true;
            }
        }
    }

    private float getCenterXForColumn(int column) {
        return paddingLeft + column * squareWidth + squareWidth / 2f;
    }

    private float getCenterYForRow(int row) {
        return paddingTop + row * squareHeight + squareHeight / 2f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final ArrayList<Cell> pattern = mPattern;
        final int count = pattern.size();

        if (patternDisplayMode == DisplayMode.Animate) {

            final int oneCycle = (count + 1) * MILLIS_PER_CIRCLE_ANIMATING;
            final int spotInCycle = (int) (SystemClock.elapsedRealtime() - animatingPeriodStart)
                    % oneCycle;
            final int numCircles = spotInCycle / MILLIS_PER_CIRCLE_ANIMATING;

            clearPatternDrawLookup();
            for (int i = 0; i < numCircles; i++) {
                final Cell cell = pattern.get(i);
                cellManager.draw(cell, true);
            }

            final boolean needToUpdateInProgressPoint = numCircles > 0
                    && numCircles < count;

            if (needToUpdateInProgressPoint) {
                final float percentageOfNextCircle = ((float) (spotInCycle % MILLIS_PER_CIRCLE_ANIMATING))
                        / MILLIS_PER_CIRCLE_ANIMATING;

                final Cell currentCell = pattern.get(numCircles - 1);
                final float centerX = getCenterXForColumn(currentCell.getColumn());
                final float centerY = getCenterYForRow(currentCell.getRow());

                final Cell nextCell = pattern.get(numCircles);
                final float dx = percentageOfNextCircle
                        * (getCenterXForColumn(nextCell.getColumn()) - centerX);
                final float dy = percentageOfNextCircle
                        * (getCenterYForRow(nextCell.getRow()) - centerY);
                inProgressX = centerX + dx;
                inProgressY = centerY + dy;
            }
            invalidate();
        }

        final float squareWidth = this.squareWidth;
        final float squareHeight = this.squareHeight;

        float radius = (squareWidth * diameterFactor * 0.5f);
        pathPaint.setStrokeWidth(radius);

        final Path currentPath = this.currentPath;
        currentPath.rewind();

        // draw the circles
        final int paddingTop = this.paddingTop;
        final int paddingLeft = this.paddingLeft;

        for (int i = 0; i < gridRows; i++) {
            float topY = paddingTop + i * squareHeight;
            for (int j = 0; j < gridColumns; j++) {
                float leftX = paddingLeft + j * squareWidth;
                drawCircle(canvas, (int) leftX, (int) topY, cellManager.isDrawn(i, j));
            }
        }

        final boolean drawPath = (!inStealthMode
                && patternDisplayMode == DisplayMode.Correct || !inErrorStealthMode
                && patternDisplayMode == DisplayMode.Wrong);

        boolean oldFlagCircle = (circlePaint.getFlags() & Paint.FILTER_BITMAP_FLAG) != 0;
        boolean oldFlagDot = (dotPaint.getFlags() & Paint.FILTER_BITMAP_FLAG) != 0;
        circlePaint.setFilterBitmap(true);
        dotPaint.setFilterBitmap(true);

        if (drawPath) {
            boolean anyCircles = false;
            for (int i = 0; i < count; i++) {
                Cell cell = pattern.get(i);

                if (!cellManager.isDrawn(cell)) {
                    break;
                }
                anyCircles = true;

                float centerX = getCenterXForColumn(cell.getColumn());
                float centerY = getCenterYForRow(cell.getRow());
                if (i == 0) {
                    currentPath.moveTo(centerX, centerY);
                } else {
                    currentPath.lineTo(centerX, centerY);
                }
            }

            if ((patternInProgress || patternDisplayMode == DisplayMode.Animate)
                    && anyCircles && count > 1) {
                currentPath.lineTo(inProgressX, inProgressY);
            }
            canvas.drawPath(currentPath, pathPaint);
        }

        circlePaint.setFilterBitmap(oldFlagCircle);
        dotPaint.setFilterBitmap(oldFlagDot);
    }

    private void drawCircle(Canvas canvas, int leftX, int topY,
                            boolean partOfPattern) {
        Bitmap outerCircle;
        Bitmap innerCircle;

        if (!partOfPattern
                || (inStealthMode && patternDisplayMode == DisplayMode.Correct)
                || (inErrorStealthMode && patternDisplayMode == DisplayMode.Wrong)) {
            outerCircle = bitmapCircleDefault;
            innerCircle = bitmapBtnDefault;
        } else if (patternInProgress) {
            // user is in middle of drawing a pattern
            outerCircle = bitmapCircleSelected;
            innerCircle = bitmapBtnTouched;
        } else if (patternDisplayMode == DisplayMode.Wrong) {
            // the pattern is wrong
            outerCircle = bitmapCircleRed;
            innerCircle = bitmapBtnDefault;
        } else if (patternDisplayMode == DisplayMode.Correct
                || patternDisplayMode == DisplayMode.Animate) {
            // the pattern is correct
            outerCircle = bitmapCircleSelected;
            innerCircle = bitmapBtnDefault;
        } else {
            throw new IllegalStateException("unknown display mode "
                    + patternDisplayMode);
        }

        final int width = bitmapWidth;
        final int height = bitmapHeight;

        final float squareWidth = this.squareWidth;
        final float squareHeight = this.squareHeight;

        int offsetX = (int) ((squareWidth - width) / 2f);
        int offsetY = (int) ((squareHeight - height) / 2f);

        float sx = Math.min(this.squareWidth / bitmapWidth, 1.0f);
        float sy = Math.min(this.squareHeight / bitmapHeight, 1.0f);

        circleMatrix.setTranslate(leftX + offsetX, topY + offsetY);
        circleMatrix.preTranslate(bitmapWidth / 2, bitmapHeight / 2);
        circleMatrix.preScale(sx, sy);
        circleMatrix.preTranslate(-bitmapWidth / 2, -bitmapHeight / 2);

        canvas.drawBitmap(outerCircle, circleMatrix, circlePaint);
        canvas.drawBitmap(innerCircle, circleMatrix, dotPaint);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        return new SavedState(superState, patternToIntArray(),
                patternDisplayMode.ordinal(), inputEnabled, inStealthMode,
                enableHapticFeedback);
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        final SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        setPattern(DisplayMode.Correct, CellUtils.intArrayToPattern(ss.getSerializedPattern(), cellManager));
        patternDisplayMode = DisplayMode.values()[ss.getDisplayMode()];
        inputEnabled = ss.isInputEnabled();
        inStealthMode = ss.isInStealthMode();
        enableHapticFeedback = ss.isTactileFeedbackEnabled();
    }

    private static class SavedState extends BaseSavedState {

        private final int[] mSerializedPattern;
        private final int mDisplayMode;
        private final boolean mInputEnabled;
        private final boolean mInStealthMode;
        private final boolean mTactileFeedbackEnabled;

        private SavedState(Parcelable superState, int[] serializedPattern,
                           int displayMode, boolean inputEnabled, boolean inStealthMode,
                           boolean tactileFeedbackEnabled) {
            super(superState);
            mSerializedPattern = serializedPattern;
            mDisplayMode = displayMode;
            mInputEnabled = inputEnabled;
            mInStealthMode = inStealthMode;
            mTactileFeedbackEnabled = tactileFeedbackEnabled;
        }

        private SavedState(Parcel in) {
            super(in);
            mSerializedPattern = in.createIntArray();
            mDisplayMode = in.readInt();
            mInputEnabled = (Boolean) in.readValue(null);
            mInStealthMode = (Boolean) in.readValue(null);
            mTactileFeedbackEnabled = (Boolean) in.readValue(null);
        }

        public int[] getSerializedPattern() {
            return mSerializedPattern;
        }

        public int getDisplayMode() {
            return mDisplayMode;
        }

        public boolean isInputEnabled() {
            return mInputEnabled;
        }

        public boolean isInStealthMode() {
            return mInStealthMode;
        }

        public boolean isTactileFeedbackEnabled() {
            return mTactileFeedbackEnabled;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeIntArray(mSerializedPattern);
            dest.writeInt(mDisplayMode);
            dest.writeValue(mInputEnabled);
            dest.writeValue(mInStealthMode);
            dest.writeValue(mTactileFeedbackEnabled);
        }

        @SuppressWarnings("unused")
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {

            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };

    }

    public void clearPattern(long delayMillis) {
        cancelClearDelay();
        postDelayed(mPatternClearer, delayMillis);
    }

    public void cancelClearDelay() {
        removeCallbacks(mPatternClearer);
    }

    private final Runnable mPatternClearer = new Runnable() {

        @Override
        public void run() {
            clearPattern();
        }
    };

    public void onShow() {
        clearPattern();
    }

    public enum DisplayMode {
        Correct,
        Animate,
        Wrong
    }

    public interface OnPatternStartListener {

        void onPatternStart();
    }

    public interface OnPatternClearedListener {
        void onPatternCleared();
    }

    public interface OnPatternCellAddedListener {
        void onPatternCellAdded();
    }

    public interface OnPatternDetectedListener {
        void onPatternDetected();
    }
}