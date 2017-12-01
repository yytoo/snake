package com.example.snake;

import java.security.spec.MGF1ParameterSpec;
import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class SnakeView extends View {
	
	public static final String TAG = "SnakeView";
	
	private int mWidth; //view的宽
	private int mHeight; //View的高
	
	private static final int sXOffset = 0;
	private static final int sYOffset =  600; //x坐标和y坐标的偏移量
	
	private final int BOXWIDTH=30; //食物的边长，蛇体的宽度
	private Random mRandom = new Random(); //用于产生随机数
	private Point mFoodPosition; //食物的位置
	private boolean mIsFoodDone = true; //食物是否已经被吃掉
	
	private ArrayList<Point> mSnakeList; //蛇体可以看做是很多食物组成的
	private Paint mSnakePaint;  //用于画蛇的画笔
	private int mSnakeDirection =0; //蛇体运动的方向
	private final int UP=1;
	private final int DOWN=2;
	private final int LEFT=3;
	private final int RIGHT=4;
	private final int START=5;
	
	private Paint mBgPaint; //游戏背景画笔
	private Paint mFoodPaint; //食物画笔
	private Paint mStartPaint;
	private Paint mContolPaint;

	
	private Rect rKeyUp;
	private Rect rKeyDown;
	private Rect rKeyLeft;
	private Rect rKeyRight;
	private Rect rKeyStart;
	
	
	public SnakeView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mSnakeList = new ArrayList<Point>();
		mSnakePaint = new Paint();
		mSnakePaint.setColor(Color.RED);
		mSnakePaint.setStyle(Paint.Style.FILL_AND_STROKE);
		mSnakeList.add(new Point(500,500));
		mSnakeList.add(new Point(500,530));  //初始化出现的蛇
		
		mSnakeDirection = RIGHT;
		mIsFoodDone = true;
		mFoodPosition = new Point();
		
		mFoodPaint = new Paint();
		mFoodPaint.setColor(Color.CYAN);
		mFoodPaint.setStyle(Paint.Style.FILL);
		

		mContolPaint=new Paint();
		mContolPaint.setColor(Color.BLUE);
		mContolPaint.setStyle(Paint.Style.FILL);
		
		mSnakePaint=new Paint();
		mSnakePaint.setColor(Color.BLUE);
		mSnakePaint.setStyle(Paint.Style.FILL);

		
		mBgPaint = new Paint();
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {  //touch控制蛇的运动
		// TODO Auto-generated method stub
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			int x = (int) (event.getX());
			int y = (int) (event.getY());
			Log.e(TAG, "x= " +x+ " y= "+y+" mSnakeDirection= "+mSnakeDirection);
			
			Point head = mSnakeList.get(0);   //根据头部的点位置和手指位置来改变运动方向
			/*if(mSnakeDirection == UP || mSnakeDirection ==DOWN){  			
				if(x<head.x) mSnakeDirection = LEFT;
				if(x>head.x) mSnakeDirection = RIGHT;
			} else if(mSnakeDirection == LEFT || mSnakeDirection == RIGHT){
				if(y<head.y) mSnakeDirection = UP;
				if(y>head.y) mSnakeDirection = DOWN;
			}*/
			if(mSnakeDirection == UP || mSnakeDirection ==DOWN){ 
				if(rKeyLeft.contains(x,y)){
					mSnakeDirection = LEFT;
				}else if(rKeyRight.contains(x,y)){
					mSnakeDirection = RIGHT;
				}
			}else if(mSnakeDirection == LEFT || mSnakeDirection == RIGHT){
				if(rKeyUp.contains(x,y)){
					mSnakeDirection = UP;
				}else if(rKeyDown.contains(x,y)){
					mSnakeDirection = DOWN;
				}				
			}
			Log.e(TAG, "x= " +x+ " y= "+y+" mSnakeDirection=============== "+mSnakeDirection);
			
		}
		return super.onTouchEvent(event);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		
		drawBg(canvas, mBgPaint);
		drawFood(canvas, mFoodPaint);
		drawSnake(canvas, mSnakePaint);
		drawStartButton(canvas, mSnakePaint);
		drawControlButton(canvas, mContolPaint);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		
		mWidth = w;
		mHeight = h;
	}
	
	private void drawBg(Canvas canvas, Paint paint){
		canvas.drawColor(Color.WHITE);
		Rect  rect = new Rect(sXOffset, 0, mWidth - sXOffset, mHeight - sYOffset);
		canvas.drawRect(rect, paint);
	} 
	
	private void drawSnake(Canvas canvas, Paint paint){
		for(int i=0; i<mSnakeList.size(); i++){          //使用for循环逐点描绘蛇的身体
			Point point = mSnakeList.get(i);
			Rect rect = new Rect(point.x, point.y, point.x+BOXWIDTH, point.y+BOXWIDTH);
			canvas.drawRect(rect, paint);
		}
		
		snakeMove(mSnakeList, mSnakeDirection);   //蛇移动，更新list生成下一次刷新需要的数据
		if(isFoodEaten()){
			mIsFoodDone=true;
		}else{
			mSnakeList.remove(mSnakeList.size()-1);  //如果没有吃到食物，犹豫前进时加了一个，这里删除尾巴，出现移动的效果
		}
		
	}
	
	private void drawFood(Canvas canvas, Paint paint){
		if(mIsFoodDone){
			mFoodPosition.x=mRandom.nextInt(mWidth-2*sXOffset-BOXWIDTH)+sXOffset;
			mFoodPosition.y=mRandom.nextInt(mHeight-2*sYOffset-BOXWIDTH)+sYOffset;
			mIsFoodDone = false;
		}
		Rect food = new Rect(mFoodPosition.x, mFoodPosition.y, mFoodPosition.x+BOXWIDTH, mFoodPosition.y+BOXWIDTH);
		canvas.drawRect(food, paint);
	}
	
	public void drawStartButton(Canvas canvas, Paint paint){
		rKeyStart = new Rect(mWidth-400,mHeight-400, mWidth-200,mHeight-200);
		canvas.drawRect(rKeyStart, paint);
		
	}
	
	public void drawControlButton(Canvas canvas, Paint paint){
		Log.e(TAG,"1");
		rKeyUp = new Rect(mWidth-400,mHeight-600, mWidth-200,mHeight-400);
		canvas.drawRect(rKeyUp, paint);
		rKeyDown = new Rect(mWidth-400, mHeight-200,mWidth-200,mHeight);
		canvas.drawRect(rKeyDown, paint);
		rKeyLeft = new Rect(mWidth-600,mHeight-400, mWidth-400,mHeight-200);
		canvas.drawRect(rKeyLeft, paint);
		rKeyRight = new Rect(mWidth-200,mHeight-400, mWidth,mHeight-200);		
		canvas.drawRect(rKeyRight, paint);
		Log.e(TAG,"2");
	}	
	
	public void snakeMove(ArrayList<Point> list, int direction){
		Point orighead = list.get(0);
		Point newhead = new Point();
		
		switch(direction){
		case UP:
            newhead.x = orighead.x;
            newhead.y = orighead.y  - BOXWIDTH ;
            break;
        case DOWN:
            newhead.x = orighead.x;
            newhead.y = orighead.y  + BOXWIDTH ;
            break;
        case LEFT:
            newhead.x = orighead.x  - BOXWIDTH;
            newhead.y = orighead.y;
            break;
        case RIGHT:
            newhead.x = orighead.x + BOXWIDTH ;
            newhead.y = orighead.y;
            break;
        default:
            break;
		}
		adjustHead(newhead);
		list.add(0, newhead);
	}
	
	private boolean isOutBound(Point point){
		if(point.x < sXOffset || point.x >mWidth - sXOffset) return true;
		if(point.y < sYOffset || point.y >mHeight - sYOffset) return true;
		return false;
	}
	
	private void adjustHead(Point point){
		if(isOutBound(point)){
			if(mSnakeDirection == UP) point.y = mHeight - sYOffset - BOXWIDTH;
			if(mSnakeDirection == DOWN) point.y = sYOffset;
			if(mSnakeDirection == LEFT) point.y = mWidth - sXOffset - BOXWIDTH;
			if(mSnakeDirection == RIGHT) point.y = sXOffset;
		}
	}
	
	private boolean isFoodEaten(){
		if(!mIsFoodDone){
			Rect foodrect = new Rect(mFoodPosition.x, mFoodPosition.y, mFoodPosition.x+BOXWIDTH, mFoodPosition.y+BOXWIDTH );
			Point head = mSnakeList.get(0);
			Rect headRect = new Rect(head.x, head.y, head.x+BOXWIDTH, head.y+BOXWIDTH);
			return foodrect.intersect(headRect);
		}
		return false;
	}
	

}
