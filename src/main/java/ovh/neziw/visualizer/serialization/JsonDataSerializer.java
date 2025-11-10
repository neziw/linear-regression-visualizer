/*
 * This file is part of "linear-regression-visualizer", licensed under MIT License.
 *
 *  Copyright (c) 2025 neziw
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */
package ovh.neziw.visualizer.serialization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;
import ovh.neziw.visualizer.DataTableModel;

public class JsonDataSerializer {

    private final Gson gson;

    public JsonDataSerializer() {
        this.gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();
    }

    public String serialize(final List<DataTableModel.DataPoint> dataPoints) {
        return this.gson.toJson(dataPoints);
    }

    public List<DataTableModel.DataPoint> deserialize(final String json) throws IOException {
        final Type listType = new TypeToken<List<DataTableModel.DataPoint>>() {
        }.getType();
        final List<DataTableModel.DataPoint> dataPoints = this.gson.fromJson(json, listType);
        if (dataPoints == null) {
            throw new IOException("Plik jest pusty lub ma nieprawidłowy format");
        }
        return dataPoints;
    }

    public void writeToFile(final File file, final List<DataTableModel.DataPoint> dataPoints) throws IOException {
        final String json = this.serialize(dataPoints);
        try (final FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8)) {
            writer.write(json);
        }
    }

    public List<DataTableModel.DataPoint> readFromFile(final File file) throws IOException {
        final String json = this.readFileContent(file);
        return this.deserialize(json);
    }

    private String readFileContent(final File file) throws IOException {
        try (final FileReader reader = new FileReader(file, StandardCharsets.UTF_8);
             final BufferedReader bufferedReader = new BufferedReader(reader)) {

            final StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                jsonBuilder.append(line).append("\n");
            }
            return jsonBuilder.toString();
        }
    }
}
