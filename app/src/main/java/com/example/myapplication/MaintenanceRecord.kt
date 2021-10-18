package com.example.myapplication

data class MaintenanceRecord(
    val inventoryNum: String,
    val workOrderNum: String,
    val serviceProvider: String,
    val serviceEngineeringCode: String,
    val faultCode: String,
    val ipmProcedure: String,
    val status: String,
)