package mappers

import com.example.inrussian.stores.auth.register.RegisterStore.PersistableState
import com.example.inrussian.stores.auth.register.RegisterStore.State

fun PersistableState.toUiState(): State = State(
    email = this.email,
    emailError = null,
    password = this.password,
    showPassword = this.showPassword,
    passwordError = null,
    confirmPassword = this.confirmPassword,
    showConfirmPassword = this.showConfirmPassword,
    confirmPasswordError =null,
    loading = this.loading
)

fun State.toPersistableState(): PersistableState = PersistableState(
    email = email,
    password = password,
    showPassword = showPassword,
    confirmPassword = confirmPassword,
    showConfirmPassword = showConfirmPassword,
    emailErrorKey = null,
    passwordErrorKey = null,
    confirmPasswordErrorKey = null,
    loading = loading
)

