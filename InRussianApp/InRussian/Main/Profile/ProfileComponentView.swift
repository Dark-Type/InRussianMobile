//
//  ProfileComponentView.swift
//  InRussian
//
//  Created by dark type on 16.08.2025.
//


import SwiftUI
import Shared

struct ProfileComponentView: View {
    let component: ProfileComponent
    @StateValue private var stack: ChildStack<AnyObject, any ProfileComponentChild>

    init(component: ProfileComponent) {
        self.component = component
        _stack = StateValue(component.stack)
    }

    var body: some View {
        let current = stack.active.instance
        Group {
            if let main = current as? ProfileComponentChildMainChild {
                ProfileMainView(component: main.component)
            } else if let edit = current as? ProfileComponentChildEditProfileChild {
                EditProfileView(component: edit.component)
            } else if let about = current as? ProfileComponentChildAboutChild {
                StaticTextScreen(component: about.component, onBack: about.component.onBack)
            } else if let privacy = current as? ProfileComponentChildPrivacyPolicyChild {
                StaticTextScreen(component: privacy.component, onBack: privacy.component.onBack)
            } else {
                EmptyView()
            }
        }
        .animation(.default, value: stack.active.instance.hashObject())
    }
}

// MARK: - Main Profile Screen

struct ProfileMainView: View {
    let component: ProfileMainComponent
    @StateValue private var state: ProfileMainState

    init(component: ProfileMainComponent) {
        self.component = component
        _state = StateValue(component.state)
    }

    var body: some View {
        if state.isLoading {
            ProgressCentered()
                .padding(.top, 60)
        } else {
            ScrollView {
                VStack(alignment: .leading, spacing: 24) {
                    header
                    badges
                    actionButtons
                }
                .padding(16)
            }
        }
    }

    // Header section: avatar, name, registration date, notifications toggle, theme button
    private var header: some View {
        HStack(alignment: .top, spacing: 16) {
            AvatarView(avatarId: state.user?.avatarId)
            VStack(alignment: .leading, spacing: 10) {
                Text(state.displayName.isEmpty ? "—" : state.displayName)
                    .font(.title3).bold()
                Text("Дата регистрации: \(state.registrationDate)")
                    .font(.footnote)
                    .foregroundColor(.secondary)

                Toggle(isOn: Binding(
                    get: { state.notificationsEnabled },
                    set: { _ in component.toggleNotifications() }
                )) {
                    Text("Уведомления")
                }
                .toggleStyle(.switch)

                Button {
                    component.cycleTheme()
                } label: {
                    Label("Тема: \(state.theme.readable())", systemImage: "paintbrush")
                        .frame(maxWidth: .infinity)
                }
                .buttonStyle(.bordered)
            }
        }
    }

    private var badges: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("Значки")
                .font(.title3).bold()
            if state.badges.isEmpty {
                Text("Пока нет значков")
                    .foregroundColor(.secondary)
            } else {
                ScrollView(.horizontal, showsIndicators: false) {
                    HStack(spacing: 16) {
                        ForEach(state.badges, id: \.id) { badge in
                            BadgeView(badge: badge)
                        }
                    }
                }
            }
        }
    }

    private var actionButtons: some View {
        VStack(spacing: 12) {
            Button {
                component.onEditClick()
            } label: {
                Text("Редактировать профиль")
                    .frame(maxWidth: .infinity)
            }
            .buttonStyle(.borderedProminent)

            Button {
                component.onAboutClick()
            } label: {
                Text("О приложении")
                    .frame(maxWidth: .infinity)
            }
            .buttonStyle(.bordered)

            Button {
                component.onPrivacyPolicyClick()
            } label: {
                Text("Политика конфиденциальности")
                    .frame(maxWidth: .infinity)
            }
            .buttonStyle(.bordered)
        }
    }
}

// MARK: - Edit Profile Screen

struct EditProfileView: View {
    let component: EditProfileComponent
    @StateValue private var state: EditProfileState

    init(component: EditProfileComponent) {
        self.component = component
        _state = StateValue(component.state)
    }

    var body: some View {
        if state.isLoading || state.workingCopy == nil {
            ProgressCentered()
                .padding(.top, 60)
        } else {
            let working = state.workingCopy!
            ScrollView {
                VStack(alignment: .leading, spacing: 14) {
                    Text("Редактирование профиля")
                        .font(.title2).bold()

                    Group {
                        LabeledField("Фамилия", text: working.surname) { component.updateSurname(value: $0) }
                        LabeledField("Имя", text: working.name) { component.updateName(value: $0) }
                        LabeledField("Отчество", text: working.patronymic ?? "") { component.updatePatronymic(value: $0) }
                        LabeledField("Дата рождения (YYYY-MM-DD)", text: working.dob) { component.updateDob(value: $0) }
                        LabeledField("Гражданство", text: working.citizenship ?? "") { component.updateCitizenship(value: $0) }
                        LabeledField("Национальность", text: working.nationality ?? "") { component.updateNationality(value: $0) }
                        LabeledField("Страна проживания", text: working.countryOfResidence ?? "") { component.updateCountryOfResidence(value: $0) }
                        LabeledField("Город проживания", text: working.cityOfResidence ?? "") { component.updateCityOfResidence(value: $0) }
                        LabeledField("Образование", text: working.education ?? "") { component.updateEducation(value: $0) }
                        LabeledField("Цель регистрации", text: working.purposeOfRegister ?? "") { component.updatePurpose(value: $0) }
                    }

                    GenderPicker(selected: working.gender) {
                        component.updateGender(value: $0)
                    }

                    HStack(spacing: 12) {
                        Button {
                            component.onBack()
                        } label: {
                            Text("Назад")
                                .frame(maxWidth: .infinity)
                        }
                        .buttonStyle(.bordered)

                        Button {
                            component.save()
                        } label: {
                            if state.isSaving {
                                ProgressView().progressViewStyle(.circular)
                                    .frame(maxWidth: .infinity)
                            } else {
                                Text("Сохранить")
                                    .frame(maxWidth: .infinity)
                            }
                        }
                        .buttonStyle(.borderedProminent)
                        .disabled(!state.canSave || state.isSaving)
                    }
                    .padding(.top, 4)
                }
                .padding(16)
            }
        }
    }
}

// MARK: - Static Text (About / Privacy)

struct StaticTextScreen: View {
    let component: AnyObject
    let onBack: () -> Void
    @StateValue private var state: StaticTextState

    init(component: AboutComponent, onBack: @escaping () -> Void) {
        self.component = component as AnyObject
        _state = StateValue(component.state)
        self.onBack = onBack
    }

    init(component: PrivacyPolicyComponent, onBack: @escaping () -> Void) {
        self.component = component as AnyObject
        _state = StateValue(component.state)
        self.onBack = onBack
    }

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 20) {
                Text(state.title)
                    .font(.title2).bold()
                Text(state.text)
                    .font(.body)
                Button {
                    onBack()
                } label: {
                    Text("Назад")
                        .frame(maxWidth: .infinity)
                }
                .buttonStyle(.bordered)
            }
            .padding(16)
        }
    }
}

// MARK: - Reusable Views

private struct AvatarView: View {
    let avatarId: String?
    var body: some View {
        ZStack {
            Circle()
                .fill(Color(uiColor: .secondarySystemBackground))
            Text("A")
                .font(.title)
                .bold()
        }
        .frame(width: 96, height: 96)
    }
}

private struct BadgeView: View {
    let badge: Badge
    var body: some View {
        VStack(spacing: 6) {
            ZStack {
                Circle()
                    .fill(Color.accentColor.opacity(0.15))
                Text(badge.name.first.map { String($0).uppercased() } ?? "?")
                    .font(.title3).bold()
            }
            .frame(width: 64, height: 64)
            Text(badge.name)
                .font(.caption)
                .lineLimit(1)
        }
        .frame(minWidth: 72)
    }
}

private struct LabeledField: View {
    let label: String
    @State private var textValue: String
    let onChange: (String) -> Void

    init(_ label: String, text: String, onChange: @escaping (String) -> Void) {
        self.label = label
        _textValue = State(initialValue: text)
        self.onChange = onChange
    }

    var body: some View {
        VStack(alignment: .leading, spacing: 4) {
            Text(label)
                .font(.caption)
                .foregroundColor(.secondary)
            TextField("", text: Binding(
                get: { textValue },
                set: { newValue in
                    textValue = newValue
                    onChange(newValue)
                }
            ))
            .textFieldStyle(.roundedBorder)
            .font(.body)
        }
    }
}

private struct GenderPicker: View {
    let selected: Gender
    let onSelect: (Gender) -> Void

    var body: some View {
        VStack(alignment: .leading, spacing: 6) {
            Text("Пол")
                .font(.caption)
                .foregroundColor(.secondary)
            FlowLayout(spacing: 8, runSpacing: 8) {
                ForEach(Gender.entries, id: \.self) { g in
                    let isSel = g == selected
                    Button {
                        onSelect(g)
                    } label: {
                        Text(g.name)
                            .font(.footnote)
                            .padding(.horizontal, 12)
                            .padding(.vertical, 6)
                            .background(
                                Capsule().fill(isSel ? Color.accentColor : Color.gray.opacity(0.2))
                            )
                            .foregroundColor(isSel ? .white : .primary)
                    }
                    .buttonStyle(.plain)
                }
            }
        }
    }
}

// MARK: - Simple FlowLayout for chips

struct FlowLayout: Layout {
    
    typealias Cache = Void

    let spacing: CGFloat
    let runSpacing: CGFloat
    let alignment: HorizontalAlignment

    init(spacing: CGFloat = 8, runSpacing: CGFloat = 8, alignment: HorizontalAlignment = .leading) {
        self.spacing = spacing
        self.runSpacing = runSpacing
        self.alignment = alignment
    }

    // MARK: - Cache management (required by Layout)
    func makeCache(subviews: Subviews) -> Cache {
        
        return ()
    }

    func updateCache(_ cache: inout Cache, subviews: Subviews) {
        // No-op
    }

    // MARK: - Sizing (exact signature required by Layout)
    func sizeThatFits(proposal: ProposedViewSize, subviews: Subviews, cache: inout Cache) -> CGSize {
        
        let maxWidth = proposal.width ?? .greatestFiniteMagnitude

        var currentX: CGFloat = 0
        var currentRowHeight: CGFloat = 0
        var totalHeight: CGFloat = 0
        var maxRowWidth: CGFloat = 0

        for subview in subviews {
            let subviewSize = subview.sizeThatFits(ProposedViewSize(width: maxWidth, height: .greatestFiniteMagnitude))

            if currentX > 0 && currentX + subviewSize.width > maxWidth {
                // finish current row
                totalHeight += currentRowHeight
                maxRowWidth = max(maxRowWidth, currentX - spacing)
                // start new row
                currentX = 0
                currentRowHeight = 0
            }

            currentX += subviewSize.width + spacing
            currentRowHeight = max(currentRowHeight, subviewSize.height)
        }

        // last row
        if currentRowHeight > 0 || currentX > 0 {
            totalHeight += currentRowHeight
            maxRowWidth = max(maxRowWidth, currentX - spacing)
        }

        let finalWidth = proposal.width ?? maxRowWidth
        return CGSize(width: finalWidth, height: totalHeight)
    }

    // MARK: - Placement (exact signature required by Layout)
    func placeSubviews(in bounds: CGRect, proposal: ProposedViewSize, subviews: Subviews, cache: inout Cache) {
        let maxWidth = bounds.width

        var x = bounds.minX
        var y = bounds.minY
        var currentRowHeight: CGFloat = 0

        for subview in subviews {
            let subviewSize = subview.sizeThatFits(ProposedViewSize(width: maxWidth, height: .greatestFiniteMagnitude))

            // wrap to next line when necessary
            if x > bounds.minX && x + subviewSize.width > bounds.maxX {
                x = bounds.minX
                y += currentRowHeight + runSpacing
                currentRowHeight = 0
            }

            // place the subview
            subview.place(
                at: CGPoint(x: x, y: y),
                proposal: ProposedViewSize(width: subviewSize.width, height: subviewSize.height)
            )

            x += subviewSize.width + spacing
            currentRowHeight = max(currentRowHeight, subviewSize.height)
        }
    }
}

// Convenience initializer for short calls
extension FlowLayout {
    init(_ spacing: CGFloat = 8, _ runSpacing: CGFloat = 8) {
        self.init(spacing: spacing, runSpacing: runSpacing, alignment: .leading)
    }
}

// MARK: - Utilities

private func ProgressCentered() -> some View {
    AnyView(
        VStack {
            ProgressView()
            Text("Загрузка...")
                .font(.footnote)
                .foregroundColor(.secondary)
                .padding(.top, 8)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
    )
}

private extension AppTheme {
    func readable() -> String {
        switch self {
        case .system: return "СИСТЕМА"
        case .light:  return "СВЕТЛАЯ"
        case .dark:   return "ТЁМНАЯ"
        default:
            // In case codegen uses different casing
            return String(describing: self).uppercased()
        }
    }
}


private extension ProfileComponentChild {
    func hashObject() -> Int { (self as AnyObject).hash }
}
