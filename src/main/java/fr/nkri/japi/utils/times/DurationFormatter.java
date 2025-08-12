package fr.nkri.japi.utils.times;

import org.apache.commons.lang.time.DurationFormatUtils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DurationFormatter {

    private static final long MINUTE = TimeUnit.MINUTES.toMillis(1L);
    private static final long HOUR = TimeUnit.HOURS.toMillis(1L);
    private static final long DAY = TimeUnit.DAYS.toMillis(1L);
    private static final SimpleDateFormat FRENCH_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private static final ThreadLocal<DecimalFormat> REMAINING_SECONDS = ThreadLocal.withInitial(() -> new DecimalFormat("0.#"));
    private static final ThreadLocal<DecimalFormat> REMAINING_SECONDS_TRAILING = ThreadLocal.withInitial(() -> new DecimalFormat("0.0"));

    public static String getRemaining(final long millis, final boolean milliseconds) {
        return getRemaining(millis, milliseconds, true);
    }

    public static String getRemaining(final long duration, final boolean milliseconds, final boolean trail) {
        if (milliseconds && duration < MINUTE) {
            return getFormattedSeconds(duration, trail) + 's';
        }
        if (duration >= DAY) {
            return DurationFormatUtils.formatDuration(duration, "dd-HH:mm:ss");
        }
        return DurationFormatUtils.formatDuration(duration, (duration >= HOUR ? "HH:" : "") + "mm:ss");
    }

    private static String getFormattedSeconds(final long duration, final boolean trail) {
        DecimalFormat decimalFormat = trail ? REMAINING_SECONDS_TRAILING.get() : REMAINING_SECONDS.get();
        return decimalFormat.format(duration * 0.001D);
    }

    public static String getDurationWords(final long duration) {
        return DurationFormatUtils.formatDuration(duration, "H' heures 'm' minutes'");
    }

    public static String getDurationDate(final long duration) {
        return FRENCH_DATE_FORMAT.format(new Date(duration));
    }

    public static String getHour() {
        return new SimpleDateFormat("HH:mm").format(new Date());
    }

    public static String getCurrentDate() {
        return FRENCH_DATE_FORMAT.format(new Date());
    }
}
