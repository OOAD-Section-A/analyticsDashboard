package dto;

import java.time.LocalDateTime;

public class MarginResultDTO {
    private final double marginConceded;
    private final double marginProtected;
    private final LocalDateTime periodStart;
    private final LocalDateTime periodEnd;

    public MarginResultDTO(double marginConceded, double marginProtected,
                           LocalDateTime periodStart, LocalDateTime periodEnd) {
        this.marginConceded = marginConceded;
        this.marginProtected = marginProtected;
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
    }

    public double getMarginConceded() { return marginConceded; }
    public double getMarginProtected() { return marginProtected; }
    public LocalDateTime getPeriodStart() { return periodStart; }
    public LocalDateTime getPeriodEnd() { return periodEnd; }
}
