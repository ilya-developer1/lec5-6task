package taslitsky.ilya.task1;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.Data;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class Fine {
  private String type;
  @SerializedName(value = "first_name")
  private String firstName;
  @SerializedName(value = "last_name")
  private String lastName;
  @SerializedName(value = "date_time")
  private String date;
  @SerializedName(value = "fine_amount")
  private Double amount;
}
