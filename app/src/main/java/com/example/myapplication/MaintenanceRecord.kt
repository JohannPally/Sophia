package com.example.myapplication

data class MaintenanceRecord(
    var inventoryNum: String,
    var workOrderNum: String,
    var serviceProvider: String,
    var serviceEngineeringCode: String,
    var faultCode: String,
    var ipmProcedure: String,
    var status: String,
)