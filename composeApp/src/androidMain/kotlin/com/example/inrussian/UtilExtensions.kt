package com.example.inrussian

import com.example.inrussian.components.main.train.TaskType
import inrussian.composeapp.generated.resources.Res
import inrussian.composeapp.generated.resources.check_correct
import inrussian.composeapp.generated.resources.conncet_audio
import inrussian.composeapp.generated.resources.connect_image
import inrussian.composeapp.generated.resources.connect_translate
import inrussian.composeapp.generated.resources.delete
import inrussian.composeapp.generated.resources.fill_gaps
import inrussian.composeapp.generated.resources.listen
import inrussian.composeapp.generated.resources.read
import inrussian.composeapp.generated.resources.remember
import inrussian.composeapp.generated.resources.select
import inrussian.composeapp.generated.resources.speak
import inrussian.composeapp.generated.resources.write
import org.jetbrains.compose.resources.DrawableResource

fun TaskType.getImageRes(): DrawableResource =
    when(this){

        TaskType.WRITE -> Res.drawable.write
        TaskType.LISTEN -> Res.drawable.listen
        TaskType.READ -> Res.drawable.read
        TaskType.SPEAK -> Res.drawable.speak
        TaskType.REMIND -> Res.drawable.remember
        TaskType.MARK -> Res.drawable.check_correct
        TaskType.FILL -> Res.drawable.fill_gaps
        TaskType.CONNECT_AUDIO -> Res.drawable.conncet_audio
        TaskType.CONNECT_IMAGE -> Res.drawable.connect_image
        TaskType.CONNECT_TRANSLATE -> Res.drawable.connect_translate
        TaskType.SELECT -> Res.drawable.select
    }
