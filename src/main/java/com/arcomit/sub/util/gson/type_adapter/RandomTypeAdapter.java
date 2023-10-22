package com.arcomit.sub.util.gson.type_adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Random;

/**
 * @author til
 */
public class RandomTypeAdapter extends TypeAdapter<Random>  {

    @Override
    public void write(JsonWriter out, Random value) throws IOException {
        out.value("This is just a placeholder. Deleting will result in null!");
    }

    @Override
    public Random read(JsonReader in) throws IOException {
        in.skipValue();
        return new Random();
    }
}
