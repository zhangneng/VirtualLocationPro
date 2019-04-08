package cn.com.cmplatform.utils;

import cn.com.cmplatform.gameplatform.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.ant.liao.GifView;
import com.ant.liao.GifView.GifImageType;

public class UtilsGifAction extends Activity implements OnClickListener {

	private GifView gf1;

	private GifView gf2;

	private boolean f = true;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		setContentView(R.layout.gif);
		gf1 = (GifView) findViewById(R.id.gif1);
		gf1.setGifImage(R.drawable.gif1);
		gf1.setOnClickListener(this);

		gf2 = (GifView) findViewById(R.id.gif2);
		gf2.setGifImageType(GifImageType.COVER);
		gf2.setShowDimension(300, 300);
		gf2.setGifImage(R.drawable.a);
	}

	public void onClick(View v) {
		if (f) {
			gf1.showCover();
			f = false;
		} else {
			gf1.showAnimation();
			f = true;
		}
	}
}
