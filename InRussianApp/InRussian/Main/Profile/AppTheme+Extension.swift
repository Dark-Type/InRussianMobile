import Shared
extension AppTheme {
    func readable() -> String {
        switch self {
        case .system: return "СИСТЕМА"
        case .light:  return "СВЕТЛАЯ"
        case .dark:   return "ТЁМНАЯ"
        default:
            
            return String(describing: self).uppercased()
        }
    }
}
