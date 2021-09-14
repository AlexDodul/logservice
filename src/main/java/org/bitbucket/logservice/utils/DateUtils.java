package org.bitbucket.logservice.utils;

import java.sql.Timestamp;
import java.util.Objects;
import lombok.experimental.UtilityClass;
import org.bitbucket.logservice.exception.DateFormatException;

@UtilityClass
public class DateUtils {

  public static final int DATA_LENGTH = 19;

  public String convertToEpoch(String date) {
    if (Objects.isNull(date) || date.isEmpty()){
      return null;
    }
    assert date.length() >= 10 : "Неверный формат даты";
    if (date.length() == 10) {
      return String.valueOf(Timestamp.valueOf(date + " 00:00:00").getTime());
    }
    if (date.length() == DATA_LENGTH) {
      return String.valueOf(Timestamp.valueOf(date).getTime());
    }
    throw new DateFormatException("Неверный формат даты");
  }

}
