package com.project.iosephknecht.barcode_sender_plugin

import com.project.iosephknecht.barcode_sender_plugin.presentation.features.devices.DevicesFeatureContract.Device
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach

internal class DeviceEqualTest {

    private lateinit var deviceTest1: Device
    private lateinit var deviceTest1Copy: Device
    private lateinit var deviceTest2: Device

    @BeforeEach
    fun setUp() {
        val (device1, device1Copy) = mockDevice(name = "device_test_1")
            .let { Device(it) to Device(it) }

        deviceTest1 = device1
        deviceTest1Copy = device1Copy

        deviceTest2 = mockDevice(name = "device_test_2")
            .run(::Device)
    }

    @Test
    fun positiveEqualsTest() {
        Assertions.assertFalse(deviceTest1 === deviceTest1Copy)
        Assertions.assertEquals(deviceTest1, deviceTest1Copy)
    }

    @Test
    fun negativeEqualsTest() {
        Assertions.assertFalse(deviceTest1 === deviceTest2)
        Assertions.assertNotEquals(deviceTest1, deviceTest2)
    }
}