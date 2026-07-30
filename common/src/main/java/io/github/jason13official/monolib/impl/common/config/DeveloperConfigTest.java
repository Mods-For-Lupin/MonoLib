package io.github.jason13official.monolib.impl.common.config;

import io.github.jason13official.monolib.Constants;
import io.github.jason13official.monolib.api.common.EnvironmentSide;
import io.github.jason13official.monolib.api.common.config.ConfigGetterSetter.Commented;
import io.github.jason13official.monolib.platform.Services;
import java.util.ArrayList;
import java.util.List;

public class DeveloperConfigTest {

  public static boolean debug = false;
  public static Commented<Boolean> DEBUG = new Commented<>("debug", () -> debug, value -> debug = value, "Whether to log more information.");
  public static int myInteger = 4;
  public static Commented<Integer> MY_INT = new Commented<>("my_int", () -> myInteger, value -> myInteger = value, "Test int value");
  public static long myLong = 20L;
  public static Commented<Long> MY_LONG = new Commented<>("my_long", () -> myLong, value -> myLong = value, "Test long value");
  public static float myFloat = 5.0f;
  public static Commented<Float> MY_FLOAT = new Commented<>("my_float", () -> myFloat, value -> myFloat = value, "Test float value");
  public static double myDouble = 21.0D;
  public static Commented<Double> MY_DOUBLE = new Commented<>("my_double", () -> myDouble, value -> myDouble = value, "Test double value");
  public static String myString = "this is a test value";
  public static Commented<String> MY_STRING = new Commented<>("my_string", () -> myString, value -> myString = value, "Test String value");
  public static byte myByte = 6;
  public static Commented<Byte> MY_BYTE = new Commented<>("my_byte", () -> myByte, value -> myByte = value, "Test byte value");

  public static void performGetterSetterTest() {

    if (Services.PLATFORM.isDevelopmentEnvironment()) {
      System.out.println("loading config test...");
      ModSidedConfigIO.load(Services.PLATFORM.getConfigDirectory(), Constants.MOD_ID + "_test", EnvironmentSide.COMMON,
          new ArrayList<>(List.of(
              DeveloperConfigTest.DEBUG,
              DeveloperConfigTest.MY_INT,
              DeveloperConfigTest.MY_LONG,
              DeveloperConfigTest.MY_FLOAT,
              DeveloperConfigTest.MY_DOUBLE,
              DeveloperConfigTest.MY_STRING,
              DeveloperConfigTest.MY_BYTE
          )));
    }
  }
}
