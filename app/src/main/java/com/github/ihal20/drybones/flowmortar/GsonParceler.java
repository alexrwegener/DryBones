package com.github.ihal20.drybones.flowmortar;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import flow.Parceler;
import flow.Path;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

public class GsonParceler implements Parceler {
  private final Gson gson;

  public GsonParceler(Gson gson) {
    this.gson = gson;
  }

  public Parcelable wrap(Path instance) {
    try {
      String e = this.encode(instance);
      return new GsonParceler.Wrapper(e);
    } catch (IOException var3) {
      throw new RuntimeException(var3);
    }
  }

  public Path unwrap(Parcelable parcelable) {
    GsonParceler.Wrapper wrapper = (GsonParceler.Wrapper) parcelable;

    try {
      return this.decode(wrapper.json);
    } catch (IOException var4) {
      throw new RuntimeException(var4);
    }
  }

  private String encode(Path instance) throws IOException {
    StringWriter stringWriter = new StringWriter();
    JsonWriter writer = new JsonWriter(stringWriter);

    String var5;
    try {
      Class type = instance.getClass();
      writer.beginObject();
      writer.name(type.getName());
      this.gson.toJson(instance, type, writer);
      writer.endObject();
      var5 = stringWriter.toString();
    } finally {
      writer.close();
    }

    return var5;
  }

  private Path decode(String json) throws IOException {
    JsonReader reader = new JsonReader(new StringReader(json));

    Path var4;
    try {
      reader.beginObject();
      Class e = Class.forName(reader.nextName());
      var4 = this.gson.fromJson(reader, e);
    } catch (ClassNotFoundException var8) {
      throw new RuntimeException(var8);
    } finally {
      reader.close();
    }

    return var4;
  }

  private static class Wrapper implements Parcelable {
    final String json;
    public static final Creator<Wrapper> CREATOR = new Creator<Wrapper>() {
      public GsonParceler.Wrapper createFromParcel(Parcel in) {
        String json = in.readString();
        return new GsonParceler.Wrapper(json);
      }

      public GsonParceler.Wrapper[] newArray(int size) {
        return new GsonParceler.Wrapper[size];
      }
    };

    Wrapper(String json) {
      this.json = json;
    }

    public int describeContents() {
      return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
      out.writeString(this.json);
    }
  }
}
