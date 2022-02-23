package com.example.myapplication

/**
 * This is the class representation of all the constituents of a maintenance record.
 * All modifications to the contents of an MR should start from here.
 */
data class DeviceTemplate(
    var deviceName: String,
    var weeklyMaintenance: String,
    var monthlyMaintenance: String,
    var yearlyMaintenance: String
)