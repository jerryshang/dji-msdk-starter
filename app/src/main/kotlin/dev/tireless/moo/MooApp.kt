package dev.tireless.moo

import android.app.Application
import android.content.Context
import dji.v5.common.callback.CommonCallbacks
import dji.v5.common.error.IDJIError
import dji.v5.common.register.DJISDKInitEvent
import dji.v5.manager.SDKManager
import dji.v5.manager.aircraft.payload.PayloadCenter
import dji.v5.manager.aircraft.payload.PayloadIndexType
import dji.v5.manager.interfaces.SDKManagerCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class MooApp : Application() {
  private val applicationScope = CoroutineScope(SupervisorJob())
  private val sdf =
    SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault()).apply {
      timeZone = TimeZone.getDefault()
    }

  private val _inMessageStream: MutableSharedFlow<String> = MutableSharedFlow()
  val inMessageStream: SharedFlow<String> = _inMessageStream

  val outMessageStream: MutableSharedFlow<String> = MutableSharedFlow()

  val eventStream: MutableSharedFlow<String> = MutableSharedFlow()

  fun postEvent(event: String) =
    applicationScope.launch {
      eventStream.emit("${sdf.format(Date())}: $event")
    }

  override fun attachBaseContext(base: Context?) {
    super.attachBaseContext(base)
    com.cySdkyc.clx.Helper
      .install(this)
    xcrash.XCrash.init(this)
  }

  @OptIn(ExperimentalStdlibApi::class)
  override fun onCreate() {
    super.onCreate()

    val payloadIndex = PayloadIndexType.LEFT_OR_MAIN

    // According to dji's community article,
    // SDKManager.init must be executed in main thread or in the activity
    // to make sure Helper.install() is properly finished.
    CoroutineScope(Dispatchers.Main).launch {
      SDKManager.getInstance().init(
        this@MooApp,
        object : SDKManagerCallback {
          override fun onInitProcess(
            event: DJISDKInitEvent?,
            totalProcess: Int,
          ) {
            postEvent("onInitProcess")
            if (event == DJISDKInitEvent.INITIALIZE_COMPLETE) {
              SDKManager.getInstance().registerApp()
            }
          }

          override fun onRegisterSuccess() {
            postEvent("onRegisterSuccess")
          }

          override fun onRegisterFailure(error: IDJIError?) {
            postEvent("onRegisterFailure, $error")
          }

          override fun onProductConnect(productId: Int) {
            postEvent("onProductConnect: $productId")
          }

          override fun onProductDisconnect(productId: Int) {
            postEvent("onProductDisconnect: $productId")
          }

          override fun onProductChanged(productId: Int) {
            postEvent("onProductChanged: $productId")
          }

          override fun onDatabaseDownloadProgress(
            current: Long,
            total: Long,
          ) {
            postEvent("onDatabaseDownloadProgress: ${current / total}")
          }
        },
      )

      PayloadCenter
        .getInstance()
        .payloadManager[payloadIndex]
        ?.addPayloadDataListener { bytes ->
          postEvent("onDataFromPayloadUpdate")
          try {
            applicationScope.launch {
              _inMessageStream.emit(bytes.toHexString())
            }
          } catch (e: Exception) {
            postEvent("onDataFromPayloadUpdate, error: $e")
          }
        }
    }
    applicationScope.launch {
      outMessageStream.collect {
        PayloadCenter
          .getInstance()
          .payloadManager[payloadIndex]
          ?.sendDataToPayload(
            it.encodeToByteArray(),
            object : CommonCallbacks.CompletionCallback {
              override fun onSuccess() {
                postEvent("onSuccess(sendDataToPayload)")
              }

              override fun onFailure(error: IDJIError) {
                postEvent("onFailure(sendDataToPayload): $error")
              }
            },
          )
          ?: postEvent("payload not online")
      }
    }
  }
}
