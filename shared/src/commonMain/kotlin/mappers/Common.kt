package mappers

import com.example.inrussian.models.ErrorKey
import com.example.inrussian.models.ErrorType
import com.example.inrussian.utile.ErrorDecoder
import org.jetbrains.compose.resources.StringResource

fun ErrorKey.mapErrorKeyToStringResource(errorDecoder: ErrorDecoder = ErrorDecoder): StringResource {
    return when (this) {
        ErrorKey.INVALID_EMAIL -> errorDecoder.decode(ErrorType.InvalidEmail)
        ErrorKey.INVALID_PASSWORD

            -> errorDecoder.decode(ErrorType.InvalidPassword)

        ErrorKey.INVALID_CONFIRM_PASSWORD -> errorDecoder.decode(ErrorType.InvalidConfirmPassword)
    }
}