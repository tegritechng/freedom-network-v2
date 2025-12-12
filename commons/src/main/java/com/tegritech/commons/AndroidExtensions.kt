import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.VibrationEffect.DEFAULT_AMPLITUDE
import android.os.Vibrator

val Context.vibrator
    get() = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

fun Context.vibrate(durationInMillis: Long = 500, amplitude: Int = DEFAULT_AMPLITUDE) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrator.vibrate(VibrationEffect.createOneShot(durationInMillis, amplitude))
    } else {
        vibrator.vibrate(durationInMillis)
    }
}