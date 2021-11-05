package com.project.iosephknecht.barcode_sender_plugin

import com.android.ddmlib.*
import com.android.ddmlib.log.LogReceiver
import com.android.sdklib.AndroidVersion
import com.google.common.util.concurrent.Futures
import java.io.File
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

internal fun mockDevice(
    name: String = "",
    serialNumber: String = "",
    avdName: String = name,
    isEmulator: Boolean = true,
    state: IDevice.DeviceState = IDevice.DeviceState.ONLINE,
    root: Boolean = true,
    isRoot: Boolean = true,
    batteryLevel: Int = 100,
    battery: Int = 100,
    abiList: List<String> = emptyList(),
    density: Int = 160,
    language: String = "EN",
    region: String = "",
    version: AndroidVersion = AndroidVersion(AndroidVersion.VersionCodes.R)
) = IDeviceMock(
    name = name,
    serialNumber = serialNumber,
    avdName = avdName,
    isEmulator = isEmulator,
    state = state,
    root = root,
    isRoot = isRoot,
    batteryLevel = batteryLevel,
    battery = battery,
    abiList = abiList,
    density = density,
    language = language,
    region = region,
    version = version
)

internal class IDeviceMock constructor(
    private val name: String,
    private val serialNumber: String,
    private val avdName: String,
    private val isEmulator: Boolean,
    private val state: IDevice.DeviceState,
    private val root: Boolean,
    private val isRoot: Boolean,
    private val batteryLevel: Int,
    private val battery: Int,
    private val abiList: List<String>,
    private val density: Int,
    private val language: String,
    private val region: String,
    private val version: AndroidVersion
) : IDevice {

    override fun getName(): String = name

    override fun executeShellCommand(
        command: String?,
        receiver: IShellOutputReceiver?,
        maxTimeToOutputResponse: Int
    ) = Unit

    override fun executeShellCommand(command: String?, receiver: IShellOutputReceiver?) = Unit

    override fun executeShellCommand(
        command: String?,
        receiver: IShellOutputReceiver?,
        maxTimeToOutputResponse: Long,
        maxTimeUnits: TimeUnit?
    ) = Unit

    override fun executeShellCommand(
        command: String?,
        receiver: IShellOutputReceiver?,
        maxTimeout: Long,
        maxTimeToOutputResponse: Long,
        maxTimeUnits: TimeUnit?
    ) = Unit

    override fun getSystemProperty(name: String?): Future<String> {
        return Futures.immediateFuture("")
    }

    override fun getSerialNumber(): String = serialNumber

    override fun getAvdName(): String = avdName

    override fun getState(): IDevice.DeviceState = state

    override fun getProperties(): MutableMap<String, String> = hashMapOf()

    override fun getPropertyCount(): Int = 0

    override fun getProperty(name: String?): String = ""

    override fun arePropertiesSet(): Boolean = false

    override fun getPropertySync(name: String?): String = ""

    override fun getPropertyCacheOrSync(name: String?): String = ""

    override fun supportsFeature(feature: IDevice.Feature?): Boolean = false

    override fun supportsFeature(feature: IDevice.HardwareFeature?): Boolean = false

    override fun getMountPoint(name: String?): String = ""

    override fun isOnline(): Boolean = state == IDevice.DeviceState.ONLINE

    override fun isEmulator(): Boolean = isEmulator

    override fun isOffline(): Boolean = state == IDevice.DeviceState.OFFLINE

    override fun isBootLoader(): Boolean = state == IDevice.DeviceState.BOOTLOADER

    override fun hasClients(): Boolean = false

    override fun getClients(): Array<Client> = arrayOf()

    override fun getClient(applicationName: String?): Client? = null

    override fun getSyncService(): SyncService? = null

    override fun getFileListingService(): FileListingService? = null

    override fun getScreenshot(): RawImage? = null

    override fun getScreenshot(timeout: Long, unit: TimeUnit?): RawImage? = null

    override fun startScreenRecorder(
        remoteFilePath: String?,
        options: ScreenRecorderOptions?,
        receiver: IShellOutputReceiver?
    ) = Unit

    override fun runEventLogService(receiver: LogReceiver?) = Unit

    override fun runLogService(logname: String?, receiver: LogReceiver?) = Unit

    override fun createForward(localPort: Int, remotePort: Int) = Unit

    override fun createForward(
        localPort: Int,
        remoteSocketName: String?,
        namespace: IDevice.DeviceUnixSocketNamespace?
    ) = Unit

    override fun removeForward(localPort: Int, remotePort: Int) = Unit

    override fun removeForward(
        localPort: Int,
        remoteSocketName: String?,
        namespace: IDevice.DeviceUnixSocketNamespace?
    ) = Unit

    override fun getClientName(pid: Int): String? = null

    override fun pushFile(local: String?, remote: String?) = Unit

    override fun pullFile(remote: String?, local: String?) = Unit

    override fun installPackage(
        packageFilePath: String?,
        reinstall: Boolean,
        vararg extraArgs: String?
    ) = Unit

    override fun installPackage(
        packageFilePath: String?,
        reinstall: Boolean,
        receiver: InstallReceiver?,
        vararg extraArgs: String?
    ) = Unit

    override fun installPackage(
        packageFilePath: String?,
        reinstall: Boolean,
        receiver: InstallReceiver?,
        maxTimeout: Long,
        maxTimeToOutputResponse: Long,
        maxTimeUnits: TimeUnit?,
        vararg extraArgs: String?
    ) = Unit

    override fun installPackages(
        apks: MutableList<File>?,
        reinstall: Boolean,
        installOptions: MutableList<String>?,
        timeout: Long,
        timeoutUnit: TimeUnit?
    ) = Unit

    override fun syncPackageToDevice(localFilePath: String?): String? = null

    override fun installRemotePackage(
        remoteFilePath: String?,
        reinstall: Boolean,
        vararg extraArgs: String?
    ) = Unit

    override fun installRemotePackage(
        remoteFilePath: String?,
        reinstall: Boolean,
        receiver: InstallReceiver?,
        vararg extraArgs: String?
    ) = Unit

    override fun installRemotePackage(
        remoteFilePath: String?,
        reinstall: Boolean,
        receiver: InstallReceiver?,
        maxTimeout: Long,
        maxTimeToOutputResponse: Long,
        maxTimeUnits: TimeUnit?,
        vararg extraArgs: String?
    ) = Unit

    override fun removeRemotePackage(remoteFilePath: String?) = Unit

    override fun uninstallPackage(packageName: String?): String? = null

    override fun reboot(into: String?) = Unit

    override fun root() = root

    override fun isRoot() = isRoot

    override fun getBatteryLevel() = batteryLevel


    override fun getBatteryLevel(freshnessMs: Long) = batteryLevel


    override fun getBattery(): Future<Int> {
        return Futures.immediateFuture(battery)
    }

    override fun getBattery(freshnessTime: Long, timeUnit: TimeUnit?): Future<Int> {
        return Futures.immediateFuture(battery)
    }

    override fun getAbis() = abiList

    override fun getDensity(): Int = density

    override fun getLanguage(): String = language

    override fun getRegion(): String = region

    override fun getVersion() = version
}