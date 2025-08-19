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
    let isSecure: Bool

    
    @FocusState private var isFocused: Bool
    @State private var isSecureVisible: Bool = false

    let borderColor: Color = AppColors.color(for: AppColors.Palette.stroke)

    private let height: CGFloat = 50
    private let cornerRadius: CGFloat = 10
    private let borderWidth: CGFloat = 1

    var body: some View {
        ZStack {
            RoundedRectangle(cornerRadius: cornerRadius)
                .stroke(borderColor, lineWidth: borderWidth)
                .background(
                    RoundedRectangle(cornerRadius: cornerRadius)
                        .fill(AppColors.Palette.baseBackground.color)
                )
                .frame(height: height)

            HStack {
                if isSecure {
                    Group {
                        if isSecureVisible {
                            TextField(placeholder, text: $text)
                                .focused($isFocused)
                                .autocapitalization(.none)
                                .disableAutocorrection(true)
                        } else {
                            SecureField(placeholder, text: $text)
                                .focused($isFocused)
                                .autocapitalization(.none)
                                .disableAutocorrection(true)
                        }
                    }
                } else {
                    TextField(placeholder, text: $text)
                        .focused($isFocused)
                        .autocapitalization(.none)
                        .disableAutocorrection(true)
                }

                
                if isSecure {
                    Button(action: { isSecureVisible.toggle() }) {
                        Image(systemName: isSecureVisible ? "eye.slash" : "eye")
                            .foregroundColor(AppColors.Palette.fontInactive.color)
                    }
                    .buttonStyle(.plain)
                }
            }
            .padding(.horizontal, 14)
        }
        .frame(height: height)
        .animation(.easeInOut(duration: 0.18), value: isFocused)
    }
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
