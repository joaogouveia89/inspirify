package io.github.joaogouveia89.inspirify.uiHelpers.recyclerView

import android.R
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView


interface OnSwipeListener {
    fun onSwiped(position: Int)
}

public abstract class SwipeToDeleteCallback(private val context: Context) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    private val mContext: Context = context
    private val mBackground: ColorDrawable = ColorDrawable()
    private val mClearPaint: Paint = Paint()
    private val backgroundColor: Int = Color.parseColor("#b80f0a")
    private val deleteDrawable: Drawable?
    private val intrinsicWidth: Int
    private val intrinsicHeight: Int

    init {
        mClearPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        deleteDrawable = ContextCompat.getDrawable(mContext, R.drawable.ic_menu_delete)
        intrinsicWidth = deleteDrawable?.intrinsicWidth ?: 0
        intrinsicHeight = deleteDrawable?.intrinsicHeight ?: 0
    }

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int = makeMovementFlags(0, ItemTouchHelper.LEFT)
    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean = false
    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float = 0.7f

    override fun onChildDraw(c: Canvas,
                             recyclerView: RecyclerView,
                             viewHolder: RecyclerView.ViewHolder,
                             dX: Float,
                             dY: Float,
                             actionState: Int,
                             isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        val itemView: View = viewHolder.itemView
        val itemHeight: Int = itemView.height

        val isCancelled: Boolean = dX == 0f && !isCurrentlyActive

        if (isCancelled) {
            clearCanvas(
                c = c,
                left = itemView.right + dX,
                top = itemView.top.toFloat(),
                right = itemView.right.toFloat(),
                bottom = itemView.bottom.toFloat()
            )

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            return
        }

        mBackground.apply {
            color = backgroundColor
            setBounds((itemView.right + dX).toInt(), itemView.top, itemView.right, itemView.bottom)
            draw(c)
        }

        val deleteIconTop: Int = itemView.top + (itemHeight - intrinsicHeight) / 2
        val deleteIconMargin: Int = (itemHeight - intrinsicHeight) / 2
        val deleteIconLeft: Int = itemView.right - deleteIconMargin - intrinsicWidth
        val deleteIconRight: Int = itemView.right - deleteIconMargin
        val deleteIconBottom: Int = deleteIconTop + intrinsicHeight

        deleteDrawable?.apply {
            setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
            draw(c)
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun clearCanvas(c: Canvas, left: Float, top: Float, right: Float, bottom: Float) {
        c.drawRect(left, top, right, bottom, mClearPaint)
    }
}