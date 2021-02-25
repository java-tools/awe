package com.almis.awe.model.util.data;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.CellData;
import com.almis.awe.model.entities.Copyable;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.io.serialization.ValidatingObjectInputStream;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utilities for lists
 */
public final class ListUtil {

  private ListUtil() {
  }

  /**
   * Copy a single element
   *
   * @param element Element to copy
   * @param <T>     Element class
   * @return Copy of element
   * @throws AWException AWE exception
   */
  public static <T extends Copyable> T copyElement(T element) throws AWException {
    return element == null ? null : element.copy();
  }

  /**
   * Get a copy of a list
   *
   * @param source Source list
   * @param clazz  Element class
   * @param <T>    Element type
   * @return Copied list
   */
  public static <T> List<T> copyList(List<T> source, Class<T> clazz) {
    List<T> copy = null;
    if (source != null) {
      try {
        Constructor<T> constructor = clazz.getConstructors().length > 1 ? clazz.getConstructor(clazz) : null;
        copy = source.getClass().getConstructor().newInstance();
        for (T item : source) {
          if (constructor != null) {
            copy.add(constructor.newInstance(item));
          } else {
            copy.add(item);
          }
        }

      } catch (Exception exc) {
        copy = new ArrayList<>();
      }
    }
    return copy;
  }

  /**
   * Get a copy of a list
   *
   * @param source Source list
   * @param <T>    Element type
   * @return Copied list
   * @throws AWException Error generating a new instance of an element
   */
  public static <T extends Copyable> List<T> copyList(List<T> source) throws AWException {
    List<T> copy = null;
    if (source != null) {
      try {
        Constructor<? extends List> constructor = source.getClass().getConstructor();
        copy = constructor.newInstance();
      } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException exc) {
        copy = new ArrayList<>();
      }

      for (T item : source) {
        copy.add(item.copy());
      }
    }
    return copy;
  }

  /**
   * Get a copy of a map
   *
   * @param source Source map
   * @param clazz  Element class
   * @param <T>    Element type
   * @return Copied map
   */
  public static <T> Map<String, T> copyMap(Map<String, T> source, Class<T> clazz) {
    Map<String, T> copy = null;
    if (source != null) {
      try {
        Constructor<T> constructor = clazz.getConstructors().length > 1 ? clazz.getConstructor(clazz) : null;
        copy = source.getClass().getConstructor().newInstance();
        for (Map.Entry<String, T> entry : source.entrySet()) {
          if (constructor != null) {
            copy.put(entry.getKey(), constructor.newInstance(entry.getValue()));
          } else if (entry.getValue() instanceof JsonNode) {
            copy.put(entry.getKey(), (T) ((JsonNode) entry.getValue()).deepCopy());
          } else {
            copy.put(entry.getKey(), entry.getValue());
          }
        }
      } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException exc) {
        copy = new HashMap<>();
      }
    }
    return copy;
  }

  /**
   * Get a copy of a map
   *
   * @param source Source map
   * @param <T>    Element type
   * @return Copied map
   * @throws AWException Error generating a new instance of an element
   */
  public static <T extends Copyable> Map<String, T> copyMap(Map<String, T> source) throws AWException {
    Map<String, T> copy = null;
    if (source != null) {
      try {
        Constructor<? extends Map> constructor = source.getClass().getConstructor();
        copy = constructor.newInstance();
      } catch (Exception exc) {
        copy = new HashMap<>();
      }

      for (Map.Entry<String, T> item : source.entrySet()) {
        copy.put(item.getKey(), item.getValue() == null ? null : item.getValue().copy());
      }
    }
    return copy;
  }

  /**
   * Get a copy of a list of map of CellData
   * @param sourceRows
   */
  public static List<Map<String, CellData>> copyDataListRows(List<Map<String, CellData>> sourceRows) {
    List<Map<String, CellData>> targetRows = new ArrayList<>();
    // Copy rows
    sourceRows.forEach(row -> {
      Map<String, CellData> newRow = new HashMap<>();
      row.entrySet().forEach(cell -> newRow.put(cell.getKey(), new CellData(cell.getValue())));
      targetRows.add(newRow);
    });

    return targetRows;
  }

  /**
   * Serialize list
   *
   * @param out  Output stream
   * @param list List
   * @param <T>  Class
   * @throws IOException Error accessing to IO
   */
  public static <T> void writeList(ObjectOutputStream out, List<T> list) throws IOException {
    out.writeInt(list == null ? -1 : list.size());
    if (list != null) {
      for (T item : list) {
        out.writeObject(item);
      }
    }
  }

  /**
   * Deserialize list
   * @param in Input stream
   * @param clazz class
   * @param <T> class type
   * @return deserialize list
   * @throws IOException IO exception
   * @throws ClassNotFoundException Class not found exception
   */
  public static <T> List<T> readList(ObjectInputStream in, Class<T> clazz) throws IOException, ClassNotFoundException {
    int size = in.readInt();
    ValidatingObjectInputStream inputStream = new ValidatingObjectInputStream(in);
    inputStream.accept(clazz);
    List<T> list = null;
    if (size >= 0) {
      list = new ArrayList<>();
      for (int i = 0; i < size; i++) {
        list.add(clazz.cast(inputStream.readObject()));
      }
    }
    return list;
  }


  /**
   * Serialize list
   *
   * @param out Output stream
   * @param map List
   * @param <T> Class
   * @throws IOException Error accessing to IO
   */
  public static <T> void writeMap(ObjectOutputStream out, Map<String, T> map) throws IOException {
    out.writeInt(map == null ? -1 : map.size());
    if (map != null) {
      for (Map.Entry<String, T> entry : map.entrySet()) {
        out.writeObject(entry.getKey());
        out.writeObject(entry.getValue());
      }
    }
  }

  /**
   * Deserialize list
   * @param in Input stream
   * @param clazz Class
   * @param <T> type class
   * @return Map
   * @throws IOException Error accessing to IO
   * @throws ClassNotFoundException Class not found
   */
  public static <T> Map<String, T> readMap(ObjectInputStream in, Class<T> clazz) throws IOException, ClassNotFoundException {
    int size = in.readInt();
    ValidatingObjectInputStream inputStream = new ValidatingObjectInputStream(in);
    inputStream.accept(clazz);
    Map<String, T> map = null;
    if (size >= 0) {
      map = new HashMap<>();
      for (int i = 0; i < size; i++) {
        map.put((String) inputStream.readObject(), clazz.cast(inputStream.readObject()));
      }
    }
    return map;
  }
}
