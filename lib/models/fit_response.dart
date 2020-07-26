import 'package:intl/intl.dart';
import 'package:json_annotation/json_annotation.dart';
part 'fit_response.g.dart';

@JsonSerializable(nullable: false)
class FitResponse {
  int stepCount;
  Map<String, dynamic> distance;
  Map<String, dynamic> heartRate;
  double weight;
  var startTime;
  var endTime;

  FitResponse(this.stepCount, this.distance, this.heartRate, this.weight,
      this.startTime, this.endTime);

  factory FitResponse.fromJson(Map<String, dynamic> json) =>
      _$FitResponseFromJson(json);
  Map<String, dynamic> toJson() => _$FitResponseToJson(this);

  String get getTime {
    var date = DateTime.fromMillisecondsSinceEpoch(startTime);
    String formattedDate = DateFormat.yMMMd().format(date);
    return formattedDate;
  }
}
