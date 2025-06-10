package api.testdata;

import lombok.Getter;
import lombok.experimental.Accessors;
import utils.DateUtils;

@Getter
@Accessors(fluent = true)
public class DateRange {
    private final String start;
    private final String end;

    public DateRange(int startOffset, int endOffset) {
        this.start = DateUtils.generateFutureDate(startOffset).toString();
        this.end = DateUtils.generateFutureDate(endOffset).toString();
    }
}
