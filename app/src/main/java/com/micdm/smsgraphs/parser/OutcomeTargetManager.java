package com.micdm.smsgraphs.parser;

import android.content.Context;

import com.micdm.smsgraphs.data.Category;
import com.micdm.smsgraphs.data.outcomes.OutcomeTarget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OutcomeTargetManager {

    private static final String STORE_FILE_NAME = "targets.json";

    private final Context context;
    private final List<OutcomeTarget> targets = new ArrayList<OutcomeTarget>();

    public OutcomeTargetManager(Context context) {
        this.context = context;
    }

    private void load() {
        String content = getFileContent();
        if (content == null) {
            return;
        }
        try {
            JSONArray targetsJson = (JSONArray) new JSONTokener(content).nextValue();
            for (int i = 0; i < targetsJson.length(); i += 1) {
                JSONObject targetJson = targetsJson.getJSONObject(i);
                OutcomeTarget target = new OutcomeTarget(targetJson.getString("title"));
                target.setCategory(new Category(targetJson.getString("category")));
                targets.add(target);
            }
        } catch (JSONException e) {}
    }

    private String getFileContent() {
        try {
            FileInputStream input = context.openFileInput(STORE_FILE_NAME);
            StringBuilder builder = new StringBuilder();
            byte[] buffer = new byte[1024];
            int read;
            while ((read = input.read(buffer)) != -1) {
                builder.append(new String(buffer, 0, read));
            }
            input.close();
            return builder.toString();
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    private void save() {
        try {
            JSONArray targets = getJson();
            FileOutputStream output = context.openFileOutput(STORE_FILE_NAME, Context.MODE_PRIVATE);
            output.write(targets.toString().getBytes());
            output.close();
        } catch (JSONException e) {

        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
    }

    private JSONArray getJson() throws JSONException {
        JSONArray result = new JSONArray();
        for (OutcomeTarget target: targets) {
            result.put(getTargetJson(target));
        }
        return result;
    }

    private JSONObject getTargetJson(OutcomeTarget target) throws JSONException {
        JSONObject result = new JSONObject();
        result.put("title", target.getTitle());
        result.put("category", target.getCategory().getTitle());
        return result;
    }

    public OutcomeTarget get(String title) {
        for (OutcomeTarget target: targets) {
            if (target.getTitle().equals(title)) {
                return target;
            }
        }
        return null;
    }

    public void add(OutcomeTarget target) {
        targets.add(target);
        //save();
    }
}
