package com.example.myapplication

/**
 * This is the class representation of all the constituents of a maintenance record.
 * All modifications to the contents of an MR should start from here.
 */
data class MaintenanceRecord(
    var id: String,
    var workOrderNum: String,
    var serviceProvider: String,
    var serviceEngineeringCode: String,
    var faultCode: String,
    var ipmProcedure: String,
    var status: String,
    var timestamp: Long,
    var weeklyMaintenance: String,
    var monthlyMaintenance: String,
    var yearlyMaintenance: String
)