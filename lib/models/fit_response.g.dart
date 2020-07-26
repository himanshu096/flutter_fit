// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'fit_response.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

FitResponse _$FitResponseFromJson(Map<String, dynamic> json) {
  return FitResponse(
    json['stepCount'] as int,
    json['distance'] as Map<String, dynamic>,
    json['heartRate'] as Map<String, dynamic>,
    (json['weight'] as num).toDouble(),
    json['startTime'],
    json['endTime'],
  );
}

Map<String, dynamic> _$FitResponseToJson(FitResponse instance) =>
    <String, dynamic>{
      'stepCount': instance.stepCount,
      'distance': instance.distance,
      'heartRate': instance.heartRate,
      'weight': instance.weight,
      'startTime': instance.startTime,
      'endTime': instance.endTime,
    };
