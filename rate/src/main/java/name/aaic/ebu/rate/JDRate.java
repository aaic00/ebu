package name.aaic.ebu.rate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class JDRate {
  private static final DateTimeFormatter formatter =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

  private Long id;
  private String content;
  private LocalDateTime datetime;
  private List<String> pics;

  public void setId(final Long id) {
    this.id = id;
  }

  public Long getId() {
    return this.id;
  }

  public void setContent(final String content) {
    this.content = content;
  }

  public void setDatetime(final LocalDateTime datetime) {
    this.datetime = datetime;
  }

  public void setPics(final List<String> pics) {
    this.pics = pics;
  }

  public List<String> getPics() {
    return this.pics;
  }

  public String[] toCsv1() {
    // 商品编码 使用心得 评论时间（精确到秒，且不同） 评分
    return new String[] {this.id.toString(), this.content, this.datetime.format(formatter), ""};
  }

  public String[] toCsv2(final Integer count) {
    // 商品编码 通码 使用心得 评论时间 评分 标签 1 2 3 4 5
    int c = count;
    final int picCount = this.pics == null ? 0 : Math.min(this.pics.size(), 5);
    final int size = picCount > 0 ? 6 + picCount : 4;
    final String[] row = new String[size];
    row[0] = this.id.toString();
    row[1] = "";
    row[2] = this.content;
    row[3] = this.datetime.format(formatter);
    if (c <= 99 && size > 4) {
      row[4] = "";
      row[5] = "";
      for (int i = 0; i < picCount; i++) {
        row[6 + i] = this.id + "+" + (c < 9 ? "0" : "") + c++ + ".jpg";
      }
    }
    return row;
  }
}
