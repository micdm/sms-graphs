package com.micdm.smsgraphs.db.writers;

import android.content.ContentValues;
import android.content.Context;

import com.micdm.smsgraphs.data.Target;

public class DbTargetWriter extends DbWriter {

    public DbTargetWriter(Context context) {
        super(context);
    }

    public void write(Target target) {
        db.update("targets", getValues(target), "id = ?", new String[] {String.valueOf(target.id)});
    }

    private ContentValues getValues(Target target) {
        ContentValues result = new ContentValues();
        if (target.category == null)  {
            result.putNull("category_id");
        } else {
            result.put("category_id", target.category.id);
        }
        result.put("name", target.name);
        if (target.title == null) {
            result.putNull("title");
        } else {
            result.put("title", target.title);
        }
        return result;
    }
}
