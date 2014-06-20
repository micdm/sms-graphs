package com.micdm.smsgraphs.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;

public abstract class Loader<Entity> extends AsyncTaskLoader<Entity> {

    public static interface OnLoadListener {
        public void onStartLoad();
        public void onFinishLoad();
    }

    private final OnLoadListener onLoadListener;

    public Loader(Context context, OnLoadListener onLoadListener) {
        super(context);
        this.onLoadListener = onLoadListener;
    }

    @Override
    protected void onStartLoading() {
        if (onLoadListener != null) {
            onLoadListener.onStartLoad();
        }
    }

    @Override
    public void deliverResult(Entity data) {
        super.deliverResult(data);
        if (onLoadListener != null) {
            onLoadListener.onFinishLoad();
        }
    }
}
