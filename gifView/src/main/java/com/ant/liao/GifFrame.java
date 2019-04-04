package com.ant.liao;

import android.graphics.Bitmap;

public class GifFrame {
	/**
	 * 构造函数
	 * @param im 图片
	 * @param del 延时
	 */
	public GifFrame(Bitmap im, int del) {
		image = im;
		delay = del;
	}
	/**图片*/
	public Bitmap image;
	/**延时*/
	public int delay;
	/**下一帧*/
	public GifFrame nextFrame = null;
}
