package com.huai.gamesdk.services;

import java.util.Random;

import com.huai.gamesdk.solid.GameSdkConstants;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.text.TextUtils;

public class Code {
	private static final char[] CHARS = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y',
			'Z' };
	private static Code bpUtil;

	private final int DEFAULT_COLOR = 0xff;// 默认背景颜色值

	// number of chars, lines; font size
	private int codeLength = 4;// 验证码的长度 这里是4位
	private int lineNumber = 6;// 多少条干扰线

	// variables
	private String code;// 保存生成的验证码
	private Random random = new Random();

	private int width;
	private int height;
	private int fontSize;
	private int topPadding;
	private int leftPadding;
	private int baseLeftPadding;

	private Code() {
		int rank = caculate();
		if (rank == 0) {
			fontSize = 20;
			baseLeftPadding = 10;
			width = 60;// 60 + 120 * rank;
			height = 30;
		} else {
			fontSize = (int) (20 + 20 * Math.pow(2, rank));
			baseLeftPadding = (int) (10 + 10 * Math.pow(2, rank));
			width = (int) (60 + 60 * Math.pow(2, rank));
			height = (int) (30 * Math.pow(2, rank));
		}
		topPadding = fontSize;
	}

	public int caculate() {
		int density = GameSdkConstants.DEVICE_INFO.densityDpi;
		if (density == 160) {
			return 0;
		}
		return density / 160;
	}

	public Bitmap getBitmap() {
		return createBitmap();
	}

	/**
	 * 判断验证码是否正确
	 * 
	 * @param code
	 * @return
	 */
	@SuppressLint("DefaultLocale")
	public boolean isCode(String code) {
		if (TextUtils.isEmpty(this.code) || TextUtils.isEmpty(code)) {
			return false;
		}

		return this.code.equals(code.toUpperCase());
	}

	private Bitmap createBitmap() {
		leftPadding = baseLeftPadding;
		Bitmap bp = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas c = new Canvas(bp);
		code = createCode();
		c.drawColor(Color.rgb(DEFAULT_COLOR, DEFAULT_COLOR, DEFAULT_COLOR));
		Paint paint = new Paint();
		paint.setTextSize(fontSize);
		for (int i = 0; i < code.length(); i++) {
			randomTextStyle(paint);
			c.drawText(code.charAt(i) + "", leftPadding, topPadding, paint);
			randomPadding();
		}
		c.drawText("", leftPadding, topPadding, paint);
		for (int i = 0; i < lineNumber; i++) {
			drawLine(c, paint);
		}
		c.save(Canvas.ALL_SAVE_FLAG);
		// 保存
		c.restore();
		return bp;
	}

	private String createCode() {
		StringBuilder buffer = new StringBuilder();
		for (int i = 0; i < codeLength; i++) {
			buffer.append(CHARS[random.nextInt(CHARS.length)]);
		}
		return buffer.toString();
	}

	private void drawLine(Canvas canvas, Paint paint) {
		int color = randomColor();
		int startX = random.nextInt(width);
		int startY = random.nextInt(height);
		int stopX = random.nextInt(width);
		int stopY = random.nextInt(height);
		paint.setStrokeWidth(1);
		paint.setColor(color);
		canvas.drawLine(startX, startY, stopX, stopY, paint);
	}

	private int randomColor() {
		return randomColor(1);
	}

	private int randomColor(int rate) {
		int red = random.nextInt(256) / rate;
		int green = random.nextInt(256) / rate;
		int blue = random.nextInt(256) / rate;
		return Color.rgb(red, green, blue);
	}

	private void randomTextStyle(Paint paint) {
		int color = randomColor();
		paint.setColor(color);
		paint.setFakeBoldText(random.nextBoolean()); // true为粗体，false为非粗体
		float skewX = random.nextInt(11) / 10;
		skewX = random.nextBoolean() ? skewX : -skewX;
		paint.setTextSkewX(skewX); // float类型参数，负数表示右斜，整数左斜
		// paint.setUnderlineText(true); // true为下划线，false为非下划线
		paint.setStrikeThruText(random.nextBoolean()); // true为删除线，false为非删除线
	}

	private void randomPadding() {
		leftPadding += baseLeftPadding;// +
										// random.nextInt(range_padding_left);
	}

	public static Code getInstance() {
		if (bpUtil == null)
			bpUtil = new Code();
		return bpUtil;
	}
}
