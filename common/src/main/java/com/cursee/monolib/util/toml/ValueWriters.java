package com.cursee.monolib.util.toml;

import static com.cursee.monolib.util.toml.BooleanValueReaderWriter.BOOLEAN_VALUE_READER_WRITER;
import static com.cursee.monolib.util.toml.DateValueReaderWriter.DATE_PARSER_JDK_6;
import static com.cursee.monolib.util.toml.DateValueReaderWriter.DATE_VALUE_READER_WRITER;
import static com.cursee.monolib.util.toml.MapValueWriter.MAP_VALUE_WRITER;
import static com.cursee.monolib.util.toml.NumberValueReaderWriter.NUMBER_VALUE_READER_WRITER;
import static com.cursee.monolib.util.toml.ObjectValueWriter.OBJECT_VALUE_WRITER;
import static com.cursee.monolib.util.toml.PrimitiveArrayValueWriter.PRIMITIVE_ARRAY_VALUE_WRITER;
import static com.cursee.monolib.util.toml.StringValueReaderWriter.STRING_VALUE_READER_WRITER;
import static com.cursee.monolib.util.toml.TableArrayValueWriter.TABLE_ARRAY_VALUE_WRITER;

public class ValueWriters {

  public static final ValueWriters WRITERS = new ValueWriters();

  public ValueWriter findWriterFor(Object value) {
    for (ValueWriter valueWriter : VALUE_WRITERS) {
      if (valueWriter.canWrite(value)) {
        return valueWriter;
      }
    }

    return OBJECT_VALUE_WRITER;
  }

  private ValueWriters() {}
  
  private static DateValueReaderWriter getPlatformSpecificDateConverter() {
    String specificationVersion = Runtime.class.getPackage().getSpecificationVersion();
    return specificationVersion != null && specificationVersion.startsWith("1.6") ? DATE_PARSER_JDK_6 : DATE_VALUE_READER_WRITER;
  }

  private static final ValueWriter[] VALUE_WRITERS = {
      STRING_VALUE_READER_WRITER, NUMBER_VALUE_READER_WRITER, BOOLEAN_VALUE_READER_WRITER, getPlatformSpecificDateConverter(),
      MAP_VALUE_WRITER, PRIMITIVE_ARRAY_VALUE_WRITER, TABLE_ARRAY_VALUE_WRITER
  };
}
