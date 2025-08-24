//
//  OutlinedTextfield.swift
//  InRussian
//
//  Created by dark type on 19.08.2025.
//

import SwiftUI

struct OutlinedTextfield: View {
    @Binding var text: String
    let placeholder: String

    // For email: false. For password fields: pass !showPassword from your store.
    let isSecure: Bool

    // Provide for password fields to show eye icon and toggle via store.
    var onToggleSecure: (() -> Void)? = nil

    // Parent controls navigation via submit key.
    var submitLabel: SubmitLabel = .done
    var onSubmit: (() -> Void)? = nil

    // Pass through content types and keyboard types so parent can choose (.password vs .newPassword).
    var textContentType: UITextContentType?
    var keyboardType: UIKeyboardType = .default

    private let height: CGFloat = 50
    private let cornerRadius: CGFloat = 10
    private let borderWidth: CGFloat = 1
    private let borderColor: Color = AppColors.Palette.stroke.color

    var body: some View {
        ZStack {
            RoundedRectangle(cornerRadius: cornerRadius)
                .stroke(borderColor, lineWidth: borderWidth)
                .background(
                    RoundedRectangle(cornerRadius: cornerRadius)
                        .fill(AppColors.Palette.baseBackground.color)
                )
                .frame(height: height)

            HStack(spacing: 8) {
                if isPasswordField {
                    // Keep both fields alive to avoid keyboard teardown when toggling
                    ZStack(alignment: .leading) {
                        TextField(placeholder, text: $text)
                            .textInputAutocapitalization(.never)
                            .autocorrectionDisabled(true)
                            .lineLimit(1)
                            .submitLabel(submitLabel)
                            .onSubmit { onSubmit?() }
                            .textContentType(textContentType)
                            .keyboardType(.default)
                            .opacity(isSecure ? 0 : 1)
                            .allowsHitTesting(!isSecure)

                        SecureField(placeholder, text: $text)
                            .textInputAutocapitalization(.never)
                            .autocorrectionDisabled(true)
                            .lineLimit(1)
                            .submitLabel(submitLabel)
                            .onSubmit { onSubmit?() }
                            .textContentType(textContentType)
                            .keyboardType(.default)
                            .opacity(isSecure ? 1 : 0)
                            .allowsHitTesting(isSecure)
                    }
                    .frame(maxWidth: .infinity, alignment: .leading)

                    if let onToggleSecure {
                        Button(action: onToggleSecure) {
                            (isSecure
                             ? AppImages.image(for: AppImages.Eye.close)
                             : AppImages.image(for: AppImages.Eye.open))
                                .resizable()
                                .renderingMode(.template)
                                .aspectRatio(contentMode: .fit)
                                .frame(width: 22, height: 22)
                                .foregroundColor(AppColors.Palette.fontInactive.color)
                        }
                        .buttonStyle(.plain)
                    }
                } else {
                    // Email / non-password: single TextField (simplest tap path)
                    TextField(placeholder, text: $text)
                        .textInputAutocapitalization(.never)
                        .autocorrectionDisabled(true)
                        .lineLimit(1)
                        .submitLabel(submitLabel)
                        .onSubmit { onSubmit?() }
                        .textContentType(textContentType)
                        .keyboardType(keyboardType)
                        .frame(maxWidth: .infinity, alignment: .leading)
                }
            }
            .padding(.horizontal, 14)
            .contentShape(Rectangle())
        }
        .frame(height: height)
    }

    private var isPasswordField: Bool { onToggleSecure != nil }
}

// MARK: - Preview

#if DEBUG
struct OutlinedTextfield_Previews: PreviewProvider {
    @State static var text1 = ""
    @State static var text2 = "Password"

    static var previews: some View {
        VStack(spacing: 24) {
            OutlinedTextfield(
                text: $text1,
                placeholder: "Email",
                isSecure: false
            )
            OutlinedTextfield(
                text: $text2,
                placeholder: "Пароль",
                isSecure: true
            )
        }
        .padding()
        .background(AppColors.Palette.baseBackground.color)
        .previewLayout(.sizeThatFits)
    }
}
#endif
