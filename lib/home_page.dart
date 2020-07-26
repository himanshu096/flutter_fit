import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutterfit/models/fit_response.dart';

class HomePage extends StatefulWidget {
  @override
  _HomePageState createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  static const platform = const MethodChannel('com.example.flutterfit/fitdata');
  List<FitResponse> fitData;

  Future<void> _getFitData() async {
    try {
      final result = await platform.invokeMethod('getFitData');
      List<dynamic> data = json.decode(result);
      List<FitResponse> fitDatalist = List<dynamic>.from(data)
          .map((model) => FitResponse.fromJson(model))
          .toList();

      setState(() {
        fitData = fitDatalist;
      });
    } on PlatformException catch (e) {
      print(e);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Column(
        children: <Widget>[
          Container(
            color: Colors.blueAccent.withOpacity(0.8),
            width: MediaQuery.of(context).size.width,
            height: MediaQuery.of(context).size.height / 4,
            child: Center(
              child: RaisedButton(
                color: Colors.blueAccent,
                onPressed: _getFitData,
                child: Text(
                  "Sync Data",
                  style: TextStyle(color: Colors.white),
                ),
              ),
            ),
          ),
          Container(
            height: MediaQuery.of(context).size.height * 3 / 4,
            child: ListView.builder(
                itemCount: fitData != null ? fitData.length : 0,
                itemBuilder: (BuildContext context, int index) {
                  return listRow(index);
                }),
          )
        ],
      ),
    );
  }

  Widget listRow(int index) {
    return Card(
      margin: EdgeInsets.symmetric(vertical: 10.0, horizontal: 10.0),
      child: Container(
          padding: EdgeInsets.all(10),
          child: Center(
              child: Column(
            children: <Widget>[
              Text(
                fitData[index].getTime,
                style: TextStyle(fontWeight: FontWeight.bold),
              ),
              Text('Distance travelled - ' +
                  fitData[index]?.distance['value'].toString()),
              Text('Steps - ' + fitData[index].stepCount.toString()),
              fitData[index].heartRate != null
                  ? Text('Avg. Heart rate - ' +
                      fitData[index].heartRate['value'].toString())
                  : SizedBox(),
              Text('Weight - ' + fitData[index].weight.toString()),
            ],
          ))),
    );
  }
}
