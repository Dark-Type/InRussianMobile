package com.example.inrussian.root.auth.register

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.value.MutableValue
import com.example.inrussian.components.onboarding.citizenship.CitizenshipComponent
import com.example.inrussian.models.models.auth.PeriodSpent
import com.example.inrussian.ui.theme.BackButton
import com.example.inrussian.ui.theme.ContinueButton
import com.example.inrussian.ui.theme.DarkGrey
import com.example.inrussian.ui.theme.LocalExtraColors
import com.example.inrussian.ui.theme.Orange
import inrussian.composeapp.generated.resources.Res
import inrussian.composeapp.generated.resources.checkmark_circle
import inrussian.composeapp.generated.resources.citizenship
import inrussian.composeapp.generated.resources.citizenship_data
import inrussian.composeapp.generated.resources.delete
import inrussian.composeapp.generated.resources.down_arrow
import inrussian.composeapp.generated.resources.eath
import inrussian.composeapp.generated.resources.nationality
import inrussian.composeapp.generated.resources.residence_city
import inrussian.composeapp.generated.resources.residence_country
import inrussian.composeapp.generated.resources.study_country
import inrussian.composeapp.generated.resources.time_of_stay
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

private val countryOptionsRu = listOf(
    "Австралия",
    "Австрия",
    "Азербайджан",
    "Албания",
    "Алжир",
    "Ангола",
    "Андорра",
    "Антигуа и Барбуда",
    "Аргентина",
    "Армения",
    "Афганистан",
    "Багамские Острова",
    "Бангладеш",
    "Барбадос",
    "Бахрейн",
    "Беларусь",
    "Белиз",
    "Бельгия",
    "Бенин",
    "Болгария",
    "Боливия",
    "Босния и Герцеговина",
    "Ботсвана",
    "Бразилия",
    "Бруней",
    "Буркина-Фасо",
    "Бурунди",
    "Бутан",
    "Вануату",
    "Ватикан",
    "Великобритания",
    "Венгрия",
    "Венесуэла",
    "Восточный Тимор",
    "Вьетнам",
    "Габон",
    "Гаити",
    "Гайана",
    "Гамбия",
    "Гана",
    "Гватемала",
    "Гвинея",
    "Гвинея-Бисау",
    "Германия",
    "Гондурас",
    "Гренада",
    "Греция",
    "Грузия",
    "Дания",
    "Джибути",
    "Доминика",
    "Доминиканская Республика",
    "Египет",
    "Замбия",
    "Зимбабве",
    "Израиль",
    "Индия",
    "Индонезия",
    "Иордания",
    "Ирак",
    "Иран",
    "Ирландия",
    "Исландия",
    "Испания",
    "Италия",
    "Йемен",
    "Кабо-Верде",
    "Казахстан",
    "Камбоджа",
    "Камерун",
    "Канада",
    "Катар",
    "Кения",
    "Кипр",
    "Киргизия",
    "Кирибати",
    "Китай",
    "Колумбия",
    "Коморы",
    "Конго",
    "Коста-Рика",
    "Кот-д’Ивуар",
    "Куба",
    "Кувейт",
    "Лаос",
    "Латвия",
    "Лесото",
    "Либерия",
    "Ливан",
    "Ливия",
    "Литва",
    "Лихтенштейн",
    "Люксембург",
    "Маврикий",
    "Мавритания",
    "Мадагаскар",
    "Макао",
    "Македония",
    "Малави",
    "Малайзия",
    "Мали",
    "Мальдивы",
    "Мальта",
    "Марокко",
    "Маршалловы Острова",
    "Мексика",
    "Мозамбик",
    "Молдова",
    "Монако",
    "Монголия",
    "Мьянма",
    "Намибия",
    "Науру",
    "Непал",
    "Нигер",
    "Нигерия",
    "Нидерланды",
    "Никарагуа",
    "Новая Зеландия",
    "Норвегия",
    "ОАЭ",
    "Оман",
    "Пакистан",
    "Палау",
    "Палестина",
    "Панама",
    "Папуа — Новая Гвинея",
    "Парагвай",
    "Перу",
    "Польша",
    "Португалия",
    "Россия",
    "Руанда",
    "Румыния",
    "Сальвадор",
    "Самоа",
    "Сан-Марино",
    "Сан-Томе и Принсипи",
    "Саудовская Аравия",
    "Свазиленд",
    "Северная Корея",
    "Северная Македония",
    "Сейшельские Острова",
    "Сенегал",
    "Сент-Винсент и Гренадины",
    "Сент-Китс и Невис",
    "Сент-Люсия",
    "Сербия",
    "Сингапур",
    "Сирия",
    "Словакия",
    "Словения",
    "Соломоновы Острова",
    "Сомали",
    "Судан",
    "Суринам",
    "США",
    "Сьерра-Леоне",
    "Таджикистан",
    "Таиланд",
    "Танзания",
    "Того",
    "Тонга",
    "Тринидад и Тобаго",
    "Тувалу",
    "Тунис",
    "Туркмения",
    "Турция",
    "Уганда",
    "Узбекистан",
    "Украина",
    "Уругвай",
    "Фиджи",
    "Филиппины",
    "Финляндия",
    "Франция",
    "Хорватия",
    "ЦАР",
    "Чад",
    "Черногория",
    "Чехия",
    "Чили",
    "Швейцария",
    "Швеция",
    "Шри-Ланка",
    "Эквадор",
    "Экваториальная Гвинея",
    "Эритрея",
    "Эстония",
    "Эфиопия",
    "ЮАР",
    "Южная Корея",
    "Южный Судан",
    "Ямайка",
    "Япония"
).sorted()

private val nationalityOptionsRu = listOf(
    "Абазин",
    "Абхаз",
    "Аварец",
    "Австралиец",
    "Азербайджанец",
    "Айны",
    "Айну",
    "Акан",
    "Албанец",
    "Алжирец",
    "Американец",
    "Англичанин",
    "Антигуанец",
    "Араб",
    "Аргентинец",
    "Армянин",
    "Ассириец",
    "Афганец",
    "Багулалец",
    "Балкарец",
    "Башкир",
    "Башкирин",
    "Беларус",
    "Белизец",
    "Белудж",
    "Бельгиец",
    "Бенинец",
    "Бирманец",
    "Болгарин",
    "Боливиец",
    "Босниец",
    "Ботлихец",
    "Ботсванец",
    "Бразилец",
    "Британец",
    "Брунейц",
    "Будуг",
    "Бурят",
    "Бутанец",
    "Венгр",
    "Венесуэлец",
    "Вепс",
    "Вьетнамец",
    "Гаитянин",
    "Гамбиец",
    "Ганец",
    "Гвинеец",
    "Гвинеец-Бисау",
    "Германец",
    "Гинухец",
    "Годоберинец",
    "Гондурасец",
    "Грек",
    "Гренландец",
    "Грузин",
    "Гуарани",
    "Дагестанец",
    "Даргинец",
    "Датчанин",
    "Дидой",
    "Долган",
    "Доминиканец",
    "Джухур",
    "Еврей",
    "Египтянин",
    "Эвен",
    "Эвенк",
    "Эквадорец",
    "Эрзя",
    "Эскимос",
    "Эстонец",
    "Эфиоп",
    "Жапонец",
    "Замбиец",
    "Зимбабвиец",
    "Ивановец",
    "Индиец",
    "Индонезиец",
    "Ингуш",
    "Иорданец",
    "Иранец",
    "Ирландец",
    "Исландиец",
    "Испанец",
    "Итальянец",
    "Ительмен",
    "Йеменец",
    "Кабардинец",
    "Казах",
    "Калмык",
    "Камерунец",
    "Канадец",
    "Караим",
    "Карачаевец",
    "Карел",
    "Катарец",
    "Кениец",
    "Кет",
    "Китайц",
    "Киргиз",
    "Кирибати",
    "Киприот",
    "Колумбиец",
    "Коми",
    "Конголезец",
    "Кореец",
    "Коряк",
    "Костариканец",
    "Кот-д’ивуарец",
    "Крымчак",
    "Кувейтец",
    "Кумык",
    "Кумыкец",
    "Кхмер",
    "Кыргызы",
    "Лакец",
    "Лаосец",
    "Латыш",
    "Лезгин",
    "Либанец",
    "Литовец",
    "Лихтенштейнец",
    "Люксембуржец",
    "Маврикиец",
    "Мавританец",
    "Мадагаскарец",
    "Малайзиец",
    "Малиец",
    "Мальдивец",
    "Мальтиец",
    "Манси",
    "Мариец",
    "Марокканец",
    "Маршалловец",
    "Мексиканец",
    "Молдаванин",
    "Монгол",
    "Мордва",
    "Мордвин",
    "Мозамбикец",
    "Мьянманец",
    "Нагаец",
    "Намибиец",
    "Нганасан",
    "Немец",
    "Нидерландец",
    "Нигериец",
    "Никарагуанец",
    "Нивх",
    "Новозеландец",
    "Ногай",
    "Норвежец",
    "Орок",
    "Осетин",
    "Палауец",
    "Панамец",
    "Папуас",
    "Пакистанец",
    "Парагваец",
    "Перуанец",
    "Поляк",
    "Португалец",
    "Пуэрториканец",
    "Румын",
    "Русский",
    "Рутулец",
    "Саам",
    "Саха",
    "Сальвадорец",
    "Самоанец",
    "Сенегалец",
    "Серб",
    "Сингапурец",
    "Сириец",
    "Словац",
    "Словенец",
    "Сойот",
    "Сомалиец",
    "Суданец",
    "Суринамец",
    "Сьерра-леонец",
    "Табарит",
    "Таджик",
    "Таец",
    "Танзаниец",
    "Татарин",
    "Татарин-крымский",
    "Тат",
    "Тиндинец",
    "Тофалар",
    "Тувинец",
    "Тунисец",
    "Туркмен",
    "Турок",
    "Удмурт",
    "Уильта",
    "Узбекистанец",
    "Узбек",
    "Украинец",
    "Уругваец",
    "Фиджиец",
    "Филиппинец",
    "Финн",
    "Француз",
    "Хакас",
    "Хант",
    "Хваршине",
    "Хиналуг",
    "Хорват",
    "Цез",
    "Цыган",
    "Чамалинец",
    "Черкес",
    "Чех",
    "Чеченец",
    "Чилиец",
    "Чуваш",
    "Чукча",
    "Швед",
    "Шотландец",
    "Шри-ланкиец",
    "Юкагир",
    "Южноафриканец",
    "Яванец",
    "Ямайец",
    "Якут",
    "Японец"
).sorted()



@Composable
fun CitizenshipDate(component: CitizenshipComponent) {
    val state by component.state.subscribeAsState()
    val currentColors = LocalExtraColors.current
    val isResidenceOpen = remember { mutableStateOf(false) }
    val isStudyCountryOpen = remember { mutableStateOf(false) }
    val context = LocalContext.current
    Column(
        Modifier
            .background(currentColors.secondaryBackground)
            .padding(horizontal = 22.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 64.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            BackButton(true, component::onBack)
            
            Text(
                stringResource(Res.string.citizenship_data),
                style = MaterialTheme.typography.titleLarge,
                color = currentColors.fontCaptive,
                textAlign = TextAlign.Center
            )
            
            ContinueButton(state.continueEnable, component::onNext)
        }
        
        Box(
            Modifier
                .weight(0.8f)
                .fillMaxWidth(),
        ) {
            Icon(
                vectorResource(Res.drawable.eath),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(bottom = 22.dp)
                    .align(Alignment.Center),
                tint = Orange
            )
        }
        
        Column(
            Modifier
                .fillMaxWidth()
                .padding(bottom = 50.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(currentColors.componentBackground)
        ) {
            // Гражданства — мультиселект (список стран)
            ClipsContainer(
                isOpen = state.isCitizenshipOpen,
                variants = countryOptionsRu,
                active = state.citizenship,
                onClick = component::deleteCountry,
                onChangeExpanded = component::openCitizenship,
                onAddClick = component::selectCountry,
                placeholder = stringResource(Res.string.citizenship)
            )
            HorizontalDivider(Modifier.padding(horizontal = 16.dp), color = currentColors.stroke)
            
            // Национальность — сингл-селект (список национальностей)
            SingleSelectRow(
                isOpen = state.isNationalityOpen,
                value = state.nationality,
                options = nationalityOptionsRu,
                placeholder = stringResource(Res.string.nationality),
                onChangeExpanded = component::openNationality,
                onSelect = component::selectNationality,
                onClear = { component.selectNationality("") })
            HorizontalDivider(Modifier.padding(horizontal = 16.dp), color = currentColors.stroke)
            
            // Страна проживания — сингл-селект (список стран)
            SingleSelectRow(
                isOpen = isResidenceOpen.value,
                value = state.countryOfResidence,
                options = countryOptionsRu,
                placeholder = stringResource(Res.string.residence_country),
                onChangeExpanded = { isResidenceOpen.value = it },
                onSelect = { choice -> component.countryLiveChange(choice) },
                onClear = { component.countryLiveChange("") })
            HorizontalDivider(Modifier.padding(horizontal = 16.dp), color = currentColors.stroke)
            
            InputFormField(
                state.cityOfResidence,
                component::cityLiveChange,
                stringResource(Res.string.residence_city)
            )
            HorizontalDivider(Modifier.padding(horizontal = 16.dp), color = currentColors.stroke)
            
            SingleSelectRow(
                isOpen = isStudyCountryOpen.value,
                value = state.countryDuringEducation,
                options = countryOptionsRu,
                placeholder = stringResource(Res.string.study_country),
                onChangeExpanded = { isStudyCountryOpen.value = it },
                onSelect = { choice -> component.studyCountyChange(choice) },
                onClear = { component.studyCountyChange("") })
            HorizontalDivider(Modifier.padding(horizontal = 16.dp), color = currentColors.stroke)
            
            SingleSelectRow(
                isOpen = state.isTimeOpen,
                value = state.timeSpentInRussia,
                options = PeriodSpent.entries.toList(),
                placeholder = stringResource(Res.string.time_of_stay),
                onChangeExpanded = component::openTime,
                onSelect = component::selectTime,
                onClear = { component.selectTime(null) })
        }
    }
}

@Composable
fun CommonClips(onClick: (String) -> Unit, text: String) {
    val currentColors = LocalExtraColors.current
    Box(Modifier.padding(end = 16.dp, bottom = 4.dp)) {
        Text(
            text,
            color = currentColors.fontInactive,
            modifier = Modifier
                .clip(RoundedCornerShape(18.dp))
                .background(DarkGrey.copy(0.1f))
                .border(
                    width = 1.dp, color = currentColors.stroke, shape = RoundedCornerShape(18.dp)
                )
                .padding(horizontal = 9.dp, vertical = 3.dp)
                .align(Alignment.Center)
        )
        IconButton(
            onClick = { onClick(text) },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(14.dp)
                .offset(7.dp, (-7).dp)
        ) {
            Icon(
                vectorResource(Res.drawable.delete),
                contentDescription = "",
                tint = currentColors.stroke
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClipsContainer(
    isOpen: Boolean,
    variants: List<String>,
    active: List<String>,
    onClick: (String) -> Unit,
    onChangeExpanded: (Boolean) -> Unit,
    onAddClick: (String) -> Unit,
    placeholder: String
) {
    val currentColors = LocalExtraColors.current
    
    Box(Modifier.fillMaxWidth()) {
        FlowRow(
            Modifier.padding(start = 16.dp, end = 12.dp, top = 12.dp, bottom = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            active.forEach { text ->
                CommonClips(onClick, text)
            }
            if (active.isEmpty()) {
                Text(placeholder, color = currentColors.fontCaptive)
            }
        }
        
        IconButton(
            onClick = { onChangeExpanded(!isOpen) }, modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Icon(
                vectorResource(Res.drawable.down_arrow),
                contentDescription = null,
                tint = currentColors.stroke
            )
        }
        
        DropdownMenu(
            expanded = isOpen,
            onDismissRequest = { onChangeExpanded(false) },
            offset = DpOffset(x = 220.dp, y = 0.dp),
            containerColor = currentColors.componentBackground
        ) {
            variants.forEach { option ->
                DropdownMenuItem(text = { Text(option) }, onClick = {
                    if (!active.contains(option)) onAddClick(option)
                    onChangeExpanded(false)
                })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SingleSelectRow(
    isOpen: Boolean,
    value: String,
    options: List<String>,
    placeholder: String,
    onChangeExpanded: (Boolean) -> Unit,
    onSelect: (String) -> Unit,
    onClear: () -> Unit
) {
    val currentColors = LocalExtraColors.current
    
    Box(
        Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 8.dp, top = 12.dp, bottom = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterStart),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val isEmpty = value.isEmpty()
            
            Text(
                if (isEmpty) placeholder else value,
                color = if (isEmpty) currentColors.fontInactive else currentColors.fontCaptive,
                modifier = Modifier.weight(1f)
            )
            
            IconButton(onClick = { onChangeExpanded(!isOpen) }, Modifier.size(32.dp)) {
                Icon(
                    vectorResource(Res.drawable.down_arrow),
                    contentDescription = null,
                    tint = currentColors.stroke
                )
            }
        }
        
        DropdownMenu(
            expanded = isOpen,
            onDismissRequest = { onChangeExpanded(false) },
            offset = DpOffset(x = 220.dp, y = 0.dp),
            containerColor = currentColors.componentBackground
        ) {
            options.forEach { option ->
                DropdownMenuItem(text = { Text(option) }, trailingIcon = {
                    if (option == value) {
                        Icon(
                            vectorResource(Res.drawable.checkmark_circle),
                            contentDescription = null,
                            tint = Orange,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }, onClick = {
                    onSelect(option)
                    onChangeExpanded(false)
                })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SingleSelectRow(
    isOpen: Boolean,
    value: PeriodSpent?,
    options: List<PeriodSpent>,
    placeholder: String,
    onChangeExpanded: (Boolean) -> Unit,
    onSelect: (PeriodSpent) -> Unit,
    onClear: () -> Unit
) {
    val currentColors = LocalExtraColors.current
    val context = LocalContext.current
    Box(
        Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 8.dp, top = 12.dp, bottom = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterStart),
            verticalAlignment = Alignment.CenterVertically
        ) {
            
            Text(
                text = value?.string?.getString(context) ?: placeholder,
                color = if (value == null) currentColors.fontInactive else currentColors.fontCaptive,
                modifier = Modifier.weight(1f)
            )
            
            IconButton(onClick = { onChangeExpanded(!isOpen) }, Modifier.size(32.dp)) {
                Icon(
                    vectorResource(Res.drawable.down_arrow),
                    contentDescription = null,
                    tint = currentColors.stroke
                )
            }
        }
        
        DropdownMenu(
            expanded = isOpen,
            onDismissRequest = { onChangeExpanded(false) },
            offset = DpOffset(x = 220.dp, y = 0.dp),
            containerColor = currentColors.componentBackground
        ) {
            options.forEach { option ->
                DropdownMenuItem(text = { Text(option.string.getString(context)) }, trailingIcon = {
                    if (option == value) {
                        Icon(
                            vectorResource(Res.drawable.checkmark_circle),
                            contentDescription = null,
                            tint = Orange,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }, onClick = {
                    onSelect(option)
                    onChangeExpanded(false)
                })
            }
        }
    }
}

class CitizenshipDate : CitizenshipComponent {
    
    override val state = MutableValue(
        CitizenshipComponent.State(isNationalityOpen = true)
    )
    
    override fun onNext() {
        TODO("Not yet implemented")
    }
    
    override fun onBack() {
        TODO("Not yet implemented")
    }
    
    override fun countryLiveChange(country: String) {
        TODO("Not yet implemented")
    }
    
    override fun cityLiveChange(city: String) {
        TODO("Not yet implemented")
    }
    
    override fun studyCountyChange(country: String) {
        TODO("Not yet implemented")
    }
    
    override fun selectCountry(country: String) {
        TODO("Not yet implemented")
    }
    
    override fun openCitizenship(isOpen: Boolean) {
        TODO("Not yet implemented")
    }
    
    override fun openNationality(isOpen: Boolean) {
        TODO("Not yet implemented")
    }
    
    override fun openTime(isOpen: Boolean) {
        TODO("Not yet implemented")
    }
    
    
    override fun selectNationality(string: String) {
        TODO("Not yet implemented")
    }
    
    override fun deleteCountry(country: String) {
        TODO("Not yet implemented")
    }
    
    override fun selectTime(time: PeriodSpent?) {
        TODO("Not yet implemented")
    }
    
   
    override fun deleteNationality(nationality: String) {
        TODO("Not yet implemented")
    }
    
    override fun deleteTime(time: String) {
        TODO("Not yet implemented")
    }
    
    @Preview(showBackground = true, showSystemUi = true)
    @Composable
    fun Preview() {
        CitizenshipDate(this)
    }
}
