package jpt.test;

public class StockView extends Stock {
  private final boolean isNegative;
  private final String symbolUrl;
  private final String parity;

  public StockView(Stock s, int ix) {
    super(s.getName(), s.getName2(), s.getUrl(), s.getSymbol(), s.getPrice(), s.getChange(), s
        .getRatio());
    this.isNegative = s.getChange() < 0;
    this.symbolUrl = "/stocks/".concat(s.getSymbol());
    this.parity = ix % 2 == 0 ? "even" : "odd";
  }

  public boolean isNegativeChange() {
    return isNegative;
  }

  public String getSymbolUrl() {
    return symbolUrl;
  }

  public String getParity() {
    return parity;
  }
}
