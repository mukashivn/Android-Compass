package com.core.compass.ui.custom;

import android.content.res.XmlResourceParser;
import android.content.res.Resources;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParserException;
import android.util.Log;
import android.os.Message;
import java.util.LinkedList;
import android.os.Handler;
import android.content.Context;
import android.widget.ImageView;

import java.util.List;
/**
 * Package: com.core.ssvapp.ui.custom
 * Created by: CuongCK
 * Date: 2/28/18
 */

public class TutorialAnimation {
    private final int ANIMATION_CMD;
    private List<AnimationItem> mAnimation;
    private ImageView mAnimationView;
    private Context mContext;
    private Handler mHandler;
    private int mIndex;
    private boolean mIsAnimating;

    public TutorialAnimation(final Context mContext) {
        this.ANIMATION_CMD = 1;
        this.mAnimation = new LinkedList<AnimationItem>();
        this.mHandler = new Handler() {
            public void handleMessage(final Message message) {
                if (TutorialAnimation.this.mIsAnimating) {
                    TutorialAnimation.this.mIndex = (TutorialAnimation.this.mIndex + 1) % TutorialAnimation.this.mAnimation.size();
                    final AnimationItem animationItem = TutorialAnimation.this.mAnimation.get(TutorialAnimation.this.mIndex);
                    TutorialAnimation.this.mAnimationView.setImageResource(animationItem.mResId);
                    TutorialAnimation.this.mHandler.sendEmptyMessageDelayed(1, (long)animationItem.mDuration);
                }
            }
        };
        this.mContext = mContext;
    }

    private void loadAnimationFrame(int paramInt) {
        Resources localResources = this.mContext.getResources();
        XmlResourceParser localXmlResourceParser = localResources.getXml(paramInt);
        try
        {
            for (;;)
            {
                paramInt = localXmlResourceParser.next();
                if (paramInt == 1) {
                    break;
                }
                if ((paramInt == 2) && (localXmlResourceParser.getName().equals("item")))
                {
                    /*
                    TypedArray localTypedArray = localResources.obtainAttributes(localXmlResourceParser, R.styleable.AnimationDrawableItem);
                    int i = localTypedArray.getInt(0, -1);
                    paramInt = i;
                    if (i < 0) {
                        paramInt = 100;
                    }
                    i = localTypedArray.getResourceId(1, 0);
                    this.mAnimation.add(new TutorialAnimation.AnimationItem( i, paramInt));
                    localTypedArray.recycle();
                    */
                }
            }
            return;
        }
        catch (XmlPullParserException localXmlPullParserException)
        {
            Log.e("Compass:TutorialAnimation", "loadAnimationFrame XmlPullParserException", localXmlPullParserException);
            return;
        }
        catch (IOException localIOException)
        {
            Log.e("Compass:TutorialAnimation", "loadAnimationFrame IOException", localIOException);
        }
    }

    public void startAnimation(final ImageView mAnimationView, final int n) {
        this.mAnimationView = mAnimationView;
        this.mIsAnimating = true;
        this.mAnimation.clear();
        this.mIndex = 0;
        this.loadAnimationFrame(n);
        this.mHandler.sendEmptyMessage(1);
    }

    public void stopAnimation() {
        this.mIsAnimating = false;
        this.mAnimation.clear();
        this.mHandler.removeMessages(1);
    }

    class AnimationItem
    {
        int mDuration;
        int mResId;

        AnimationItem(final int mResId, final int mDuration) {
            this.mResId = mResId;
            this.mDuration = mDuration;
        }
    }
}
