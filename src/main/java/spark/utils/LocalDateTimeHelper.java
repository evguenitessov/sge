package spark.utils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;

public enum LocalDateTimeHelper implements Helper<LocalDateTime>{
	
	dateTime {
		@Override
		public CharSequence apply(LocalDateTime arg0, Options arg1) throws IOException {
			if (arg0 != null)
				return DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(arg0);
			else
				return "";
		}
	}
	
}
